import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tunc on 3.03.2018.
 */
public class Polarity {

    private TurkishMorphology morphology = null;

    public Polarity(TurkishMorphology morphology){
        this.morphology = morphology;
    }

    public int calculatePolarity(String sentence){

        int polarity = 0;
        Database db = new Database();

        sentence = sentence.toLowerCase();  //words in db are kept in lowercase

        String[] words = sentence.split(" ");

        List<Word> wordList = new LinkedList<Word>();

        //Create word objects and put them to a LL
        for(int i = 0; i < words.length; i++){
            Word temp = new Word(words[i],morphology);
            wordList.add(temp);
        }

        for(Word word:wordList){
            word.setInfinitive(fixInfinitives(word));
        }

        wordList = db.getPolarities(wordList);


        for(Word word:wordList){
            polarity += word.getPolarity();
        }

        return polarity;
    }

    public String fixInfinitives(Word word){
        String infinitive = null;
        String root = word.getRoot();
        char lastSecondLetter = root.charAt(root.length()-2);

        System.out.println("Fixing Infinitives");
        System.out.println(word.getWord() + " -> " + word.getForm()+"\n");

        if(word.getForm().equalsIgnoreCase("verb")){
            for(int i = root.length()-1; i > 0 ;i--){
                if(lastSecondLetter == 'a' || lastSecondLetter == 'ı' || lastSecondLetter == 'o' || lastSecondLetter == 'u'){
                    infinitive = root + "mak";
                    System.out.println(word.getWord() + " -> " + infinitive);
                    break;
                }
                else if(lastSecondLetter == 'e' || lastSecondLetter == 'i' || lastSecondLetter == 'ö' || lastSecondLetter == 'ü'){
                    infinitive = root + "mek";
                    System.out.println(word.getWord() + " -> " + infinitive);
                    break;
                }
            }
        }
        else{
            infinitive = word.getWord();
        }
        return infinitive;
    }

    /*public void analyze(String word) {
        System.out.println("Word = " + word);
        List<WordAnalysis> results = morphology.analyze(word);
        for (WordAnalysis result : results) {
            System.out.println(result.formatLong());
            //System.out.println(result.formatNoEmpty());
            //System.out.println(result.formatOflazer());
            //System.out.println(result.formatOnlyIgs());
            System.out.println(result.getLemma());
            System.out.println(result.getStemAndEnding());
            System.out.println(result.getRoot());
        }
    }*/

}
