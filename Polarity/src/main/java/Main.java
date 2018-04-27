import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.io.IOException;

/**
 * Created by Tunc on 3.03.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        Polarity polarity = new Polarity(morphology);
        int polarityNum = 0;
        String sentence = "Bunu yapması çok iyi";
        //sentence = "bütün bu olanlar çok kötü oldu";
        //sentence = "çok güzel olmuş";
        sentence = "sınavlarim çok kötü geçti ama notum çok iyi geldi";
        //polarity.analyze(sentence);
        polarityNum = polarity.calculatePolarity(sentence);
        System.out.println(sentence + " -> " + polarityNum);
        System.out.println();
        //polarity.analyze("olmasın");
        //System.out.println(morphology.analyze("gelme").get(0).getPos().shortForm);        //How to get words form


       // polarityNum = polarity.calculatePolarity(sentence);
       // System.out.println(sentence + " -> " + polarityNum);

        // polarity.analyze("kalemlerim");
        //System.out.println(morphology.analyze("açık").get(0).getPos());
       /* System.out.println(morphology.analyze("fena").get(0).getPos());
        System.out.println(morphology.analyze("yanlış").get(0).getPos());
        System.out.println(morphology.analyze("zaman").get(0).getPos());
        System.out.println(morphology.analyze("öyle").get(0).getPos());
        System.out.println(morphology.analyze("sonuç").get(0).getPos());
        System.out.println(morphology.analyze("kırmızı").get(0).getPos());
        System.out.println(morphology.analyze("benim").get(0).getPos());
        System.out.println(morphology.analyze("kork").get(0).getPos());
*/


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