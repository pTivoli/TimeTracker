package view;

import com.github.lgooddatepicker.components.DateTimePicker;
import exceptions.TimeTrackerException;
import model.Task;
import model.TimeUtils;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class is the core of the User Interface.
 */
public class View {

    /**
     * This is the font used to display the objects to the user.
     */
    public static final String SEGOE_UI_LIGHT = "Segoe UI Light";
    /**
     * This is the main frame.
     * @see JFrame
     */
    private final JFrame frame;
    /**
     * This is the button used to start the timer to count the time of a task.
     * @see JButton
     */
    private JButton start;
    /**
     * This is the button used to stop the timer to count the time of a task and
     * used to save the tasks in the database and in the file.
     * @see JButton
     */
    private JButton stop;
    /**
     * Clock is actually the timer that counts the time of a task.
     * @see JLabel
     */
    private JLabel clock;
    /**
     * Since clock was the timer that counts the time of a task, the little clock with
     * the current date and time is called now.
     * @see JLabel
     */
    private JLabel now;
    /**
     * This is the textarea for the description of the task.
     * @see JTextArea
     */
    private JTextArea taskDescription;
    /**
     * This is table model of the Daily.
     * @see DefaultTableModel
     */
    private DefaultTableModel tableModel;
    /**
     * This is the date and time picker used to filter the tasks while generating
     * the report. This date and time picker refers to the beginning date and time.
     * A special thanks goes to LGoodDatePicker and all its fantastic contributor for
     * making this fantastic project.
     * For more information about LGoodDatePicker, please visit their
     * <a href="https://github.com/LGoodDatePicker/LGoodDatePicker">GitHub page</a>.
     * @see DateTimePicker
     */
    private DateTimePicker fromDateTimePicker;
    /**
     * This is the date and time picker used to filter the tasks while generating
     * the report. This date and time picker refers to the end date and time.
     * A special thanks goes to LGoodDatePicker and all its fantastic contributor for
     * making this fantastic project.
     * For more information about LGoodDatePicker, please visit their
     * <a href="https://github.com/LGoodDatePicker/LGoodDatePicker">GitHub page</a>.
     * @see DateTimePicker
     */
    private DateTimePicker toDateTimePicker;
    /**
     * This is the button used to generate the report.
     * @see JButton
     */
    private JButton generateReport;
    /**
     * This is the checkbox used to enable the "from" filter for the report.
     * @see JCheckBox
     */
    private JCheckBox fromDateCheckBox;
    /**
     * This is the checkbox used to enable the "to" filter for the report.
     * @see JCheckBox
     */
    private JCheckBox toDateCheckBox;
    /**
     * This is the resource bundle for the languages.
     * @see ResourceBundle
     */
    private final ResourceBundle resourceBundle;

    /**
     * This is the constructor that initialize the frame with all its objects.
     * Now it displays even a nice welcome message.
     * @param resourceBundle the resourceBundle needed for the translations
     * @see JFrame
     * @see ResourceBundle
     */
    public View(ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        setLookAndFeel();
        welcomeMessage();
        frame = new JFrame("Time Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(rootPanel());
        frame.setSize(1000,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * This is the root panel, it contains every graphical object.
     * @return the root panel
     * @see JPanel
     * @see BoxLayout
     */
    private JPanel rootPanel() {
        JPanel panel = new JPanel();
        panel.add(clockPanel());
        panel.add(mainPanel());
        panel.add(reportPanel());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    /**
     * This main panel is actually the control panel (the panel with task description, start and
     * stop button) and the Daily panel (the panel with all the tasks executed today).
     * @return the main panel
     * @see JPanel
     * @see GridLayout
     */
    private JPanel mainPanel() {
        JPanel panel = new JPanel();
        panel.add(controlPanel());
        panel.add(dailyPanelInfo());
        panel.setLayout(new GridLayout(1,2));
        return panel;
    }

    /**
     * This is the control panel which is the panel with the task description, start and
     * stop button.
     * @return the control panel
     * @see JPanel
     * @see GridLayout
     */
    private JPanel controlPanel() {
        JPanel panel = new JPanel();

        clock = new JLabel("", SwingConstants.CENTER);
        clock.setFont(new Font(SEGOE_UI_LIGHT, Font.PLAIN, 50));

        taskDescription = new JTextArea();
        taskDescription.setFont(new Font(SEGOE_UI_LIGHT, Font.PLAIN, 50));

        start = new JButton(resourceBundle.getString("startTimer"));
        start.setEnabled(false);
        start.setFont(new Font(SEGOE_UI_LIGHT, Font.PLAIN, 50));

        stop = new JButton(resourceBundle.getString("stop"));
        stop.setEnabled(false);
        stop.setFont(new Font(SEGOE_UI_LIGHT, Font.PLAIN, 50));

        panel.add(clock);
        panel.add(taskDescription);
        panel.add(start);
        panel.add(stop);

        panel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("control")));
        panel.setLayout(new GridLayout(4,1));
        return panel;
    }

    /**
     * This is the Daily panel which is a panel with a table with all the tasks executed today.
     * @return the daily panel
     * @see JPanel
     * @see JTable
     * @see DefaultTableModel
     * @see JScrollPane
     * @see BorderFactory
     * @see GridLayout
     */
    private JPanel dailyPanelInfo() {
        JTable table = new JTable();
        tableModel = new DefaultTableModel(0, 0);
        String[] header = new String[] {
                resourceBundle.getString("taskName"),
                resourceBundle.getString("start"),
                resourceBundle.getString("end"),
                resourceBundle.getString("timeSpent")
        };
        tableModel.setColumnIdentifiers(header);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("daily")));
        panel.setLayout(new GridLayout(1,1));
        return panel;
    }

    /**
     * This is the clock panel which is the panel that shows the current date and time.
     * @return the clock panel
     * @see JPanel
     * @see Font
     * @see TimeUtils
     */
    private JPanel clockPanel() {
        JPanel panel = new JPanel();
        now = new JLabel(TimeUtils.getFancyDateTime(LocalDateTime.now()));
        now.setFont(new Font(SEGOE_UI_LIGHT, Font.PLAIN, 30));
        panel.add(now);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }

    /**
     * This is the report panel which is the panel that contains the tasks filter and the button
     * used to generate the report.
     * A special thanks goes to LGoodDatePicker and all its fantastic contributor for
     * making this fantastic project.
     * For more information about LGoodDatePicker, please visit their
     * <a href="https://github.com/LGoodDatePicker/LGoodDatePicker">GitHub page</a>.
     * @return the report panel
     * @see DateTimePicker
     * @see JPanel
     * @see FlowLayout
     * @see BorderFactory
     */
    private JPanel reportPanel() {
        JPanel panel = new JPanel();

        JLabel from = new JLabel(resourceBundle.getString("from") + ":");
        panel.add(from);

        fromDateCheckBox = new JCheckBox();
        fromDateCheckBox.setSelected(true);
        panel.add(fromDateCheckBox);

        fromDateTimePicker = new DateTimePicker();
        fromDateTimePicker.getDatePicker().setDateToToday();
        fromDateTimePicker.getTimePicker().setTimeToNow();
        panel.add(fromDateTimePicker);

        JLabel to = new JLabel(resourceBundle.getString("to") + ":");
        panel.add(to);

        toDateCheckBox = new JCheckBox();
        toDateCheckBox.setSelected(true);
        panel.add(toDateCheckBox);

        toDateTimePicker = new DateTimePicker();
        toDateTimePicker.getDatePicker().setDateToToday();
        toDateTimePicker.getTimePicker().setTimeToNow();
        panel.add(toDateTimePicker);

        generateReport = new JButton(resourceBundle.getString("generateReport"));
        panel.add(generateReport);
        panel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("report")));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }

    /**
     * This method sets the look and feel of the program, it uses the system look and feel to make
     * this program a little more fancy.
     * @see UIManager
     */
    private void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new TimeTrackerException("There was a problem while setting the UI look and feel", e);
        }
    }

    /**
     * This method sets the action listener of the start button.
     * @param actionListener the action listener to implement
     * @see ActionListener
     * @see JLabel
     */
    public void setStartActionListener(ActionListener actionListener) {
        start.addActionListener(actionListener);
    }

    /**
     * This method sets the action listener of the stop button.
     * @param actionListener the action listener to implement
     * @see ActionListener
     * @see JLabel
     */
    public void setStopActionListener(ActionListener actionListener) {
        stop.addActionListener(actionListener);
    }

    /**
     * This method returns the string with the value of the timer.
     * @return the value of the timer
     * @see JLabel
     */
    public String getClock() {
        return clock.getText();
    }

    /**
     * This method resets the timer.
     * @see JLabel
     */
    public void resetClock(){
        clock.setText("00:00:00");
    }

    /**
     * This method gets the task description.
     * @return the task description
     * @see JLabel
     */
    public String getTaskDescription() {
        return taskDescription.getText();
    }

    /**
     * This method sets the task description.
     * @param taskDescription the task description
     * @see JLabel
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription.setText(taskDescription);
    }

    /**
     * This is the method used to add the document listener for the text area.
     * @param documentListener the document listener to implement.
     * @see JLabel
     * @see DocumentListener
     */
    public void addDocumentListenerToTaskDescription(DocumentListener documentListener) {
        this.taskDescription.getDocument().addDocumentListener(documentListener);
    }

    /**
     * This method enables the start button.
     * @see JButton
     */
    public void enableStartButton(){
        if (!this.stop.isEnabled()) {
            this.start.setEnabled(true);
        }
    }

    /**
     * This method enables the stop button.
     * @see JButton
     */
    public void enableStopButton(){
        if (!this.start.isEnabled()) {
            this.stop.setEnabled(true);
        }
    }

    /**
     * This method disables the start button.
     * @see JButton
     */
    public void disableStartButton(){
        this.start.setEnabled(false);
    }

    /**
     * This method disables the stop button.
     * @see JButton
     */
    public void disableStopButton(){
        this.stop.setEnabled(false);
    }

    /**
     * This method can be used to get the {@link JLabel label} of the timer.
     * @return the label of the timer
     * @see JLabel
     */
    public JLabel getClockLabel() {
        return this.clock;
    }

    /**
     * This method erase the task description.
     * @see JLabel
     */
    public void eraseTaskDescription() {
        this.taskDescription.setText("");
    }

    /**
     * This method adds a task to the Daily.
     * @param task the task that needs to be added to the Daily.
     * @see DefaultTableModel
     */
    public void addRowOnTable(Task task) {
        this.tableModel.addRow(new String[] {task.getTaskName(), task.getStartTime().toLocalTime().toString(), task.getEndTime().toLocalTime().toString(), task.getDelta()});
    }

    /**
     * This is the method used to retrieve the now label (the current date and time).
     * @return the label with the current date and time
     * @see JLabel
     */
    public JLabel getNow() {
        return now;
    }

    /**
     * This is the method used to get the button that generates the report.
     * @return retrieve the button to generate the report
     * @see JButton
     */
    public JButton getGenerateReport() {
        return generateReport;
    }

    /**
     * This is the method used to get the "from" date and time filter for the report.
     * A special thanks goes to LGoodDatePicker and all its fantastic contributor for
     * making this fantastic project.
     * For more information about LGoodDatePicker, please visit their
     * <a href="https://github.com/LGoodDatePicker/LGoodDatePicker">GitHub page</a>.
     * @return the "from" date and time filter for the report
     * @see DateTimePicker
     * @see LocalDateTime
     */
    public LocalDateTime getFromDate() {
        return fromDateTimePicker.getDateTimePermissive();
    }

    /**
     * This is the method used to get the "to" date and time filter for the report.
     * A special thanks goes to LGoodDatePicker and all its fantastic contributor for
     * making this fantastic project.
     * For more information about LGoodDatePicker, please visit their
     * <a href="https://github.com/LGoodDatePicker/LGoodDatePicker">GitHub page</a>.
     * @return the "to" date and time filter for the report
     * @see DateTimePicker
     * @see LocalDateTime
     */
    public LocalDateTime getToDate() {
        return toDateTimePicker.getDateTimePermissive();
    }

    /**
     * This method is used to create a new message for the user.
     * @return the handler to make a message for the user
     * @see ErrorMessage
     */
    public ErrorMessage getInfoViewHandler() {
        return new ErrorMessage(frame);
    }

    /**
     * This method returns a boolean representing the status of the checkbox of the "from"
     * filter for the report.
     * @return true is selected, false otherwise
     * @see JCheckBox
     */
    public boolean fromReportEnabled() {
        return fromDateCheckBox.isSelected();
    }

    /**
     * This method returns a boolean representing the status of the checkbox of the "to"
     * filter for the report.
     * @return true is selected, false otherwise
     * @see JCheckBox
     */
    public boolean toReportEnabled() {
        return toDateCheckBox.isSelected();
    }

    /**
     * This is the method that displays a nice welcome message to the user.
     */
    public void welcomeMessage() {
        JFrame welcomeMessage = new JFrame();
        JPanel panel = new JPanel();
        JLabel hello = new JLabel(resourceBundle.getString("welcome"));
        hello.setFont(new Font(SEGOE_UI_LIGHT, Font.PLAIN, 100));
        panel.add(hello);
        welcomeMessage.add(panel);
        welcomeMessage.setUndecorated(true);
        welcomeMessage.setSize(500,150);
        welcomeMessage.setLocationRelativeTo(null);
        welcomeMessage.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        welcomeMessage.setVisible(false);
    }

}
