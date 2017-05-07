/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author admin
 */
public class UniversityBean {

    public UniversityBean() {
    }

// These correspond to the form elements
    private String email;
    private String password;
    private String password2;
    private String universityName;
    private String state;
    private String city;
    private double cost;
    private String essay;
    private String majors;
    private String videoURL;
    private String imageURL;
    private boolean mailList;
    private boolean paid;
    private String applyURL;
    private int AdViews;
    private int profileViews;

    public UniversityBean(String email, String password, String password2, 
            String universityName, String state, String city, double cost, 
            String essay, String majors, String videoURL, String imageURL, 
            boolean mailList) {
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.universityName = universityName;
        this.state = state;
        this.city = city;
        this.cost = cost;
        this.essay = essay;
        this.majors = majors;
        this.videoURL = videoURL;
        this.imageURL = imageURL;
        this.mailList = mailList;
    }
      
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getEssay() {
        return essay;
    }

    public void setEssay(String essay) {
        this.essay = essay;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * @return the mailList
     */
    public boolean getMailList() {
        return mailList;
    }

    /**
     * @param mailList the mailList to set
     */
    public void setMailList(boolean mailList) {
        this.mailList = mailList;
    }  

    /**
     * @return the paid
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the applyURL
     */
    public String getApplyURL() {
        return applyURL;
    }

    /**
     * @param applyURL the applyURL to set
     */
    public void setApplyURL(String applyURL) {
        this.applyURL = applyURL;
    }

    /**
     * @return the AdViews
     */
    public int getAdViews() {
        return AdViews;
    }

    /**
     * @param AdViews the AdViews to set
     */
    public void setAdViews(int AdViews) {
        this.AdViews = AdViews;
    }

    /**
     * @return the profileViews
     */
    public int getProfileViews() {
        return profileViews;
    }

    /**
     * @param profileViews the profileViews to set
     */
    public void setProfileViews(int profileViews) {
        this.profileViews = profileViews;
    }
    
    
}
