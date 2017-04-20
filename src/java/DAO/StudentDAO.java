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
    public ArrayList<StudentBean> findStudents(StudentBean searchStudent) {
        ArrayList studentBeanCollection = new ArrayList();
        /*
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        try {
            String myDB = "jdbc:derby://localhost:1527/LinkedU";// connection string
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");            
            
            String queryString = "SELECT * FROM itkstu.student WHERE userid = ?";
            PreparedStatement pstmt = DBConn.prepareStatement(queryString);
            pstmt.setString(1, userID);
            
            ResultSet rs = pstmt.executeQuery();
            
            String firstName, lastName, email, securityQuestion, securityAnswer;
            StudentBean studentBean;

            while (rs.next()) {
                // 1. if a float (say PRICE) is to be retrieved, use rs.getFloat("PRICE");
                // 2. Instead of using column firstName, can alternatively use: rs.getString(1); // not 0
                firstName = rs.getString("first_Name");
                lastName = rs.getString("last_Name");
                email = rs.getString("email");
                securityQuestion = rs.getString("security_Question");
                securityAnswer = rs.getString("security_Answer");               

                // make a ProfileBean object out of the values
                studentBean = new studentBean(firstName, lastName, userID, "", "", email, securityQuestion, securityAnswer);
                // add the newly created object to the collection
                studentBeanCollection.add(studentBean);
            }
            rs.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        */
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
                    + "video = ?, highschool = ?, phone_number = ?, phone_carrier = ?, images = ?, password  = ? "
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
                   queryString = "DELTE FROM app.mail_list WHERE email = ?";
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
            student.setFirstName(rs.getString("first_name")); //first name
            student.setLastName(rs.getString("last_name")); //last name        
            student.setACTScore(rs.getInt("act")); //act
            student.setSATScore(rs.getInt("sat")); //sat
            student.setPSATScore(rs.getInt("psat_nmsqt")); //psat
            student.setEssay(rs.getString("essay")); //essay 
            student.setEmail(rs.getString("email")); //email
            student.setUniversities(rs.getString("universities")); //universities
            student.setMajors(rs.getString("majors")); //majors
            student.setMixtapeURL(rs.getString("video")); //video
            student.setHighSchool(rs.getString("highschool")); //highschool
            student.setPhoneNumber("phone_number"); //phone number
            student.setImageURL("images"); //images
            student.setProvider("phone_carrier"); //phone provider
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
}
