/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.UniversityDAO;
import Model.StudentBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.UniversityBean;

/**
 *
 * @author admin
 */
@ManagedBean
@SessionScoped
public class UniversityController {

    // This corresponds to the response to be sent back to the client
    private UniversityBean theModel;
    private StudentBean searchModel; //Add getters and setters

    /**
     * Creates a new instance of ProfileController
     */
    public UniversityController() {
        theModel = new UniversityBean();
    }

    public UniversityBean getTheModel() {
        return theModel;
    }

    public void setTheModel(UniversityBean theModel) {
        this.theModel = theModel;
    }

    
    public String createProfile() {
        UniversityDAO uniDao = new UniversityDAO();
        
        int rtrn = uniDao.createUniversity(theModel);
        if(rtrn > 0){
            return "TODO";
        }
        else{
            return "TODO";
        }
        //TODO Check for email (or university name) in this method or in a different?
    }
    
    public String searchStudent(){
        //Code to access database
        //Returns a xhtml file that will use the database
        return "TODO";
    }
}
