

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import javafx.scene.chart.PieChart;
import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tunc on 3.03.2018.
 */
public class Database {

    private Connection conn = null;
    private static Database database = null;

    private Database() {

        connectDB();
    }

    public static Database getInstance(){
        if(database == null){
            database = new Database();
        }

        return database;
    }

    private void connectDB(){
        String dbURL = "jdbc:mysql://localhost:3306/nlp";
        String user = "root";
        Statement statement = null;
        ResultSet result = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL,user,null);
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int processDBData(TurkishMorphology morphology){
        Statement statement;
        ResultSet resultSet = null;
        int truePolarityCount = 0;
        Polarity polarity = new Polarity(morphology);

        try{

            statement = conn.createStatement();
            // int num = 0;
            // for(;num < 4000;num += 500){
            resultSet = statement.executeQuery("select p_text,final_label from processed");
            // }
            while(resultSet.next()){
                String text = resultSet.getString("p_text");
                //System.out.println(text);
                int calPolarity = polarity.calculatePolarity(text);
                if(resultSet.getInt("final_label") == calPolarity){
                    truePolarityCount++;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return truePolarityCount;
    }

    private ResultSet executeQueryToNaiveBayesPolarity(Word word){
        ResultSet resultSet = null;
        String query;

        if(word.getForm().equalsIgnoreCase("verb")){
            //System.out.println("Verb");
            query = ("select NBP,NBN,NBO from naivebayespolarity where word = '" + word.getInfinitive() + "'");

            try {
                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery(query);


                if(!resultSet.next()){

                    query = "select NBP,NBN,NBO from naivebayespolarity where word = '" + word.getRoot() + "'";
                    //System.out.println("Query changed");
                    //System.out.println(query);
                    resultSet = statement.executeQuery(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultSet;
        }
        else{
            query = "select NBP,NBN,NBO from naivebayespolarity where word = '" + word.getRoot() + "'";
        }
        //System.out.println(query);

        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;

    }

    private ResultSet executeQuery(Word word){

       /* if(conn == null){
            connectDB();
        }*/


        //System.out.println(word.getWord());
        ResultSet resultSet = null;
        String query;

        if(word.getForm().equalsIgnoreCase("verb")){
            //System.out.println("Verb");
            query = ("select pol from stn where kelimeName = '" + word.getInfinitive() + "'");

            try {
                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery(query);

                if(!resultSet.next()){
                    query = "select pol from stn where kelimeName = '" + word.getRoot() + "'";
                    //System.out.println("Query changed");
                    //System.out.println(query);
                    resultSet = statement.executeQuery(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultSet;
        }
        else{
            query = "select pol from stn where kelimeName = '" + word.getRoot() + "'";
        }
        //System.out.println(query);

        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /*private Word mapPolarity(ResultSet resultSet,Word word) {
        String polarity = null;
        //System.out.println("aaaaaaaaaaaaaaaaa");
        Word temp = word;
        try {
            polarity = resultSet.getString("polarity");
            //System.out.println(resultSet.getString("polarity"));
            //if(resultSet.next()){
                // THERE IS ONE SPACE BEFORE POLARITY LETTERS COMING FROM DB
                //resultSet.previous();
                //resultSet.previous();
                System.out.println("MAPPING ");
                 polarity = resultSet.getString("polarity");
                if(polarity.equals(" o")){
                    System.out.println("word: " + word.getWord());
                    temp.setPolarity(0);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> 0");
                }
                else if(polarity.equals(" p")){
                    System.out.println("word: " + word.getWord());
                    temp.setPolarity(1);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> 1");
                }
                else if(polarity.equals(" n")){
                    System.out.println("word: " + word.getWord());
                    temp.setPolarity(-1);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> -1");
                }
            //}

        } catch (SQLException e) {
            System.out.println(temp.getWord() +" is not on sentiturknet db");
            temp.setPolarity(0);
            try {
                resultSet.next();

                polarity = resultSet.getString("polarity");

                if(polarity.equals(" o")){
                    System.out.println("word: " + word.getWord());
                    temp.setPolarity(0);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> 0");
                }
                else if(polarity.equals(" p")){
                    System.out.println("word: " + word.getWord());
                    temp.setPolarity(1);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> 1");
                }
                else if(polarity.equals(" n")){
                    System.out.println("word: " + word.getWord());
                    temp.setPolarity(-1);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> -1");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }

        //System.out.println(" polarity: " + word.getPolarity());
        return temp;
    }*/

    private Word mapNaiveBayesPolarity(ResultSet resultSet, Word word) {
        int nbp = 0;
        int nbn = 0;
        int nbo = 0;
        try {
            nbp = resultSet.getInt("NBP");
            nbn = resultSet.getInt("NBN");
            nbo = resultSet.getInt("NBO");
        } catch (SQLException e) {
            System.out.println("Word not found");
        }catch (NullPointerException e){

        }


        int maxPolarity = 0;

        if(nbp > nbn && nbp > nbn){
            maxPolarity = 1;
        }
        else if(nbn > nbp && nbn > nbo){
            maxPolarity = -1;
        }
        else {
            maxPolarity = 0;
        }

        word.setPolarity(maxPolarity);

        return word;
    }
    private Word mapPolarity(ResultSet resultSet,Word word){
        Word temp = word;

        try {
            temp.setPolarity(resultSet.getInt("pol"));
            //System.out.println(resultSet.getInt("pol"));
            //System.out.println(temp.getWord() + " " + temp.getPolarity());
        } catch (MySQLSyntaxErrorException e){
            temp.setPolarity(0);
        } catch (SQLException e) {
            temp.setPolarity(0);
        }catch (NullPointerException e){
            System.out.println("null pointer");
            temp.setPolarity(0);
        }
        return temp;
    }

    public ArrayList<Word> getPolarities(ArrayList<Word> words) {

        ArrayList<Word> wordList = new ArrayList<Word>();

        for(Word word: words){
            if(!(word.getWord().equalsIgnoreCase("değil") || word.getWord().equalsIgnoreCase("degil"))) {
                ResultSet result = executeQuery(word);

                word = mapPolarity(result, word);
/*
                ResultSet resultSet = executeQueryToNaiveBayesPolarity(word);
                word = mapNaiveBayesPolarity(resultSet,word);
*/
                wordList.add(word);
            }
        }
        return wordList;
    }

}
