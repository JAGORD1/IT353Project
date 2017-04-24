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
            String queryString = "INSERT INTO app.university VALUES (?,?,?,?,?,?,?,?,?,?)"; //sql statement
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
        ArrayList<UniversityBean> universityBeanCollection = new ArrayList<UniversityBean>();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");            
            
            //get search records
            String queryString = "SELECT * FROM app.university WHERE name LIKE ?, email LIKE ?, "
                    + "major LIKE ?, state LIKE ?, city LIKE ?, cost <= ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            
            if (searchUniversity.getUniversityName().isEmpty()) { //university_name
                pstmt.setString(1, "%");
            }else pstmt.setString(1, percentWrap(searchUniversity.getUniversityName()));
            
            if (searchUniversity.getEmail().isEmpty()) { //email
                pstmt.setString(2, "%");
            }else pstmt.setString(2, percentWrap(searchUniversity.getEmail()));
           
            if (searchUniversity.getMajors().isEmpty()) { //majors
                pstmt.setString(3, "%");
            }else pstmt.setString(3, percentWrap(searchUniversity.getMajors()));
            
            if (searchUniversity.getState().isEmpty()) { //state
                pstmt.setString(4, "%");
            }else pstmt.setString(4, percentWrap(searchUniversity.getState()));
            
            if (searchUniversity.getCity().isEmpty()) { //city
                pstmt.setString(5, "%");
            }else pstmt.setString(5, percentWrap(searchUniversity.getCity()));
            
            if (searchUniversity.getCost() == -1) { //cost
                pstmt.setDouble(6, 99999999);
            }else pstmt.setDouble(6, searchUniversity.getCost());
            
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
                
                universityBeanCollection.add(university);
            }
            
            rs.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return universityBeanCollection;
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
                    + "video_urls = ?, images = ?, major = ?, state = ?, city = ?, cost = ?, "
                    + "essay = ?, WHERE university_name = ?";
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
            pstmt.setString(10, originalUniversityName); //original university name            
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
                   queryString = "DELETE FROM app.mail_list WHERE email = ?";
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
        UniversityBean university = new UniversityBean();
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
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }       
        return university;
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
    
    //wraps a string in % signs for database access
    private String percentWrap(String word){       
        return "%" + word + "%";
    }
}
