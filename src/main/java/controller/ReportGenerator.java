package controller;

import controller.persistence.FileManager;
import controller.persistence.database.management.TaskDao;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is used to make a report, a report is a file with a list of tasks.<br>
 * The report is a text file with a ".dat" extension.
 * @author Patrich Tivoli
 */
public class ReportGenerator {

    private LocalDateTime from;
    private LocalDateTime to;

    public ReportGenerator(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    /**
     * This method aims to make a text file with a list of tasks that are included in a time range.<br>
     * The time range is given by the constructor of this method.<br>
     * In order to retrieve all the tasks from the desired time range, the method
     * {@link TaskDao#getTaskFromRange(LocalDateTime, LocalDateTime) getTaskFromRange} of the DAO is used,
     * then, all the retrieved tasks will be putted in a file with the same location of the program
     * and it will be called export.dat.
     * @see Task
     * @see List
     * @see TaskDao
     * @see FileManager
     * @throws IOException thrown when there are problems while saving the task
     */
    public void generateTextReport() throws IOException {
        List<Task> taskList = TaskDao.getTaskFromRange(from, to);
        FileManager fileManager = new FileManager("export.dat", false);
        for (Task task : taskList) {
            fileManager.write(task.toString());
        }
        fileManager.closeFileManager();
    }

}
