/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AdminDAO;
import DAO.UniversityDAO;
import Model.AdminBean;
import Model.UniversityBean;
import java.util.ArrayList;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 *
 * @author IT353S706
 */
@ManagedBean
@SessionScoped
public class AdminController {

    private AdminBean theModel;
    private ArrayList<UniversityBean> universities;
    private boolean loggedIn = false;

    public AdminBean getTheModel() {
        return theModel;
    }

    public void setTheModel(AdminBean theModel) {
        this.theModel = theModel;
    }
    

    public String login(){
        
        
//        theModel.setEmail("admin");
//        theModel.setConfirmPassword("password");
//        theModel.setPassword("password");
//        
//        AdminDAO adm = new AdminDAO();
//        adm.createAdmin(theModel);
//        return "index.xhtml";
        
        AdminDAO adm = new AdminDAO();
        if(adm.AdminLogin(theModel.getEmail(), theModel.getPassword())){
            loggedIn = true;
            return "adminPage.xhtml";
        }
        else{
            return "admin.xhtml";
        }
    }
    
    
    public ArrayList<UniversityBean> getPaid() {
        UniversityDAO data = new UniversityDAO();
        universities = data.searchUniversityByPaid(Boolean.TRUE);
        universities.addAll(data.searchUniversityByPaid(Boolean.FALSE));
        return universities;
    }

    public void setUniversities(ArrayList<UniversityBean> universities) {
        this.universities = universities;
    }
    
    public ArrayList<UniversityBean> getUniversities(){
        return universities;
    }
    
    public String submitPaid() {
        UniversityDAO data = new UniversityDAO();
        for(UniversityBean bean: universities) {
            data.updateUniversity(bean, bean.getEmail());
        }
        
        return "adminPage.xhtml";
    }
    
    public String isLoggedIn(ComponentSystemEvent event) {
        String navi = null;

        if (!loggedIn) {

            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("index?faces-redirect=true");
        }
        
        return navi;
    }
    
    
    /**
     * Creates a new instance of newController
     */
    public AdminController() {
        theModel = new AdminBean();
    }
    
    
    public String getReport(){
        UniversityDAO uni = new UniversityDAO();
        universities = uni.getReport();
        return "adminReport.xhtml";
    }
    
    
}
