package DAO;

import Model.StudentBean;
import java.util.ArrayList;

/**
 *
 * @author Mark DiFiglio
 * 4/17/2017
 */
public interface StudentDAO_Interface {   
    public int createStudent(StudentBean studentDAO);
    public boolean checkStudentEmail(String email);      
    public boolean studentLogin(String email, String password);
    public ArrayList<StudentBean> findStudents(StudentBean searchStudent); //first name, last name, act, sat, psat, major, university
    public int updateStudent(StudentBean studentDAO, String originalEmail);
    public StudentBean getStudentInfo(String email);
    public int changePassword(String email, String password);
}
