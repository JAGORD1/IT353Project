/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.UniversityDAO;
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
    private UniversityBean getModel;
    private String getEmail;
 
    public UniversityBean getGetModel() {
        return getModel;
    }

    public void setGetModel(UniversityBean getModel) {
        this.getModel = getModel;
    }

    public String getGetEmail() {
        return getEmail;
    }

    public void setGetEmail(String getEmail) {
        this.getEmail = getEmail;
        UniversityDAO data = new UniversityDAO();
        getModel = data.getUniversityInfo(getEmail);
    }

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
        if(uniDao.createUniversity(theModel) == 11){
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String login(){
        UniversityDAO uniDao = new UniversityDAO();
        if(uniDao.universityLogin(theModel.getEmail(), theModel.getPassword())){
            uniDao.getUniversityInfo(theModel.getEmail());
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
}
