/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.UniversityDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.UniversityBean;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

/**
 *
 * @author admin
 */
@ManagedBean
@SessionScoped
public class UniversityController {

    // This corresponds to the response to be sent back to the client
    private UniversityBean theModel;
    private boolean loggedIn = false;
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
            loggedIn = true;
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String login(){
        UniversityDAO uniDao = new UniversityDAO();
        if(uniDao.universityLogin(theModel.getUniversityName(), theModel.getPassword())){
            theModel = uniDao.getUniversityInfo(theModel.getUniversityName());
            loggedIn = true;
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String update(){
        UniversityDAO uniDao = new UniversityDAO();
        if(uniDao.updateUniversity(theModel, theModel.getUniversityName()) == 11){
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String isLoggedIn(){
        String navi = null;

        if (!loggedIn) {

            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("index?faces-redirect=true");
        }
        
        return navi;
    }
}
