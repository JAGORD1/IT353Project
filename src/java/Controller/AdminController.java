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
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 *
 * @author IT353S706
 */
@ManagedBean
@RequestScoped
public class AdminController {

    private AdminBean theModel;
    private ArrayList<UniversityBean> paid;
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
            return "markPaid.xhtml";
        }
        else{
            return "admin.xhtml";
        }
    }
    
    
    public ArrayList<UniversityBean> getPaid() {
        UniversityDAO data = new UniversityDAO();
        paid = data.searchUniversityByPaid(Boolean.TRUE);
        paid.addAll(data.searchUniversityByPaid(Boolean.FALSE));
        return paid;
    }

    public void setPaid(ArrayList<UniversityBean> paid) {
        this.paid = paid;
    }
    
    public String submitPaid() {
        UniversityDAO data = new UniversityDAO();
        for(UniversityBean bean: paid) {
            data.updateUniversity(bean, bean.getEmail());
        }
        
        return "index.xhtml";
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
    
}
