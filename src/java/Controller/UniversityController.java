/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.StudentDAO;
import DAO.UniversityDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.UniversityBean;
import java.util.ArrayList;
import java.util.Random;
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
    private UniversityBean getModel;
    private String getEmail;
    private String resetEmail;
    private boolean loggedIn = false;
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
        data.addProfileView(getEmail);
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
        if(uniDao.createUniversity(theModel) == 1){
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String login(){
        UniversityDAO uniDao = new UniversityDAO();
        if(uniDao.universityLogin(theModel.getEmail(), theModel.getPassword())){            
            theModel = uniDao.getUniversityInfo(theModel.getEmail());
            loggedIn = true;
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
    
       
    public String universityAd(){
        randUniversity();
        UniversityDAO uni = new UniversityDAO();
        uni.addAdview(criteria.getEmail());
        return criteria.getVideoURL();
    }
    
    public String universityLink(){
        return criteria.getApplyURL();
    }
    
    private void randUniversity(){
        UniversityDAO uni = new UniversityDAO();
        universities = uni.searchUniversityByPaid(true);
        Random rand = new Random();
        int n = rand.nextInt(universities.size());
        criteria = universities.get(n);
    }
    
    
    //------------Needs upload. signUpStudent.xhtml needs upload too along with updateStudent.xhtml-----------
    public String update(){
        UniversityDAO stu = new UniversityDAO();
        if(stu.updateUniversity(theModel, resetEmail) == 1){
            return "universityPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String loggedIn() {
        String navi = null;
        
        if (!loggedIn) {           
            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("index?faces-redirect=true");
        } else resetEmail = theModel.getEmail();
        
        return navi;
    }
    
    public void getReport(){
        UniversityDAO uni = new UniversityDAO();
        universities = uni.getReport();       
    }
}
