import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.util.List;

/**
 * Created by Tunc on 4.03.2018.
 */
public class Word {

    private String word;
    private String root;
    private int polarity;
    private String form;
    private String infinitive;

    public Word(String word,TurkishMorphology morphology) {
        this.word = word;
        polarity = 0;

        List<WordAnalysis> analysis = morphology.analyze(word);
        root = analysis.get(0).getRoot();
        analysis = morphology.analyze(root);
        form = analysis.get(0).getPos().shortForm;
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
