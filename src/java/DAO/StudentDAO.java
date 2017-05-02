package DAO;

import Model.StudentBean;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import static java.sql.JDBCType.BLOB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BLOB;
import java.util.ArrayList;

/**
 *
 * @author Mark DiFiglio
 * 4/17/2017
 * 
 * Controls access to the student table.
 */
public class StudentDAO implements StudentDAO_Interface{

    /**
     * 
     * @param studentDAO
     * @return 1 is good insert otherwise 0.
     * 
     */
    @Override
    public int createStudent(StudentBean studentDAO) {
        int rowCount = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();          
            String salt = Hasher.generateHash(studentDAO.getPassword()); //hash the password             
            String queryString = "INSERT INTO app.student VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, studentDAO.getEmail()); //email
            pstmt.setString(2, studentDAO.getFirstName()); //first_name
            pstmt.setString(3, studentDAO.getLastName()); //last_name
            pstmt.setInt(4, studentDAO.getACTScore()); //act_score
            pstmt.setInt(5, studentDAO.getSATScore()); //sat_score
            pstmt.setInt(6, studentDAO.getPSATScore()); //psat_nmsqt
            pstmt.setString(7, studentDAO.getEssay()); //essay
            pstmt.setString(8, studentDAO.getUniversities()); //universities
            pstmt.setString(9, studentDAO.getMajors()); //majors
            pstmt.setString(10, studentDAO.getMixtapeURL()); //mixtapes
            pstmt.setString(11, studentDAO.getHighSchool()); //highschool
            pstmt.setString(12, studentDAO.getPhoneNumber()); //phone_number
            pstmt.setString(13, studentDAO.getProvider()); //phone_carrier
            pstmt.setString(14, studentDAO.getImageURL()); //images
            pstmt.setString(15, salt); //password   
            pstmt.setBoolean(16, studentDAO.getMailList()); //mail_list   
            
            rowCount = pstmt.executeUpdate(); 
  
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return rowCount;
    }
    
    /**
     * Checks to database if the given student email is in the database.
     * @param email
     * @return Returns true if it already exists.
     */
    @Override
    public boolean checkStudentEmail(String email){       
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();          
            String queryString = "SELECT email FROM app.student WHERE email = ?"; //sql statement
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
     * @return True if good login otherwise false.
     */
    @Override
    public boolean studentLogin(String email, String password){
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String salt = Hasher.generateHash(password); //hash the password               
            String queryString = "SELECT email, password FROM app.student WHERE email = ? and password = '" + salt + "'"; //sql statement
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
     * @param searchStudent
     * @return ArrayList of student records that match the search information given.
     */
    @Override
    public ArrayList<StudentBean> searchStudent(StudentBean searchStudent) { //first name, last name, act, sat, psat, major, university
        ArrayList<StudentBean> studentBeanCollection = new ArrayList<>();     
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();            
            
            //get search records
            String queryString = "SELECT * FROM app.student WHERE email LIKE ? AND first_name LIKE ? AND last_name LIKE ? "
                    + "AND act_score >= ? AND sat_score >= ? AND psat_nmsqt >= ? AND universities LIKE ? AND majors LIKE ? "
                    + "AND highschool LIKE ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            
            if (searchStudent.getEmail() == null || searchStudent.getEmail().isEmpty()) { //email
                pstmt.setString(1, "%");
            }else pstmt.setString(1, DatabaseHelper.percentWrap(searchStudent.getEmail()));
            
            if (searchStudent.getFirstName() == null || searchStudent.getFirstName().isEmpty()) { //first_name
                pstmt.setString(2, "%");
            }else pstmt.setString(2, DatabaseHelper.percentWrap(searchStudent.getFirstName()));
            
            if (searchStudent.getLastName() == null || searchStudent.getLastName().isEmpty()) { //last_name
                pstmt.setString(3, "%");
            }else pstmt.setString(3, DatabaseHelper.percentWrap(searchStudent.getLastName()));
            
            if (searchStudent.getACTScore() <= 0) { //act_score
                pstmt.setInt(4, 0);
            }else pstmt.setInt(4, searchStudent.getACTScore());
            
            if (searchStudent.getSATScore() <= 0) { //sat_score
                pstmt.setInt(5, 0);
            }else pstmt.setInt(5, searchStudent.getSATScore());
            
            if (searchStudent.getPSATScore() <= 0) { //psat_nmsqt
                pstmt.setInt(6, 0);
            }else pstmt.setInt(6, searchStudent.getPSATScore());
            
            if (searchStudent.getUniversities() == null || searchStudent.getUniversities().isEmpty()) { //universities
                pstmt.setString(7, "%");
            }else pstmt.setString(7, DatabaseHelper.percentWrap(searchStudent.getUniversities()));
            
            if (searchStudent.getMajors() == null || searchStudent.getMajors().isEmpty()) { //majors
                pstmt.setString(8, "%");
            }else pstmt.setString(8, DatabaseHelper.percentWrap(searchStudent.getMajors()));
            
            if (searchStudent.getHighSchool() == null || searchStudent.getHighSchool().isEmpty()) { //highschool
                pstmt.setString(9, "%");
            }else pstmt.setString(9, DatabaseHelper.percentWrap(searchStudent.getHighSchool()));
                      
            ResultSet rs = pstmt.executeQuery(); //sql look up
            
            //for each record received
            while (rs.next()) {
                StudentBean student = new StudentBean();

                student.setFirstName(rs.getString("first_name")); //first name
                student.setLastName(rs.getString("last_name")); //last name        
                student.setACTScore(rs.getInt("act_score")); //aSct
                student.setSATScore(rs.getInt("sat_score")); //sat
                student.setPSATScore(rs.getInt("psat_nmsqt")); //psat
                student.setEssay(rs.getString("essay")); //essay 
                student.setEmail(rs.getString("email")); //email
                student.setUniversities(rs.getString("universities")); //universities
                student.setMajors(rs.getString("majors")); //majors
                student.setMixtapeURL(rs.getString("video")); //video
                student.setHighSchool(rs.getString("highschool")); //highschool
                student.setPhoneNumber(rs.getString("phone_number")); //phone number
                student.setImageURL(rs.getString("images")); //images
                student.setProvider(rs.getString("phone_carrier")); //phone provider
                student.setPassword(""); //password to null
                student.setMailList(rs.getBoolean("mail_list")); //mail_list
                
                studentBeanCollection.add(student);
            }
            
            rs.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return studentBeanCollection;
    }

    /**
     * 
     * @param mail
     * @return 
     */
    @Override
    public ArrayList<StudentBean> searchStudentByMailList(Boolean mail){
        ArrayList<StudentBean> studentBeanCollection = new ArrayList<>();        
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
                StudentBean student = new StudentBean();

                student.setFirstName(rs.getString("first_name")); //first name
                student.setLastName(rs.getString("last_name")); //last name        
                student.setACTScore(rs.getInt("act_score")); //aSct
                student.setSATScore(rs.getInt("sat_score")); //sat
                student.setPSATScore(rs.getInt("psat_nmsqt")); //psat
                student.setEssay(rs.getString("essay")); //essay 
                student.setEmail(rs.getString("email")); //email
                student.setUniversities(rs.getString("universities")); //universities
                student.setMajors(rs.getString("majors")); //majors
                student.setMixtapeURL(rs.getString("video")); //video
                student.setHighSchool(rs.getString("highschool")); //highschool
                student.setPhoneNumber(rs.getString("phone_number")); //phone number
                student.setImageURL(rs.getString("images")); //images
                student.setProvider(rs.getString("phone_carrier")); //phone provider
                student.setPassword(""); //password to null
                student.setMailList(rs.getBoolean("mail_list")); //mail_list
                
                studentBeanCollection.add(student);
            }
            
            rs.close();
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return studentBeanCollection;
    }
     
    
    /**
     * 
     * @param studentDAO
     * @param originalEmail
     * @return Int of rows updated. 
     */
    @Override
    public int updateStudent(StudentBean studentDAO, String originalEmail) {
        int rowCount = 0;
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "UPDATE app.student SET email = ?, first_name = ?, last_name = ?, "
                    + "act_score = ?, sat_score = ?, psat_nmsqt = ?, essay = ?, universities = ?, majors = ?, "
                    + "video = ?, highschool = ?, phone_number = ?, phone_carrier = ?, images = ?, "
                    + "mail_list = ? WHERE email = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, studentDAO.getEmail()); //email
            pstmt.setString(2, studentDAO.getFirstName()); //first_name
            pstmt.setString(3, studentDAO.getLastName()); //last_name
            pstmt.setInt(4, studentDAO.getACTScore()); //act_score
            pstmt.setInt(5, studentDAO.getSATScore()); //sat_score
            pstmt.setInt(6, studentDAO.getPSATScore()); //psat_nmsqt
            pstmt.setString(7, studentDAO.getEssay()); //essay
            pstmt.setString(8, studentDAO.getUniversities()); //universities
            pstmt.setString(9, studentDAO.getMajors()); //majors
            pstmt.setString(10, studentDAO.getMixtapeURL()); //mixtapes
            pstmt.setString(11, studentDAO.getHighSchool()); //highschool
            pstmt.setString(12, studentDAO.getPhoneNumber()); //phone_number
            pstmt.setString(13, studentDAO.getProvider()); //phone_carrier
            pstmt.setString(14, studentDAO.getImageURL()); //images
            //doesn't update password
            pstmt.setBoolean(15, studentDAO.getMailList()); //mail_list 
            pstmt.setString(16, originalEmail); //original email
            
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
     * @return Student.
     */
    @Override
    public StudentBean getStudentInfo(String email){
        StudentBean student = new StudentBean();          
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "SELECT * FROM app.student WHERE email = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, email); //email
            ResultSet rs = pstmt.executeQuery();                                                        
            //set student to the values in rs
            rs.next();
            student.setFirstName(rs.getString("first_name")); //first name
            student.setLastName(rs.getString("last_name")); //last name        
            student.setACTScore(rs.getInt("act_score")); //aSct
            student.setSATScore(rs.getInt("sat_score")); //sat
            student.setPSATScore(rs.getInt("psat_nmsqt")); //psat
            student.setEssay(rs.getString("essay")); //essay 
            student.setEmail(rs.getString("email")); //email
            student.setUniversities(rs.getString("universities")); //universities
            student.setMajors(rs.getString("majors")); //majors
            student.setMixtapeURL(rs.getString("video")); //video
            student.setHighSchool(rs.getString("highschool")); //highschool
            student.setPhoneNumber(rs.getString("phone_number")); //phone number
            student.setImageURL(rs.getString("images")); //images
            student.setProvider(rs.getString("phone_carrier")); //phone provider
            student.setPassword(""); //password to null
            student.setMailList(rs.getBoolean("mail_list")); //mail_list
            
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }       
        return student;   
    }

    /**
     * 
     * @param password
     * @return True if change was successful.
     */
    @Override
    public int changePassword(String email, String password) {
        int rowCount = 0;           
        try {
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "UPDATE app.student SET password  = ? WHERE email = ?"; //sql statement                       
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
    
    public int addImage(String id, Blob img){
        int rowCount = 0;           
        try {           
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "INSERT INTO app.images VALUES (?,utl_raw.cast_to_raw(?))"; //sql statement          
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, id); //password
            pstmt.setBlob(2, img); //email            
            rowCount = pstmt.executeUpdate(); //database call
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return rowCount; 
    }
    
    public Blob getImage(String id){ 
        Blob img = null;
        try {           
            Connection DBConn = DatabaseHelper.dataBaseConnection();
            String queryString = "SELECT * FROM app.images WHERE id = ?"; //sql statement          
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, id); //id         
            ResultSet rs = pstmt.executeQuery(); //database call
            if (rs.next()){
                img = rs.getBlob("image");
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return img; 
    }
}
