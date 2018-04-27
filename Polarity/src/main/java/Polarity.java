import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.util.*;

/**
 * Created by Tunc on 3.03.2018.
 */
public class Polarity {

    private TurkishMorphology morphology = null;

    public Polarity(TurkishMorphology morphology) {
        this.morphology = morphology;
    }

    public int calculatePolarity(String sentence) {

        int polarity = 0;
        Database db = new Database();

        sentence = sentence.toLowerCase();  //words in db are kept in lowercase

        String[] words = sentence.split(" ");

        ArrayList<Word> wordList = new ArrayList<Word>();

        //Create word objects and put them to a LL
        for (int i = 0; i < words.length; i++) {
            Word temp = new Word(words[i], morphology);
            wordList.add(temp);
        }

        for (Word word : wordList) {
            word.setInfinitive(fixInfinitives(word));
        }

        wordList = db.getPolarities(wordList);
        wordList = luSuz(wordList);
        wordList = negativityProcess(wordList);
        degilProcess(words,wordList);
        if(wordList.contains("ama") || wordList.contains("fakat")){
            calculatePolarityWithAmaFakat()
        }

        for (Word word : wordList) {
            polarity += word.getPolarity();
        }

        return polarity;
    }

    public String fixInfinitives(Word word) {
        String infinitive = null;
        String root = word.getRoot();

        System.out.println("Fixing Infinitives");
        System.out.println(word.getWord() + " -> " + word.getForm() + "\n");

        //TODO: look for first vowel not second letter  DONE

        if (word.getForm().equalsIgnoreCase("verb")) {
            for (int i = root.length() - 1; i > 0; i--) {
                if (root.charAt(i) == 'a' || root.charAt(i) == 'ı' || root.charAt(i) == 'o' || root.charAt(i) == 'u') {
                    infinitive = root + "mak";
                    //System.out.println(word.getWord() + " -> " + infinitive);
                    break;
                } else if (root.charAt(i) == 'e' || root.charAt(i) == 'i' || root.charAt(i) == 'ö' || root.charAt(i) == 'ü') {
                    infinitive = root + "mek";
                    //System.out.println(word.getWord() + " -> " + infinitive);
                    break;
                }
            }
        } else {
            infinitive = word.getWord();
        }
        return infinitive;
    }

    private int calculatePolarityWithAmaFakat(String sentence){
        String[] splittedSentence;

        if(sentence.contains("ama"))
        {
            splittedSentence = sentence.split("ama");

            return this.calculatePolarity(splittedSentence[1].substring(1));
        }
        else if(sentence.contains("fakat"))
        {
            splittedSentence = sentence.split("fakat");

            return this.calculatePolarity(splittedSentence[1].substring(1));
        }
        return 0;
    }

    private void degilProcess(String[] words, List<Word> wordList) {
        ArrayList<Integer> degilIndex = new ArrayList<Integer>();

        for (int i = 1; i < words.length; i++) {
            if (words[i].equalsIgnoreCase("değil") || words[i].equalsIgnoreCase("degil")) {
                degilIndex.add(i);
            }
        }

        for(Integer index: degilIndex){
            if(wordList.get(index - 1).getPolarity() == 0 && index -1 >= 0){
                if(wordList.get(index - 2).getPolarity() == 0 && index - 2 >= 0){
                    wordList.get(index -1 ).setPolarity(-1);
                }else{
                    wordList.get(index - 2).setPolarity(wordList.get(index - 2).getPolarity() * -1);
                }
            }else{
                wordList.get(index - 1).setPolarity(wordList.get(index - 1).getPolarity() * -1);
            }
        }
    }

    private ArrayList<Word> luSuz(ArrayList<Word> wordList) {

        ArrayList<Word> list = new ArrayList();

        for (int i=0;i<wordList.size();i++) {
            Word temp = wordList.get(i);
            List<WordAnalysis> results =  morphology.analyze(temp.getWord());
            for (WordAnalysis result : results) {
                 if (result.formatLong().contains("Without")) {
                     System.out.println(temp.getWord() +" " + temp.getPolarity());
                     temp.setPolarity(temp.getPolarity() * -1);
                     System.out.println("After lusuz");
                     System.out.println(temp.getWord() +" " + temp.getPolarity());
                 }
            }

            list.add(temp);
        }

        return list;
    }

    private ArrayList<Word> negativityProcess (ArrayList<Word> wordList) {

        ArrayList list = new ArrayList();

        for (int i = 0; i < wordList.size(); i++) {
            Word temp = wordList.get(i);
            List<WordAnalysis> results =  morphology.analyze(temp.getWord());
            for (WordAnalysis result : results) {
                if (result.formatLong().contains("Neg:ma") || result.formatLong().contains("Neg:me")) {
                    temp.setPolarity(temp.getPolarity() * -1);
                }
            }

            list.add(temp);
        }
        return list;
    }

    public void analyze(String word) {
        System.out.println("Word = " + word);
        List<WordAnalysis> results = morphology.analyze(word);
        for (WordAnalysis result : results) {
            System.out.println(result.formatLong());
           /* System.out.println(result.formatNoEmpty());
            System.out.println(result.formatOflazer());
            System.out.println(result.formatOnlyIgs());
            System.out.println(result.getLemma());
            System.out.println(result.getStemAndEnding());
            System.out.println(result.getRoot());*/
        }
    }

}
