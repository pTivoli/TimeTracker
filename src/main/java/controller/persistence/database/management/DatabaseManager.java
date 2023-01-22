package controller.persistence.database.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is a Database Manager which helps to use the database with the basic operation
 * such as opening a connection and closing it.
 * @author Patrich Tivoli
 */
public class DatabaseManager {

    private static Connection connection = null;

    /**
     * Private Constructor to hide the default one.
     */
    private DatabaseManager() { }

    /**
     * This method opens a connection to the timeTracker database, if it's already opened, the opened
     * connection will be returned.<br>
     * Note: there is no password since there is no sensitive data that need to be stored.
     * @return a connection to the database
     * @throws SQLException in case something goes wrong
     */
    private static Connection openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:h2:~/timeTracker", "timeTracker", ""); //NOSONAR
        }
        return connection;
    }

    /**
     * This method creates a table if it does not exist.
     * @throws SQLException in case something goes wrong
     */
    private static void createTableIfNotExists() throws SQLException {
        String createTableIfNotExists = "CREATE TABLE IF NOT EXISTS TIME_TRACKER ( START_TIME VARCHAR(23) NOT NULL PRIMARY KEY, END_TIME VARCHAR(23) NOT NULL, TASK_NAME VARCHAR(50) NOT NULL, DELTA VARCHAR(8) NOT NULL );";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(createTableIfNotExists);
        }
    }

    /**
     * This method opens a connection and create a table if it does not exist.
     * @return the connection to the database
     * @throws SQLException in case something goes wrong
     */
    public static Connection getDatabaseConnection() throws SQLException {
        Connection conn = openConnection();
        createTableIfNotExists();
        return conn;
    }

    /**
     * This method closes the connection database.
     * @throws SQLException in case something goes wrong
     */
    public static void closeConnection() throws SQLException {
        connection.close();
    }

}
