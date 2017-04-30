/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.StudentDAO;
import DAO.TokensDAO;
import DAO.UniversityDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.StudentBean;
import Model.UniversityBean;
import edu.ilstu.it.TextSenderService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author admin
 */
@ManagedBean
@SessionScoped
public class StudentController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/gfish2.it.ilstu.edu/ISUTextSMS2/TextSenderService.wsdl")
    private TextSenderService service;

    // This corresponds to the response to be sent back to the client
    private StudentBean theModel;
    private StudentBean getModel;
    private String resetEmail;
    private boolean sendSMS;
    private boolean loggedIn = false;
    private String token;
    private String getEmail;
    private UniversityBean university;

    public StudentBean getGetModel() {
        return getModel;
    }

    public void setGetModel(StudentBean getModel) {
        this.getModel = getModel;
    }

    public String getGetEmail() {
        return getEmail;
    }

    public void setGetEmail(String getEmail) {
        this.getEmail = getEmail;
        StudentDAO data = new StudentDAO();
        getModel = data.getStudentInfo(getEmail);
    }

    public TextSenderService getService() {
        return service;
    }

    public void setService(TextSenderService service) {
        this.service = service;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(boolean sendSMS) {
        this.sendSMS = sendSMS;
    }

    public String getResetEmail() {
        return resetEmail;
    }

    public void setResetEmail(String resetEmail) {
        this.resetEmail = resetEmail;
    }

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
            loggedIn = true;
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String resetPassword() {
        
        StudentDAO data = new StudentDAO();
        
        StudentBean temp = data.getStudentInfo(resetEmail);
       
        TokensDAO dao = new TokensDAO();
       
        String newToken = dao.submitToken(resetEmail);
        
        String url = "http://gfish2.it.ilstu.edu/caferg2_spring2017_LinkedU/faces/changePassword.xhtml?token=" + newToken;
        
        if (temp != null) {
               sendEmail(temp.getEmail(), url);
               if (sendSMS) sendSMS(temp.getProvider(), temp.getPhoneNumber(), url);
               return "resetSent.xhtml";
        }
        else return "resetFailed.xhtml";
    }
    
    public void sendEmail(String destination ,String sendMessage) {
        // Recipient's email ID needs to be mentioned.
        String to = destination;

        // Sender's email ID needs to be mentioned
        String from = "";
        
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
                return new PasswordAuthentication("", "");
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
            message.setSubject("LinkedU Password Reset");

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

    private void sendSMS(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        edu.ilstu.it.TextSender port = service.getTextSenderPort();
        port.sendSMS(arg0, arg1, arg2);
    }
    
    public String login(){
        StudentDAO stu = new StudentDAO();
        if(stu.studentLogin(theModel.getEmail(), theModel.getPassword())){
            theModel =stu.getStudentInfo(theModel.getEmail());
            loggedIn = true;
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    
    //------------Needs upload. signUpStudent.xhtml needs upload too along with updateStudent.xhtml-----------
    public String update(){
        StudentDAO stu = new StudentDAO();
        if(stu.updateStudent(theModel, theModel.getEmail()) == 11){
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
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
    
    public String checkToken(ComponentSystemEvent event) {
        String navi = null;
        
        TokensDAO dao = new TokensDAO();
        
        String tokenEmail = dao.verifyToken(token);

        if (tokenEmail.equals("")) {

            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("index?faces-redirect=true");
        }
        else {
            theModel.setEmail(tokenEmail);
        }
        
        return navi;
    }
    
    public String changePassword() {
        if (theModel.getPassword().equals(theModel.getPassword2())) {
            StudentDAO stu = new StudentDAO();
            stu.changePassword(theModel.getEmail(), theModel.getPassword());
            return "Password Changed";
        }
        else return "Passwords Do Not Match";
    }
    
    //search*
    public ArrayList<StudentBean> searchStudent(StudentBean searchInfo){
        StudentDAO student = new StudentDAO();
        return student.searchStudent(searchInfo);
    }
    
    public void checkStudent(javax.faces.event.AjaxBehaviorEvent event){
        StudentDAO student = new StudentDAO();               
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(String.valueOf(student.searchStudent(theModel).size())));             
    }
    
   public void addImage() throws FileNotFoundException, IOException, MessagingException{ 
        Part image = theModel.getImage();
        if (image == null) return;
        //System.out.println(image.length());
        StudentDAO images = new StudentDAO();               
        byte data[] = new byte[(int) image.getSize()];
        FileOutputStream out = new FileOutputStream("a");
        out.write(data);
        out.close();
    }
   
    /*public void upload() {
     try {
       theModel.setFileContent(new Scanner(file.getInputStream()));
           .useDelimiter("\\A").next();
     } catch (IOException e) {
       // Error handling
     }
    }*/
   
    public String getImage(String name) throws FileNotFoundException, IOException{
         StudentDAO images = new StudentDAO();               
         
         Path path = Paths.get("a");
         //byte data[] = Files.readAllBytes(path);
         
         
         return "a";
     }
    
    public void validateFile(FacesContext ctx, UIComponent comp, Object value) throws MessagingException {
        List<FacesMessage> msgs = new ArrayList<FacesMessage>();
        Part file = (Part)value;
        if (file.getSize() > 2097152) { //2 MB
          msgs.add(new FacesMessage("file too big"));
        }
        if ("text/plain".equals(file.getContentType())) {
          msgs.add(new FacesMessage("not a picture"));
        }
        if (!msgs.isEmpty()) {
          throw new ValidatorException(msgs);
        }
    }
    
    private void setUniversity(){
        UniversityDAO uni = new UniversityDAO();
        ArrayList<UniversityBean> list = uni.searchUniversityByPaid(true);
        Random rand = new Random();
        int n = rand.nextInt(list.size());//gets a random number
        university = list.get(n);
    }
 
    
    public String universityAd(){
        setUniversity();
        return university.getVideoURL();       
    }
    
    public String universityLink(){       
        return "http://gfish2.it.ilstu.edu/mjdifig_Spring2017_LinkedU/faces/universityPage.xhtml";       
    }
}
