package dev.appsody.starter.utils;

import java.sql.*;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class JDBCConnection {

    public Connection getConnection(){
        Connection con = null;

        try{
            Config config = ConfigProvider.getConfig();

            String con_url = config.getValue("jdbcURL", String.class);
            String con_username = config.getValue("dbuser", String.class);
            String con_password = config.getValue("dbpassword", String.class);

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println ("driver.newInstance gotten.");
            String dbName= "inventorydb";
            System.out.println("CON_URL " + con_url);
            System.out.println("USERNAME " + con_username);
            System.out.println("PASSWORD " + con_password);
            con = DriverManager.getConnection(con_url, con_username, con_password);
            System.out.println ("Connection gotten: " + con + ".");
            Statement sql     = con.createStatement ();
            ResultSet results = sql.executeQuery ("use " + dbName + ";");

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();;
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }

}
