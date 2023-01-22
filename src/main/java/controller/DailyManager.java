package controller;

import controller.persistence.database.management.TaskDao;
import model.Task;
import view.View;

import java.util.List;

/**
 * This class updates the Daily which is the little table that shows the tasks that are
 * executed today.
 * @author Patrich Tivoli
 */
public class DailyManager {

    private View view;

    public DailyManager(View view) {
        this.view = view;
    }

    /**
     * This method initialize the Daily.<br>
     * It calls the DAO to retrieve all the tasks executed today through the method
     * {@link TaskDao#getDailyTask() getDailyTask}. Once all the tasks are retrieved the table rows are added
     * and displayed to the user.
     * @see Task
     * @see TaskDao
     * @see View
     */
    public void initDaily() {
        List<Task> taskList = TaskDao.getDailyTask();
        for (Task task : taskList) {
            view.addRowOnTable(task);
        }
    }

}
