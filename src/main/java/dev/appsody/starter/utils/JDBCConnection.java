package dev.appsody.starter.utils;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.sql.*;

/**
 * Class is responsible for connecting a data source given source url
 * username and password.
 */
public class JDBCConnection {

    /**
     * Method is responsible for connecting a database given, username password and source url
     * @return connection on success otherwise an error of the stack trace
     */
    public Connection getConnection() {
        Connection connection = null;

        try {
            Config config = ConfigProvider.getConfig();
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            String connectionUrl = config.getValue("jdbcURL", String.class);
            String connectionUsername = config.getValue("dbuser", String.class);
            String connectionPassword = config.getValue("dbpassword", String.class);
            String dbName = "inventorydb";

            connection = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);

            System.out.println("Connection gotten: " + connection + ".");
            Statement sql = connection.createStatement();
            ResultSet results = sql.executeQuery("use " + dbName + ";");

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }
}
