/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

/**
 *
 * @author LAPTOPVTC.VN
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatabaseListener implements ServletContextListener {
    
    Connection con;

    @Override
    public void contextInitialized(ServletContextEvent sce) {        
        ServletContext sc = sce.getServletContext();
        String db = sc.getInitParameter("db_name");
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/" + db, "root", "root");
        }
        catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error: " + e);
        }
        
        sc.setAttribute("con", con);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            con.close();
        }
        catch (SQLException e) {
            System.err.println("Error: " + e);
        }//try
    }
}


