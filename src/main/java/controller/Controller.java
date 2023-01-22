package controller;

import controller.persistence.PersistenceManager;
import exceptions.TimeTrackerException;
import model.Task;
import view.View;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Timer;

/**
 * The controller implements all the logic behind this TimeTracker, from the event listener
 * when a button is clicked, to the persistence of the data.
 * @author Patrich Tivoli
 */
public class Controller {

    private View view;
    private Task currentTask;
    private DailyManager dailyManager;
    private Timer clock;
    private final ResourceBundle resourceBundle;

    /**
     * {@link ResourceBundle ResourceBundle} is used for the translations of the program.<br>
     * View is used to control everything related to the UI of the program.
     * @param resourceBundle the object for the translation
     * @param view the UI
     * @see ResourceBundle
     * @see View
     */
    public Controller(ResourceBundle resourceBundle, View view){
        this.resourceBundle = resourceBundle;
        this.view = view;
        initView(this.view);
        startNow(this.view);
    }

    /**
     * This method initialize the view:<br>
     * <ul>
     *     <li>Timer initialization, it puts the string "00:00:00" on the timer</li>
     *     <li>Event listener for the text area for the task description</li>
     *     <li>Event listener for the start button</li>
     *     <li>Event listener for the stop button</li>
     *     <li>Event listener for the report generation button</li>
     *     <li>Initialization of the Daily (list of tasks of today)</li>
     * </ul>
     * @param view view is needed to implement a listener
     * @see View
     */
    private void initView(View view){
        view.resetClock();
        enableButtonWhenTextFieldNotBlank();
        ifStartButtonIsPressedThenStartTimer();
        ifStopButtonIsPressedThenStopTimer();
        ifGenerateButtonIsPressedThenGenerateReport();
        initDaily();
    }

    /**
     * This method add a {@link DocumentListener DocumentListener} that enables the start
     * button if there is text in the textarea, otherwise the button will be disabled.
     * @see View
     * @see DocumentListener
     */
    private void enableButtonWhenTextFieldNotBlank(){
        view.addDocumentListenerToTaskDescription(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableStartButtonIfTaskDescriptionIsEmpty();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableStartButtonIfTaskDescriptionIsEmpty();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableStartButtonIfTaskDescriptionIsEmpty();
            }
        });
    }

    /**
     * This method just checks if the task description is empty or not.
     * @return true if is empty, false otherwise
     * @see View
     */
    private boolean isTaskDescriptionEmpty() {
        return view.getTaskDescription().isEmpty();
    }

    /**
     * This method enables the start button if there is text in the text area,
     * otherwise the button is disabled.
     * @see View
     */
    private void enableStartButtonIfTaskDescriptionIsEmpty() {
        if (isTaskDescriptionEmpty()) {
            view.disableStartButton();
        } else {
            view.enableStartButton();
        }
    }

    /**
     * This method implements an {@link ActionListener ActionListener} to
     * start the timer, disable the start button and to enable the stop button.
     * @see View
     * @see ActionListener
     */
    private void ifStartButtonIsPressedThenStartTimer() {
        view.setStartActionListener(e -> {
            view.disableStartButton();
            view.enableStopButton();
            currentTask = new Task(view.getTaskDescription());
            startClock(view);
        });
    }

    /**
     * This method implements an {@link ActionListener ActionListener} to:<br>
     * <ul>
     *     <li>Stop the clock</li>
     *     <li>Setting the task model object to be saved</li>
     *     <li>Save the task in the DB and in the file</li>
     *     <li>Add the row on the table</li>
     *     <li>Reset the timer</li>
     *     <li>Erase the task description</li>
     *     <li>Disable the stop button</li>
     * </ul>
     * @see View
     */
    private void ifStopButtonIsPressedThenStopTimer() {
        view.setStopActionListener(e -> {
            stopClock();
            currentTask.setEndTime(LocalDateTime.now());
            currentTask.setDelta(view.getClock());
            saveTask(currentTask);
            view.addRowOnTable(currentTask);
            view.resetClock();
            view.eraseTaskDescription();
            view.disableStopButton();
        });
    }

    /**
     * This method is used to generate the report.
     * @see View
     * @see ActionListener
     * @see ReportGenerator
     */
    private void ifGenerateButtonIsPressedThenGenerateReport() {
        view.getGenerateReport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    /**
     * This method generates the report: starting from a range of dates and times, it retrieves the data from
     * the database and then it makes a file with the result of the query.<br>
     * If the "from" date and time is disabled, then the query will retrieve all the tasks before the "to" date
     * and time. <br>
     * If the "to" date and time is disabled, then the query will retrieve all the tasks
     * after the "from" date and time.<br>
     * If "from" and "to" date and time are disabled, then all the tasks will be retrieved.
     * @see LocalDateTime
     * @see View
     * @see ReportGenerator
     * @see ResourceBundle
     * @see TimeTrackerException
     */
    private void generateReport() {
        LocalDateTime fromDate = view.getFromDate();
        LocalDateTime toDate = view.getToDate();
        if (checkDatesAndTimes(fromDate, toDate)) {
            try {
                if (!view.fromReportEnabled()) {
                    fromDate = null;
                }
                if (!view.toReportEnabled()) {
                    toDate = null;
                }
                new ReportGenerator(fromDate, toDate).generateTextReport();
                view.getInfoViewHandler().showInfo(
                        resourceBundle.getString("reportCreated"),
                        resourceBundle.getString("reportSuccessfullyCreated")
                );
            } catch (IOException ex) {
                throw new TimeTrackerException("There was a problem while creating the report", ex);
            }
        }
    }

    /**
     * This method checks the "from" date and time and the "to" date and time to
     * see if there are problems.<br>
     * A message will be shown to the user in these scenarios:
     * <ul>
     *     <li>"from" date and time is after the "to" date and time</li>
     *     <li>"from" date and time is placed in the future</li>
     * </ul>
     * @param from the "from" date and time
     * @param to the "to" date and time
     * @return true if everything is good, false otherwise
     * @see View
     * @see ResourceBundle
     */
    private boolean checkDatesAndTimes(LocalDateTime from, LocalDateTime to) {
        LocalDateTime now = LocalDateTime.now();
        boolean isFromBeforeTo = from.isBefore(to);
        boolean isFromNotFuture = from.isBefore(now);
        boolean isFromEnabled = view.fromReportEnabled();
        boolean isToEnabled = view.toReportEnabled();
        if (isFromEnabled && isToEnabled) {
            if (!isFromBeforeTo) {
                view.getInfoViewHandler().showError(
                        resourceBundle.getString("fromAfterOrEqualToTitleError"),
                        resourceBundle.getString("fromAfterOrEqualToMessageError"));
                return false;
            } else if (!isFromNotFuture) {
                view.getInfoViewHandler().showError(
                        resourceBundle.getString("fromFutureTitle"),
                        resourceBundle.getString("fromFutureMessage")
                );
                return false;
            }
        } else if (isFromEnabled && !isFromNotFuture) {
            view.getInfoViewHandler().showError(
                    resourceBundle.getString("fromFutureTitle"),
                    resourceBundle.getString("fromFutureMessage")
            );
            return false;
        }
        return true;
    }

    /**
     * This method saves a task in the file and in the database.<br>
     * Since it can be a heavy task (and it's not related to the view), a thread is created.
     * @param task the task that need to be saved
     */
    private void saveTask(Task task) {
        Thread thread = new Thread(new PersistenceManager(task));
        thread.start();
    }

    /**
     * This method initialize the "now" clock which is just a simple clock that shows
     * the output of {@link LocalDateTime#now() now}.
     * @param view the view
     * @see View
     * @see Timer
     * @see ClockManager
     * @see java.util.TimerTask
     */
    private void startNow(View view) {
        Timer now = new Timer("now");
        now.scheduleAtFixedRate(new ClockManager(view.getNow()), 0, 1000L);
    }

    /**
     * This method initialize the "clock" which is the timer that counts the time of
     * a task.
     * @param view the view
     * @see View
     * @see Timer
     * @see ClockManager
     * @see java.util.TimerTask
     */
    private void startClock(View view) {
        clock = new Timer("clock");
        clock.scheduleAtFixedRate(new TimerManager(view.getClock(), view.getClockLabel()), 1000L, 1000L);
    }

    /**
     * This method stops the timer that counts the time of a task.
     * @see Timer
     * @see java.util.TimerTask
     */
    private void stopClock() {
        clock.cancel();
    }

    /**
     * This method initialize the Daily, it retrieves all the task executed today.
     * @see DailyManager
     */
    private void initDaily() {
        dailyManager = new DailyManager(view);
        dailyManager.initDaily();
    }

}
