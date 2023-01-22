package controller.persistence;

import controller.persistence.database.management.TaskDao;
import model.Task;

/**
 * This class, as the names suggests, is needed to save the tasks
 * in the database and in the file. Since it can be a heavy task and it's a task
 * that is not related to the UI, it is done by a Thread.
 * @author Patrich Tivoli
 */
public class PersistenceManager implements Runnable{

    private Task task;

    public PersistenceManager(Task task) {
        this.task = task;
    }

    /**
     * This method is needed to save the tasks in the database and in the file.
     * @see FileManager
     * @see TaskDao
     */
    @Override
    public void run() {
        FileManager.saveTaskOnFile(task);
        TaskDao.saveTask(task);
    }

}
