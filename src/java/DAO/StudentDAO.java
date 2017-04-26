package DAO;

import Model.StudentBean;
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
public class StudentDAO implements StudentDAO_Interface{

    /**
     * 
     * @param studentDAO
     * @return 00 or 01 or 10 or 11 as an int.
     * If the first digit is zero then failed to create student database.
     * If second digit is zero then failed to add to mail_list database.
     * If 11 then its a good add.
     */
    @Override
    public int createStudent(StudentBean studentDAO) {
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
            String salt = Hasher.generateHash(studentDAO.getPassword()); //hash the password             
            String queryString = "INSERT INTO app.student VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            rowCount = 10 * pstmt.executeUpdate(); //times 10 to keep a binary track of what successfully add to database
            
            if (studentDAO.getMailList()){ //if mailing list is checked then add to mailing list
                queryString = "INSERT INTO app.mail_list VALUES (?)";
                pstmt = DBConn.prepareStatement(queryString);
                pstmt.setString(1, studentDAO.getEmail());
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
     * Checks to database if the given student email is in the database.
     * @param email
     * @return Returns true if it already exists.
     */
    @Override
    public boolean checkStudentEmail(String email){
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");            
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
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
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
     * @param searchInfo
     * @return ArrayList of student records that match the search information given.
     */
    @Override
    public ArrayList<StudentBean> findStudents(StudentBean searchStudent) { //first name, last name, act, sat, psat, major, university
        ArrayList studentBeanCollection = new ArrayList();
        
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
            String queryString = "SELECT * FROM app.student WHERE email LIKE ?, first_name LIKE ?, last_name LIKE ?, "
                    + "act_score >= ?, sat_score >= ?, psat_nmsqt >= ?, universities LIKE ?, majors LIKE ?, highschool LIKE ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            
            if (searchStudent.getEmail().isEmpty()) { //email
                pstmt.setString(1, "%");
            }else pstmt.setString(1, percentWrap(searchStudent.getEmail()));
            
            if (searchStudent.getFirstName().isEmpty()) { //first_name
                pstmt.setString(2, "%");
            }else pstmt.setString(2, percentWrap(searchStudent.getFirstName()));
            
            if (searchStudent.getLastName().isEmpty()) { //last_name
                pstmt.setString(3, "%");
            }else pstmt.setString(3, percentWrap(searchStudent.getLastName()));
            
            if (searchStudent.getACTScore() == -1) { //act_score
                pstmt.setInt(4, 0);
            }else pstmt.setInt(4, searchStudent.getACTScore());
            
            if (searchStudent.getSATScore() == -1) { //sat_score
                pstmt.setInt(5, 0);
            }else pstmt.setInt(5, searchStudent.getSATScore());
            
            if (searchStudent.getPSATScore() == -1) { //psat_nmsqt
                pstmt.setInt(6, 0);
            }else pstmt.setInt(6, searchStudent.getPSATScore());
            
            if (searchStudent.getUniversities().isEmpty()) { //universities
                pstmt.setString(7, "%");
            }else pstmt.setString(7, percentWrap(searchStudent.getUniversities()));
            
            if (searchStudent.getMajors().isEmpty()) { //majors
                pstmt.setString(8, "%");
            }else pstmt.setString(8, percentWrap(searchStudent.getMajors()));
            
            if (searchStudent.getHighSchool().isEmpty()) { //highschool
                pstmt.setString(9, "%");
            }else pstmt.setString(9, percentWrap(searchStudent.getHighSchool()));
                      
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
     * @return Int of rows updated. 
     */
    @Override
    public int updateStudent(StudentBean studentDAO, String originalEmail) {
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
            String queryString = "UPDATE app.student SET email = ?, first_name = ?, last_name = ?, "
                    + "act_score = ?, sat_score = ?, psat_nmsqt = ?, essay = ?, universities = ?, majors = ?, "
                    + "video = ?, highschool = ?, phone_number = ?, phone_carrier = ?, images = ? "
                    + "WHERE email = ?";
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
            pstmt.setString(15, originalEmail); //original email            
            rowCount = 10 * pstmt.executeUpdate(); //times 10 to keep a binary track of what successfully add to database                       
            
            //checks if email is in mail_list
            queryString = "SELECT email FROM app.mail_list WHERE email = ?";                
            pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, studentDAO.getEmail());
            ResultSet rs = pstmt.executeQuery();
            
            if (studentDAO.getMailList()){ //if mailing list is checked then add to mail_list               
                if (!rs.next()) { //email isn't in list already so add to mail_list
                    queryString = "INSERT INTO app.mail_list VALUES (?)";
                    pstmt = DBConn.prepareStatement(queryString);
                    pstmt.setString(1, studentDAO.getEmail());
                    rowCount += pstmt.executeUpdate();          
                } else rowCount++; //email is in the list then don't add                         
            } else { //don't add to mail_list or remove from mail_list
               if (rs.next()) { //delete record from mail_list
                   queryString = "DELETE FROM app.mail_list WHERE email = ?";
                   pstmt = DBConn.prepareStatement(queryString);
                   pstmt.setString(1, studentDAO.getEmail());
                   rowCount += pstmt.executeUpdate();
               } else rowCount++;
            }            
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
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }       
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
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
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }       
        try {
            String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_spring2017_LinkedU";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
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
    
    //wraps a string in % signs for database access
    private String percentWrap(String word){       
        return "%" + word + "%";
    }
}
