/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.StudentDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Model.StudentBean;
import edu.ilstu.it.TextSenderService;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
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
    private String resetEmail;
    private boolean sendSMS;

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
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
    
    public String resetPassword() {
        
        StudentDAO data = new StudentDAO();
        
        StudentBean temp = data.getStudentInfo(resetEmail);
        
        if (temp != null) {
               sendEmail(temp.getEmail());
               if (sendSMS) sendSMS(temp.getProvider(), temp.getPhoneNumber(), "Reset password link here");
               return "resetSent.xhtml";
        }
        else return "resetFailed.xhtml";
    }
    
    public void sendEmail(String destination) {
        // Recipient's email ID needs to be mentioned.
        String to = destination;

        // Sender's email ID needs to be mentioned
        String from = "caferg2@ilstu.edu";
        
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
                return new PasswordAuthentication("caferg2@ilstu.edu", "Bootcat00885");
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
            message.setContent("Reset password link here",
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
            return "studentPage.xhtml";
        }
        else{
            return "index.xhtml";
        }
    }
}
