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
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
    private String scheduleMessage;
    private String studentEmail;
 
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
   
       public void sendEmail(String destination ,String sendMessage) {
        // Recipient's email ID needs to be mentioned.
        String to = destination;

        // Sender's email ID needs to be mentioned
        String from = "ejwunde@ilstu.edu";
        
        // Assuming you are sending email from this host
        String host = "outlook.office365.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "587");
        // Get the default Session object.
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ejwunde@ilstu.edu", "Cr@ck3rj@ck5");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("LinkedU Email");

            // Send the actual HTML message, as big as you like
            message.setContent(sendMessage,
                    "text/html");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public String scheduleAppt(){
        String appt = "";
        appt = getScheduleMessage();
        
        sendEmail(getModel.getEmail(), appt);
        sendEmail(studentEmail,appt);
        
        return "getUniversityPage.xhtml";
    }
    
    public void requestInformation(){
        String message = "A user has requested that you update or include more information on your profile.";
    
        sendEmail(getModel.getEmail(), message);
    }
    
    public void reportPage(){
        String message = "The following user\'s profile page has been flagged as being offensive."
                + "Please review.\n User: " + getModel.getUniversityName();  
        
        sendEmail("ejwunde@ilstu.edu", message);
    }

    /**
     * @return the scheduleMessage
     */
    public String getScheduleMessage() {
        return scheduleMessage;
    }

    /**
     * @param scheduleMessage the scheduleMessage to set
     */
    public void setScheduleMessage(String scheduleMessage) {
        this.scheduleMessage = scheduleMessage;
    }

    /**
     * @return the studentEmail
     */
    public String getStudentEmail() {
        return studentEmail;
    }

    /**
     * @param studentEmail the studentEmail to set
     */
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}
