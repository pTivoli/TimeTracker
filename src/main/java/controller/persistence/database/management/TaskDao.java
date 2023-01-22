package controller.persistence.database.management;

import exceptions.TimeTrackerException;
import model.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a Data Access Object which aims saving a task on the H2 database.
 * @author Patrich Tivoli
 */
public class TaskDao {

    /**
     * Private Constructor to hide the default one.
     */
    private TaskDao() { }

    /**
     * This method saves a task on the database.
     * @param task the task that needs to be saved
     * @see DatabaseManager
     * @see controller.persistence.PersistenceManager
     */
    public static void saveTask(Task task) {
        try (Connection connection = DatabaseManager.getDatabaseConnection()) {
            String sql = "INSERT INTO TIME_TRACKER VALUES ( '" + task.getStartTime() + "', '" + task.getEndTime() + "', '" + task.getTaskName() + "', '" + task.getDelta() + "' )";
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new TimeTrackerException("There was a problem while saving the task on the database", e);
        }
    }

    /**
     * This method retrieves all the task executed today.
     * @return the task executed today
     * @see LocalDateTime
     * @see DatabaseManager
     * @see controller.persistence.PersistenceManager
     */
    public static List<Task> getDailyTask() {
        LocalDateTime from = LocalDateTime.now().toLocalDate().atStartOfDay();
        return getTaskFromRange(from, null);
    }

    /**
     * This method gets a list of tasks based on a range of {@link LocalDateTime LocalDateTime}.
     * @param from "from" date and time
     * @param to "to" date and time
     * @return a list of tasks
     */
    public static List<Task> getTaskFromRange(LocalDateTime from, LocalDateTime to) {
        List<Task> taskList = new LinkedList<>();
        try (Connection connection = DatabaseManager.getDatabaseConnection()) {
            String sql = buildQuery(from, to);
            try (Statement statement = connection.createStatement()){
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    String taskName = resultSet.getString("TASK_NAME");
                    Task task = new Task(taskName);
                    String startTime = resultSet.getString("START_TIME");
                    String endTime = resultSet.getString("END_TIME");
                    String delta = resultSet.getString("DELTA");
                    task.setStartTime(LocalDateTime.parse(startTime));
                    task.setEndTime(LocalDateTime.parse(endTime));
                    task.setDelta(delta);
                    taskList.add(task);
                }
            }
        } catch (SQLException e) {
            throw new TimeTrackerException("There was a problem while saving the task on the database", e);
        }
        return taskList;
    }

    /**
     * This method aims to build a query based on the available data.
     * @param from "from" date and time
     * @param to "to" date and time
     * @return the query
     */
    private static String buildQuery(LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT * FROM TIME_TRACKER WHERE 1 = 1 ";
        if (from != null) {
            sql += "AND START_TIME >= '" + from + "' ";
        }
        if (to != null) {
            sql += "AND END_TIME <= '" + to + "'";
        }
        return sql;
    }

}
