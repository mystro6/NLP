import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.sql.*;

/**
 * Created by Tunc on 29.04.2018.
 */
public class DatabaseProcessor {

    private TurkishMorphology morphology;
    private Connection connection;
    private int truePolarityCount = 0;
    private Polarity polarity;

    public DatabaseProcessor(TurkishMorphology morphology) {
        this.morphology = morphology;
        polarity = new Polarity(morphology);
    }

    public int processDBData(){
        String dbURL = "jdbc:mysql://172.17.16.28/preprocess?useSSL=false";
        String user = "preprocess";
        String password = "laplappre";
        Statement statement;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {

        }

        try{
            connection = DriverManager.getConnection(dbURL,user,password);

            statement = connection.createStatement();
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
}
