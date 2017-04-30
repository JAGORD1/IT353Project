package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mark DiFiglio
 * 4/27/2017
 * 
 * One place for database connection information.
 */
public class DatabaseHelper {
    
    /**
     * 
     * @param word
     * @return the string wrap in % for sql selects.
     */
    public static String percentWrap(String word){       
        return "%" + word + "%";
    }
    
    /**
     * 
     * @return a connection to the database.
     */
    public static Connection dataBaseConnection(){
        String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/mjdifig_Spring2017_LinkedU";
        Connection DBConn = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }       
        try {           
            DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
        return DBConn;
    }
}
