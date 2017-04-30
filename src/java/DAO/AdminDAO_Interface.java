package DAO;

import Model.AdminBean;

/**
 *
 * @author Mark DiFiglio
 * 4/29/2017
 * 
 */
public interface AdminDAO_Interface {
    public int createAdmin(AdminBean AdminDAO);
    public boolean checkAdminEmail(String email);
    public boolean AdminLogin(String email, String password);
    public int updateAdmin(AdminBean AdminDAO, String originalEmail);
    public int changePassword(String email, String password);
}
