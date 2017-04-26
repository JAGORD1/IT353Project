/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author IT353S704
 */
public class TokensDAO implements TokensDAO_Interface{
    @Override
    public String submitToken(String id){
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }     
        int rowCount = 0;
        long token = 0;
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");            
            
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

                String hash = Hasher.generateHash(String.valueOf(token)); //hash the password            

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
    
    @Override
    public String verifyToken(String token){
        String id = "";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            
            String salt = Hasher.generateHash(token); //hash the token  
            
            String queryString = "SELECT * FROM app.tokens WHERE token = ?"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, salt);  
            
            ResultSet rs = pstmt.executeQuery(); //sql call

            if (rs.next()) {
                //check timestamp
                //if (rs.getTimestamp("timestamp") );
                id = rs.getString("id"); //get id               
            }           
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id; 
    }
}
