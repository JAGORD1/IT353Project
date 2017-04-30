package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author Mark DiFiglio
 * 4/19/2017
 * 
 * Controls access to the tokens table.
 */
public class TokensDAO implements TokensDAO_Interface{
   
    /**
     * 
     * @param id
     * @return A String token to link an account. 
     */
    @Override
    public String submitToken(String id){         
        int rowCount = 0;
        long token = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();         
            
            //check if exist
            String queryString2 = "SELECT id FROM app.tokens WHERE id = ?";
            PreparedStatement pstmt2 = DBConn.prepareStatement(queryString2);
            pstmt2.setString(1, id); //id
            ResultSet rs = pstmt2.executeQuery();
            if (rs.next()) { //delete row
               queryString2 = "DELETE FROM app.tokens WHERE id = ?";
               pstmt2 = DBConn.prepareStatement(queryString2);
               pstmt2.setString(1, id);
               pstmt2.executeUpdate();
            }            
            
            Random rand = new Random();
            
            while (rowCount == 0) {          
                int n = rand.nextInt(10000);//gets a random number under 10,000
                long millis = System.currentTimeMillis();
                token = millis + n; //hash this

                String hash = Hasher.generateHash(String.valueOf(token)); //hash the token            

                String queryString = "INSERT INTO app.tokens VALUES (?,?,?)";
                PreparedStatement pstmt = DBConn.prepareStatement(queryString);
                pstmt.setString(1, id); //id
                pstmt.setString(2, hash); //token
                java.sql.Timestamp time = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()); //gets current time
                System.out.println(time);
                pstmt.setTimestamp(3, time); //timestamp                
                
                rowCount = pstmt.executeUpdate(); //adds to tokens
            }
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        

        return String.valueOf(token);
    }
    
    /**
     * 
     * @param token
     * @return String of the users email if a valid token otherwise "";
     */
    @Override
    public String verifyToken(String token){
        String id = "";
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            
            String salt = Hasher.generateHash(token); //hash the token  
            
            String queryString = "SELECT * FROM app.tokens WHERE token = ?"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, salt);  
            
            ResultSet rs = pstmt.executeQuery(); //sql call

            if (rs.next()) {
                //check timestamp
                if (Long.parseLong(token) + 300000 > System.currentTimeMillis()){ //300000 is 5 minutes + up to 10 random secs
                    id = rs.getString("id"); //get id 
                } else {//timed out delete token from table
                    queryString = "DELETE FROM app.tokens WHERE token = ?"; //sql statement
                    pstmt = DBConn.prepareStatement(queryString);
                    pstmt.setString(1, salt);
                    pstmt.executeUpdate(); //sql call
                }              
            }           
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id; 
    }
}
