package DAO;

import Model.AdminBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mark DiFiglio
 * 4/29/2017
 * 
 * Controls access to the admin table.
 */
public class AdminDAO {
    
    /**
     * 
     * @param AdminDAO
     * @return 1 if good insert otherwise 0 for failed insert
     */
    public int createAdmin(AdminBean AdminDAO){
        int rowCount = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();          
            String salt = Hasher.generateHash(AdminDAO.getPassword()); //hash the password             
            String queryString = "INSERT INTO app.admin VALUES (?,?)";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, AdminDAO.getEmail()); //email
            pstmt.setString(2, salt); //password
            
            rowCount = pstmt.executeUpdate();
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return rowCount;
    }
    
    /**
     * 
     * @param email
     * @return true if email exist otherwise false
     */
    public boolean checkAdminEmail(String email){
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();          
            String queryString = "SELECT email FROM app.admin WHERE email = ?"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, email);            
            ResultSet rs = pstmt.executeQuery(); //sql call           
            if (rs.next()) return true;            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * 
     * @param email
     * @param password
     * @return true if good login otherwise false
     */
    public boolean AdminLogin(String email, String password){
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String salt = Hasher.generateHash(password); //hash the password               
            String queryString = "SELECT email, password FROM app.admin WHERE email = ? and password = '" + salt + "'"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, email); //email          
            ResultSet rs = pstmt.executeQuery(); //sql call          
            if (rs.next()) return true;           
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;  
    }
    
    /**
     * 
     * @param AdminDAO
     * @param originalEmail
     * @return 1 if good update otherwise 0 for failed update
     */
    public int updateAdmin(AdminBean AdminDAO, String originalEmail){
        int rowCount = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "UPDATE app.admin SET email = ? WHERE email = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, AdminDAO.getEmail()); //email
            //doesn't update password
            pstmt.setString(2, originalEmail); //original email            
            
            rowCount = pstmt.executeUpdate();                               
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return rowCount;
    }
    
    /**
     * 
     * @param email
     * @param password
     * @return 1 if good update otherwise 0 for failed update
     */
    public int changePassword(String email, String password){
        int rowCount = 0;           
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "UPDATE app.admin SET password  = ? WHERE email = ?"; //sql statement                       
            String salt = Hasher.generateHash(password); //hash the password            
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, salt); //password
            pstmt.setString(2, email); //email            
            rowCount = pstmt.executeUpdate(); //database call
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return rowCount;  
    }
    
}
