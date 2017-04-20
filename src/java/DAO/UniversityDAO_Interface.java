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
    public boolean universityLogin(String universityName, String password);
    public ArrayList<UniversityBean> searchUniversity(ArrayList<String> searchInfo);
    public int updateUniversity(UniversityBean universityDAO, String originalUniversityName);
    public UniversityBean getUniversityInfo(String universityName);
    public int changePassword(String universityName, String password);
}
