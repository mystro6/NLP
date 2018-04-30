import com.google.common.util.concurrent.UncheckedExecutionException;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tunc on 4.03.2018.
 */
public class Word {

    private String word;
    private String root;
    private int polarity;
    private String form;
    private String infinitive;

    public Word(String word,TurkishMorphology morphology){
        this.word = word;
        polarity = 0;
        System.out.println(word);
        List<WordAnalysis> analysis = null;
        try{
            analysis = morphology.analyze(word);
        }catch (UncheckedExecutionException exception){
            exception.printStackTrace();
        }


        try{
            root = analysis.get(0).getRoot();
        }catch(IndexOutOfBoundsException e){
            root = word;
        }
        analysis = morphology.analyze(root);
        try{
            form = analysis.get(0).getPos().shortForm;
        }catch(IndexOutOfBoundsException e){
            form = "unk";       //Form of the word couldnt be found
        }
        //System.out.println("Word:" + word + " root:" + root + " form:" + form);
    }

    public String getWord() {
        return word;
    }

    public String getRoot() {
        return root;
    }

    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

    public String getForm() {
        return form;
    }

    public String getInfinitive() {
        return infinitive;
    }

    public void setInfinitive(String infinitive) {
        this.infinitive = infinitive;
    }
}
