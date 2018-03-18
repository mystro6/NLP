import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.io.IOException;

/**
 * Created by Tunc on 3.03.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        Polarity polarity = new Polarity(morphology);

        String sentence = "Bütün bu olanlar çok kötü oldu";
        System.out.println(polarity.calculatePolarity(sentence));

       // System.out.println(morphology.analyze("gelme").get(0).getPos().shortForm);        How to get words form

        sentence = "Kalemlerimi çalmış şerefsizler";
        System.out.println(polarity.calculatePolarity(sentence));

       // polarity.analyze("kalemlerim");
    }
}

/*
    In PrimaryPos class

    Noun("Noun"),
    Adjective("Adj"),
    Adverb("Adv"),
    Conjunction("Conj"),
    Interjection("Interj"),
    Verb("Verb"),
    Pronoun("Pron"),
    Numeral("Num"),
    Determiner("Det"),
    PostPositive("Postp"),
    Question("Ques"),
    Duplicator("Dup"),
    Punctuation("Punc"),
    Unknown("Unk");

 */