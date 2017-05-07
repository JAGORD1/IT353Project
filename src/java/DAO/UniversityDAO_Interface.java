package DAO;

import Model.UniversityBean;
import java.util.ArrayList;

/**
 *
 * @author Mark DiFiglio
 * 4/17/2017
 */
public interface UniversityDAO_Interface {
    public int createUniversity(UniversityBean universityDAO);
    public boolean checkUniversityName(String universityName);
    public boolean checkEmail(String email);
    public boolean universityLogin(String email, String password);
    public ArrayList<UniversityBean> searchUniversity(UniversityBean searchUniversity); //name, state, city, cost, majors
    public ArrayList<UniversityBean> searchUniversityByPaid(Boolean paid); //by paid for admin
    public ArrayList<UniversityBean> searchUniversityByMailList(Boolean mail); //by mailing list
    public int updateUniversity(UniversityBean universityDAO, String originalEmail);
    public UniversityBean getUniversityInfo(String email);
    public int changePassword(String email, String password);
    
    public ArrayList<UniversityBean> getReport();
    public int addAdview(String email);
    public int addProfileView(String email);
}
