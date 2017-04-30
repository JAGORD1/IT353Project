/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.StudentDAO;
import DAO.UniversityDAO;
import Model.StudentBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.UniversityBean;
import java.util.ArrayList;

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
    private ArrayList<UniversityBean> universities;
    private UniversityBean criteria;
 
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
        criteria = new UniversityBean();
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
    
    public String searchUniversity(){
        UniversityDAO uniDao = new UniversityDAO();
        setUniversities(uniDao.searchUniversity(getCriteria()));
        return "searchUniResults.xhtml";
    }

    /**
     * @return the universities
     */
    public ArrayList<UniversityBean> getUniversities() {
        return universities;
    }

    /**
     * @param universities the universities to set
     */
    public void setUniversities(ArrayList<UniversityBean> universities) {
        this.universities = universities;
    }

    /**
     * @return the criteria
     */
    public UniversityBean getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(UniversityBean criteria) {
        this.criteria = criteria;
    }
}
