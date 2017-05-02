package DAO;

import Model.UniversityBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Mark DiFiglio
 * 4/19/2017
 * 
 * Controls access to the university table.
 */
public class UniversityDAO implements UniversityDAO_Interface{
    /**
     * 
     * @param universityDAO
     * @return 1 is good insert otherwise 0.
     */
    @Override
    public int createUniversity(UniversityBean universityDAO) {
        int rowCount = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();         
            String salt = Hasher.generateHash(universityDAO.getPassword()); //hash the password             
            String queryString = "INSERT INTO app.university VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityDAO.getUniversityName()); //university name
            pstmt.setString(2, universityDAO.getEmail()); //email
            pstmt.setString(3, salt); //password
            pstmt.setString(4, universityDAO.getVideoURL()); //video
            pstmt.setString(5, universityDAO.getImageURL()); //images
            pstmt.setString(6, universityDAO.getApplyURL()); //applyURL
            pstmt.setString(7, universityDAO.getMajors()); //majors
            pstmt.setString(8, universityDAO.getState()); //state
            pstmt.setString(9, universityDAO.getCity()); //city
            pstmt.setDouble(10, universityDAO.getCost()); //cost
            pstmt.setString(11, universityDAO.getEssay()); //essay
            pstmt.setBoolean(12, universityDAO.isPaid()); //paid           
            pstmt.setBoolean(13, universityDAO.getMailList()); //mail_list
            
            rowCount = pstmt.executeUpdate(); 
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return rowCount;    
    }
    
     /**
     * Checks to database if the given university name is in the database.
     * @param universityName
     * @return Returns true if it already exists otherwise false.
     */
    @Override
    public boolean checkUniversityName(String universityName) {      
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();          
            String queryString = "SELECT university_name FROM app.university WHERE university_name = ?"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityName);            
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
     * @return Returns true if it already exists otherwise false;
     */
    @Override
    public boolean checkEmail(String email){
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();          
            String queryString = "SELECT email FROM app.university WHERE email = ?"; //sql statement
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
     * @param universityName
     * @param password
     * @return True if login information is good otherwise false.
     */
    @Override
    public boolean universityLogin(String email, String password) {     
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String salt = Hasher.generateHash(password); //hash the password               
            String queryString = "SELECT email, password FROM app.university WHERE email = ? and password = ?"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, email);
            pstmt.setString(2, salt);
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
     * @param searchUniversity
     * @return ArrayList of UniversityBeans which match the given UniversityBean
     */
    @Override
    public ArrayList<UniversityBean> searchUniversity(UniversityBean searchUniversity) {
        ArrayList<UniversityBean> universityBeanCollection = new ArrayList<>(); 
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();           
            
            //get search records
            String queryString = "SELECT * FROM app.university WHERE university_name LIKE ? AND email LIKE ? "
                    + "AND major LIKE ? AND state LIKE ? AND city LIKE ? AND cost <= ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            
            if (searchUniversity.getUniversityName() == null || searchUniversity.getUniversityName().isEmpty()) { //university_name
                pstmt.setString(1, "%");
            }else pstmt.setString(1, DatabaseHelper.percentWrap(searchUniversity.getUniversityName()));
            
            if (searchUniversity.getEmail() == null || searchUniversity.getEmail().isEmpty()) { //email
                pstmt.setString(2, "%");
            }else pstmt.setString(2, DatabaseHelper.percentWrap(searchUniversity.getEmail()));
           
            if (searchUniversity.getMajors() == null || searchUniversity.getMajors().isEmpty()) { //majors
                pstmt.setString(3, "%");
            }else pstmt.setString(3, DatabaseHelper.percentWrap(searchUniversity.getMajors()));
            
            if (searchUniversity.getState() == null || searchUniversity.getState().isEmpty()) { //state
                pstmt.setString(4, "%");
            }else pstmt.setString(4, DatabaseHelper.percentWrap(searchUniversity.getState()));
            
            if (searchUniversity.getCity() == null || searchUniversity.getCity().isEmpty()) { //city
                pstmt.setString(5, "%");
            }else pstmt.setString(5, DatabaseHelper.percentWrap(searchUniversity.getCity()));
            
            if (searchUniversity.getCost() <= 0) { //cost
                pstmt.setDouble(6, 99999999);
            }else pstmt.setDouble(6, searchUniversity.getCost());           
            
            ResultSet rs = pstmt.executeQuery(); //sql look up
            
            //for each record received
            while (rs.next()) {
               
                UniversityBean university = new UniversityBean();

                university.setUniversityName(rs.getString("university_name")); //university
                university.setEmail(rs.getString("email")); //email   
                university.setPassword(rs.getString("password")); //password
                university.setVideoURL(rs.getString("video_urls")); //video
                university.setImageURL(rs.getString("images")); //images
                university.setMajors(rs.getString("major")); //major
                university.setState(rs.getString("state")); //state 
                university.setCity(rs.getString("city")); //city
                university.setCost(rs.getDouble("cost")); //cost
                university.setEssay(rs.getString("essay")); //essay
                university.setPaid(rs.getBoolean("paid")); //paid
                university.setApplyURL(rs.getString("applyURL")); //applyURL
                university.setMailList(rs.getBoolean("mail_list")); //mail_list
                
                universityBeanCollection.add(university);
            }
            
            rs.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return universityBeanCollection;
    }
    
    /**
     * 
     * @param searchUniversity
     * @return ArrayList of university records that match the search information given.
     */
    @Override
    public ArrayList<UniversityBean> searchUniversityByPaid(Boolean paid){
        ArrayList<UniversityBean> universityBeanCollection = new ArrayList<>(); 
        
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            
            String queryString = "SELECT * FROM app.university WHERE paid = ?"; //in order to grab true or false
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            
            if (paid) { //paid
                pstmt.setBoolean(1, true);
            }else pstmt.setBoolean(1, false);
            ResultSet rs = pstmt.executeQuery(); //sql look up
            
            //for each record received
            while (rs.next()) {
                UniversityBean university = new UniversityBean();

                university.setUniversityName(rs.getString("university_name")); //university
                university.setEmail(rs.getString("email")); //email        
                university.setVideoURL(rs.getString("video_urls")); //video
                university.setImageURL(rs.getString("images")); //images
                university.setMajors(rs.getString("major")); //major
                university.setState(rs.getString("state")); //state 
                university.setCity(rs.getString("city")); //city
                university.setCost(rs.getDouble("cost")); //cost
                university.setEssay(rs.getString("essay")); //essay
                university.setPaid(rs.getBoolean("paid")); //paid
                university.setMailList(rs.getBoolean("mail_list")); //mail_list
                university.setApplyURL(rs.getString("applyurl")); //applyURL
                
                universityBeanCollection.add(university);
            }
            
            rs.close();
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return universityBeanCollection;   
    } 
    
    /**
     * 
     * @param mail
     * @return ArrayList of university records that are on the mailing list.
     */
    @Override
    public ArrayList<UniversityBean> searchUniversityByMailList(Boolean mail){
        ArrayList<UniversityBean> universityBeanCollection = new ArrayList<>();        
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            
            String queryString = "SELECT * FROM app.university WHERE mail_list = ?"; //in order to grab true or false
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            
            if (mail) { //mail list
                pstmt.setBoolean(1, true);
            }else pstmt.setBoolean(1, false);
            ResultSet rs = pstmt.executeQuery(); //sql look up
            
            //for each record received
            while (rs.next()) {
                UniversityBean university = new UniversityBean();

                university.setUniversityName(rs.getString("university_name")); //university
                university.setEmail(rs.getString("email")); //email        
                university.setVideoURL(rs.getString("video_urls")); //video
                university.setImageURL(rs.getString("images")); //images
                university.setMajors(rs.getString("major")); //major
                university.setState(rs.getString("state")); //state 
                university.setCity(rs.getString("city")); //city
                university.setCost(rs.getDouble("cost")); //cost
                university.setEssay(rs.getString("essay")); //essay
                university.setPaid(rs.getBoolean("paid")); //paid
                university.setMailList(rs.getBoolean("mail_list")); //mail_list
                university.setApplyURL(rs.getString("applyurl")); //applyURL
                
                universityBeanCollection.add(university);
            }
            
            rs.close();
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return universityBeanCollection; 
    }
    
    /**
     * 
     * @param universityDAO
     * @param originalEmail
     * @return 1 for good update 0 for bad update.
     */
    @Override
    public int updateUniversity(UniversityBean universityDAO, String originalEmail) {      
        int rowCount = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "UPDATE app.university SET university_name = ?, email = ?, "
                    + "video_urls = ?, images = ?, major = ?, state = ?, city = ?, cost = ?, "
                    + "essay = ?, paid = ?, mail_list = ?, applyurl = ? WHERE email = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityDAO.getUniversityName()); //university name           
            pstmt.setString(2, universityDAO.getEmail()); //email
            //doesn't update password
            pstmt.setString(3, universityDAO.getVideoURL()); //video
            pstmt.setString(4, universityDAO.getImageURL()); //image
            pstmt.setString(5, universityDAO.getMajors()); //majors           
            pstmt.setString(6, universityDAO.getState()); //state
            pstmt.setString(7, universityDAO.getCity()); //city           
            pstmt.setDouble(8, universityDAO.getCost()); //cost
            pstmt.setString(9, universityDAO.getEssay()); //essay                                  
            pstmt.setBoolean(10, universityDAO.isPaid()); //paid
            pstmt.setBoolean(11, universityDAO.getMailList()); //mail_list 
            pstmt.setString(12, universityDAO.getApplyURL()); //applyURL
            pstmt.setString(13, originalEmail); //original email 
            
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
     * @return UniversityBean of that email. If not found then null;
     */
    @Override
    public UniversityBean getUniversityInfo(String email) {
        UniversityBean university = new UniversityBean();            
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "SELECT * FROM app.university WHERE email = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, email); //email
            ResultSet rs = pstmt.executeQuery();
            
            //set student to the values in rs
            rs.next();
            
            university.setUniversityName(rs.getString("university_name")); //university
            university.setEmail(rs.getString("email")); //email        
            university.setVideoURL(rs.getString("video_urls")); //video
            university.setImageURL(rs.getString("images")); //images
            university.setMajors(rs.getString("major")); //major
            university.setState(rs.getString("state")); //state 
            university.setCity(rs.getString("city")); //city
            university.setCost(rs.getDouble("cost")); //cost
            university.setEssay(rs.getString("essay")); //essay            
            university.setPaid(rs.getBoolean("paid")); //paid
            university.setMailList(rs.getBoolean("mail_list")); //mail_list
            university.setApplyURL(rs.getString("applyurl")); //applyURL
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }       
        return university;
    }

    /**
     * 
     * @param email
     * @param password
     * @return 1 for good update 0 for bad update.
     */
    @Override
    public int changePassword(String email, String password) {
        int rowCount = 0;            
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "UPDATE app.university SET password  = ? WHERE email = ?"; //sql statement                       
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
