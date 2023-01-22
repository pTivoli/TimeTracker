package controller.persistence;

import exceptions.TimeTrackerException;
import model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class helps to save tasks and other things in a file.
 * @author Patrich Tivoli
 */
public class FileManager {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    /**
     * This constructor makes/opens a file called report.dat.
     * @throws IOException in case something goes wrong
     */
    public FileManager() throws IOException {
        initFileManager("report.dat", true);
    }

    /**
     * This constructor makes/opens a file.
     * @param fileName the name of the file
     * @param append append or not
     * @throws IOException in case something goes wrong
     */
    public FileManager(String fileName, boolean append) throws IOException {
        initFileManager(fileName, append);
    }

    /**
     * This method initialize the file manager by creating and or opening the file.
     * @param fileName the name of the file
     * @param append append or not
     * @throws IOException in case something goes wrong
     */
    private void initFileManager(String fileName, boolean append) throws IOException {
        fileWriter = new FileWriter(fileName, append);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    /**
     * This method writes a task in a file.
     * @param task the task that needs to be saved
     * @throws IOException in case something goes wrong
     */
    private void saveTask(Task task) throws IOException {
        this.write(task.toString());
    }

    /**
     * This method aims to save a single task on a file called report.dat: it opens/creates the file,
     * it saves the task and it closes it.<br>
     * This method has to be used only to save a task in that file and it must not be used for bulk savings.
     * @param task the task that needs to be saved
     */
    public static void saveTaskOnFile(Task task) {
        try {
            FileManager fileManager = new FileManager();
            fileManager.saveTask(task);
            fileManager.closeFileManager();
        } catch (IOException e) {
            throw new TimeTrackerException("There was a problem while saving the task on the file", e);
        }
    }

    /**
     * This method just writes a string on the file.
     * @param text the text that need to be saved
     * @throws IOException in case something goes wrong
     */
    public void write(String text) throws IOException {
        bufferedWriter.write(text);
        bufferedWriter.newLine();
    }

    /**
     * It closes the file.
     * @throws IOException in case something goes wrong
     */
    public void closeFileManager() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }

}
