/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author admin
 */
public class StudentBean {

    public StudentBean() {
    }

// These correspond to the form elements
    private String email;
    private String password;
    private String password2;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String provider;
    private int ACTScore;
    private int SATScore;
    private int PSATScore;
    private String highSchool;
    private String essay;
    private String universities;
    private String majors;
    private String activities;
    private String mixtapeURL;
    private String imageURL;
    private boolean mailList;

    public StudentBean(String email, String password, String password2, String firstName, 
            String lastName, String phoneNumber, String provider, int ACTScore, int SATScore, 
            int PSATScore, String highSchool, String essay, String universities, String majors, 
            String activities, String mixtapeURL, String imageURL, boolean mailList) {
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.provider = provider;
        this.ACTScore = ACTScore;
        this.SATScore = SATScore;
        this.PSATScore = PSATScore;
        this.highSchool = highSchool;
        this.essay = essay;
        this.universities = universities;
        this.majors = majors;
        this.activities = activities;
        this.mixtapeURL = mixtapeURL;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getACTScore() {
        return ACTScore;
    }

    public void setACTScore(int ACTScore) {
        this.ACTScore = ACTScore;
    }

    public int getSATScore() {
        return SATScore;
    }

    public void setSATScore(int SATScore) {
        this.SATScore = SATScore;
    }

    public int getPSATScore() {
        return PSATScore;
    }

    public void setPSATScore(int PSATScore) {
        this.PSATScore = PSATScore;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public String getEssay() {
        return essay;
    }

    public void setEssay(String essay) {
        this.essay = essay;
    }

    public String getUniversities() {
        return universities;
    }

    public void setUniversities(String universities) {
        this.universities = universities;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getMixtapeURL() {
        return mixtapeURL;
    }

    public void setMixtapeURL(String mixtapeURL) {
        this.mixtapeURL = mixtapeURL;
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
    
}
