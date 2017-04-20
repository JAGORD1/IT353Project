package DAO;

import Model.UniversityBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Mark DiFiglio
 * 4/17/2017
 */
public class UniversityDAO implements UniversityDAO_Interface{
    /**
     * 
     * @param universityDAO
     * @return 00 or 01 or 10 or 11 as an int.
     * If the first digit is zero then failed to create student database.
     * If second digit is zero then failed to add to mail_list database.
     * If 11 then its a good add.
     */
    @Override
    public int createUniversity(UniversityBean universityDAO) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int rowCount = 0;
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");            
            String salt = Hasher.generateHash(universityDAO.getPassword()); //hash the password             
            String queryString = "INSERT INTO app.university VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //sql statement
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityDAO.getUniversityName()); //university name
            pstmt.setString(2, universityDAO.getEmail()); //email
            pstmt.setString(3, salt); //password
            pstmt.setString(4, universityDAO.getVideoURL()); //video
            pstmt.setString(5, universityDAO.getImageURL()); //images
            pstmt.setString(6, universityDAO.getMajors()); //majors
            pstmt.setString(7, universityDAO.getState()); //state
            pstmt.setString(8, universityDAO.getCity()); //city
            pstmt.setDouble(9, universityDAO.getCost()); //cost
            pstmt.setString(10, universityDAO.getEssay()); //essay          
            rowCount = 10 * pstmt.executeUpdate(); //times 10 to keep a binary track of what successfully add to database            
            if (universityDAO.getMailList()){ //if mailing list is checked then add to mailing list
                queryString = "INSERT INTO app.mail_list VALUES (?)";
                pstmt = DBConn.prepareStatement(queryString);
                pstmt.setString(1, universityDAO.getEmail());
                rowCount += pstmt.executeUpdate();           
            } else rowCount += 1;        
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        //00 - 01 - 10 - 11
        //if the first digit is zero then failed to create student database
        //if second digit is zero then failed to add to mail_list database
        //11 is a good add
        return rowCount;    
    }
    
     /**
     * Checks to database if the given university name is in the database.
     * @param universityName
     * @return Returns true if it already exists.
     */
    @Override
    public boolean checkUniversityName(String universityName) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");            
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
     * @param universityName
     * @param password
     * @return True if login information is good otherwise false.
     */
    @Override
    public boolean universityLogin(String universityName, String password) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            String salt = Hasher.generateHash(password); //hash the password               
            String queryString = "SELECT university_name, password FROM app.university WHERE university_name = ? and password = '" + salt + "'"; //sql statement
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

    @Override
    public ArrayList<UniversityBean> searchUniversity(UniversityBean searchUniversity) {
        ArrayList<UniversityBean> universities = new ArrayList<UniversityBean>();
        
        
        return universities;
    }
    
    @Override
    public int updateUniversity(UniversityBean universityDAO, String originalUniversityName) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int rowCount = 0;
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            String queryString = "UPDATE app.university SET university_name = ?, email = ?, "
                    + "video = ?, images = ?, major = ?, state = ?, city = ?, cost = ?, "
                    + "essay = ?, WHERE university_name = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityDAO.getUniversityName()); //university name           
            pstmt.setString(2, universityDAO.getEmail()); //email
            //doesn't update password
            pstmt.setString(4, universityDAO.getVideoURL()); //video
            pstmt.setString(5, universityDAO.getImageURL()); //image
            pstmt.setString(6, universityDAO.getMajors()); //majors           
            pstmt.setString(7, universityDAO.getState()); //state
            pstmt.setString(8, universityDAO.getCity()); //city           
            pstmt.setDouble(9, universityDAO.getCost()); //cost
            pstmt.setString(10, universityDAO.getEssay()); //essay                       
            pstmt.setString(11, originalUniversityName); //original university name            
            rowCount = 10 * pstmt.executeUpdate(); //times 10 to keep a binary track of what successfully add to database                       
            
            //checks if email is in mail_list
            queryString = "SELECT email FROM app.mail_list WHERE email = ?";                
            pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityDAO.getEmail());
            ResultSet rs = pstmt.executeQuery();
            
            if (universityDAO.getMailList()){ //if mailing list is checked then add to mail_list               
                if (!rs.next()) { //email isn't in list already so add to mail_list
                    queryString = "INSERT INTO app.mail_list VALUES (?)";
                    pstmt = DBConn.prepareStatement(queryString);
                    pstmt.setString(1, universityDAO.getEmail());
                    rowCount += pstmt.executeUpdate();          
                } else rowCount++; //email is in the list then don't add                         
            } else { //don't add to mail_list or remove from mail_list
               if (rs.next()) { //delete record from mail_list
                   queryString = "DELTE FROM app.mail_list WHERE email = ?";
                   pstmt = DBConn.prepareStatement(queryString);
                   pstmt.setString(1, universityDAO.getEmail());
                   rowCount += pstmt.executeUpdate();
               } else rowCount++;
            }            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return rowCount;    
    }

    @Override
    public UniversityBean getUniversityInfo(String universityName) {
        UniversityBean student = new UniversityBean();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }       
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            String queryString = "SELECT * FROM app.university WHERE email = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, universityName); //email
            ResultSet rs = pstmt.executeQuery();                                                        
            //set student to the values in rs
            student.setUniversityName(rs.getString("university_name")); //university
            student.setEmail(rs.getString("email")); //email        
            student.setVideoURL(rs.getString("video_urls")); //video
            student.setImageURL(rs.getString("images")); //images
            student.setMajors(rs.getString("major")); //major
            student.setState(rs.getString("state")); //state 
            student.setCity(rs.getString("city")); //city
            student.setCost(rs.getDouble("cost")); //cost
            student.setEssay(rs.getString("essay")); //essay
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }       
        return student;
    }

    @Override
    public int changePassword(String universityName, String password) {
        int rowCount = 0;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }       
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            String queryString = "UPDATE app.university SET password  = ? WHERE university_name = ?"; //sql statement                       
            String salt = Hasher.generateHash(password); //hash the password            
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, salt); //password
            pstmt.setString(2, universityName); //email            
            rowCount = pstmt.executeUpdate(); //database call
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return rowCount;  
    }
    
}
