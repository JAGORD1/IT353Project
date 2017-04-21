/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.StudentDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.StudentBean;

/**
 *
 * @author admin
 */
@ManagedBean
@SessionScoped
public class StudentController {

    // This corresponds to the response to be sent back to the client
    private StudentBean theModel;

    /**
     * Creates a new instance of ProfileController
     */
    public StudentController() {
        theModel = new StudentBean();
    }

    public StudentBean getTheModel() {
        return theModel;
    }

    public void setTheModel(StudentBean theModel) {
        this.theModel = theModel;
    }

    public String createProfile() {
        StudentDAO stu = new StudentDAO();
        int a  = stu.createStudent(theModel);
        if(a == 11){
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String login(){
        StudentDAO stu = new StudentDAO();
        if(stu.studentLogin(theModel.getEmail(), theModel.getPassword())){
            theModel =stu.getStudentInfo(theModel.getEmail());
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
}
