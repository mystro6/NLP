import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tunc on 3.03.2018.
 */
public class Database {

    private Connection conn = null;

    public Database() {
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

    private ResultSet executeQuery(Word word){

        if(conn == null){
            connectDB();
        }

        ResultSet resultSet = null;
        String query = ("select polarity from stn where kelimeName = '" + word.getInfinitive() + "'");
        System.out.println(query);

        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

            if(!resultSet.next()){
                query = "select polarity from stn where kelimeName = '" + word.getRoot() + "'";
                System.out.println("Query changed");
                System.out.println(query);
                resultSet = statement.executeQuery(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    private Word mapPolarity(ResultSet resultSet,Word word){
        String polarity = null;

        try {
            if(resultSet.next()){
                 polarity = resultSet.getString("polarity");
                if(polarity.equals("o")){
                    word.setPolarity(0);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> 0");
                }
                else if(polarity.equals("p")){
                    word.setPolarity(1);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> 1");
                }
                else if(polarity.equals("n")){
                    word.setPolarity(-1);
                    System.out.println(word.getWord() + " -> " + word.getRoot() + " -> -1");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return word;
    }

    public List<Word> getPolarities(List<Word> words) {

        List<Word> wordList = new LinkedList<Word>();

        for(Word word: words){
            ResultSet result = executeQuery(word);

            word = mapPolarity(result,word);
            wordList.add(word);
        }

        return wordList;
    }

}
