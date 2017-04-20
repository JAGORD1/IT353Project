/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.StudentDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.StudentBean;
import Model.UniversityBean;

/**
 *
 * @author admin
 */
@ManagedBean
@SessionScoped
public class StudentController {

    // This corresponds to the response to be sent back to the client
    private StudentBean theModel;
    private UniversityBean searchModel; //add getters and setters for this

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
        StudentDAO stuDao = new StudentDAO();
        int rtrn = stuDao.createStudent(theModel);
        if(rtrn > 0){
            return "TODO";
        }
        else{
            return "TODO";
        }
        //TODO Check for email in this method or in a different?
    }
    
    /*public void checkEmail(){
        StudentDAO stuDao = new StudentDAO();
        S
    }*/
    
    public String logIn(){
        StudentDAO stuDao = new StudentDAO();
        if(stuDao.studentLogin(theModel.getEmail(), theModel.getPassword())){
            return "TODO";
        }
        else{
            return "TODO";
        }
    }
    
    public String searchUniversity(){
        //Code to access database
        //Returns a xhtml file that will use the database
        return "TODO";
    }
}
