package view;

import com.github.lgooddatepicker.components.DateTimePicker;
import exceptions.TimeTrackerException;
import model.Task;
import model.TimeUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class is the core of the User Interface.
 */
public class View {

    private static String resolveFontFamily() {
        String[] preferredFonts = new String[] {"Inter", "Segoe UI", "SF Pro Text", "SansSerif"};
        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String preferredFont : preferredFonts) {
            for (String availableFont : availableFonts) {
                if (preferredFont.equalsIgnoreCase(availableFont)) {
                    return availableFont;
                }
            }
        }
        return "SansSerif";
    }

    private static final String FONT_FAMILY = resolveFontFamily();
    private static final Color BACKGROUND_COLOR = new Color(15, 23, 42);
    private static final Color CARD_COLOR = new Color(30, 41, 59);
    private static final Color SURFACE_COLOR = new Color(51, 65, 85);
    private static final Color SURFACE_ALT_COLOR = new Color(37, 48, 68);
    private static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private static final Color ACCENT_COLOR = new Color(56, 189, 248);
    private static final Color ACCENT_HOVER_COLOR = new Color(14, 165, 233);
    private static final Color STOP_COLOR = new Color(244, 63, 94);
    private static final Color STOP_HOVER_COLOR = new Color(225, 29, 72);
    private static final Color ACTION_COLOR = new Color(45, 212, 191);
    private static final Color ACTION_HOVER_COLOR = new Color(20, 184, 166);
    private static final Color DISABLED_COLOR = new Color(71, 85, 105);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);

    private static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 30);
    private static final Font SECTION_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    private static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 16);
    private static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    private static final Font TIMER_FONT = new Font(FONT_FAMILY, Font.BOLD, 54);
    private static final Font WELCOME_FONT = new Font(FONT_FAMILY, Font.BOLD, 52);
    private static final int BUTTON_ARC_SIZE = 18;

    private final JFrame frame;
    private JButton start;
    private JButton stop;
    private JLabel clock;
    private JLabel now;
    private JTextArea taskDescription;
    private DefaultTableModel tableModel;
    private DateTimePicker fromDateTimePicker;
    private DateTimePicker toDateTimePicker;
    private JButton generateReport;
    private JCheckBox fromDateCheckBox;
    private JCheckBox toDateCheckBox;
    private final ResourceBundle resourceBundle;

    /**
     * This is the constructor that initialize the frame with all its objects.
     * @param resourceBundle the resourceBundle needed for the translations
     */
    public View(ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        setLookAndFeel();
        welcomeMessage();
        frame = new JFrame("Time Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(rootPanel());
        frame.setMinimumSize(new Dimension(1180, 760));
        frame.setSize(1280, 820);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * This is the root panel, it contains every graphical object.
     * @return the root panel
     */
    private JPanel rootPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 24));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.add(clockPanel(), BorderLayout.NORTH);
        panel.add(mainPanel(), BorderLayout.CENTER);
        panel.add(reportPanel(), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * This main panel is actually the control panel and the Daily panel.
     * @return the main panel
     */
    private JPanel mainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 24, 0));
        panel.setOpaque(false);
        panel.add(controlPanel());
        panel.add(dailyPanelInfo());
        return panel;
    }

    /**
     * This is the control panel which is the panel with the task description, start and
     * stop button.
     * @return the control panel
     */
    private JPanel controlPanel() {
        JPanel panel = createCardPanel(resourceBundle.getString("control"));

        clock = new JLabel("00:00:00", SwingConstants.CENTER);
        clock.setFont(TIMER_FONT);
        clock.setForeground(TEXT_PRIMARY);
        clock.setBorder(new EmptyBorder(8, 0, 8, 0));

        taskDescription = new JTextArea(8, 20);
        taskDescription.setLineWrap(true);
        taskDescription.setWrapStyleWord(true);
        taskDescription.setFont(BODY_FONT);
        taskDescription.setForeground(TEXT_PRIMARY);
        taskDescription.setCaretColor(TEXT_PRIMARY);
        taskDescription.setBackground(SURFACE_COLOR);
        taskDescription.setMargin(new Insets(18, 18, 18, 18));
        taskDescription.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JScrollPane taskScrollPane = new JScrollPane(taskDescription);
        styleScrollPane(taskScrollPane);

        start = new ModernButton(resourceBundle.getString("startTimer"), ACCENT_COLOR, ACCENT_HOVER_COLOR);
        start.setEnabled(false);

        stop = new ModernButton(resourceBundle.getString("stop"), STOP_COLOR, STOP_HOVER_COLOR);
        stop.setEnabled(false);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(start);
        buttonsPanel.add(stop);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);
        content.add(clock, BorderLayout.NORTH);
        content.add(taskScrollPane, BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    /**
     * This is the Daily panel which is a panel with a table with all the tasks executed today.
     * @return the daily panel
     */
    private JPanel dailyPanelInfo() {
        tableModel = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] header = new String[] {
                resourceBundle.getString("taskName"),
                resourceBundle.getString("start"),
                resourceBundle.getString("end"),
                resourceBundle.getString("timeSpent")
        };
        tableModel.setColumnIdentifiers(header);

        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        styleScrollPane(scrollPane);

        JPanel panel = createCardPanel(resourceBundle.getString("daily"));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * This is the clock panel which is the panel that shows the current date and time.
     * @return the clock panel
     */
    private JPanel clockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Time Tracker");
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_PRIMARY);

        now = new JLabel(TimeUtils.getFancyDateTime(LocalDateTime.now()), SwingConstants.RIGHT);
        now.setFont(BODY_FONT);
        now.setForeground(TEXT_PRIMARY);

        JPanel nowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        nowPanel.setOpaque(false);
        nowPanel.add(createChip(now));

        panel.add(title, BorderLayout.WEST);
        panel.add(nowPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * This is the report panel which is the panel that contains the tasks filter and the button
     * used to generate the report.
     * @return the report panel
     */
    private JPanel reportPanel() {
        JPanel panel = createCardPanel(resourceBundle.getString("report"));
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        JLabel from = createFieldLabel(resourceBundle.getString("from") + ":");
        fromDateCheckBox = createCheckBox();
        fromDateCheckBox.setSelected(true);
        fromDateTimePicker = new DateTimePicker();
        styleDateTimePicker(fromDateTimePicker);
        fromDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        JLabel to = createFieldLabel(resourceBundle.getString("to") + ":");
        toDateCheckBox = createCheckBox();
        toDateCheckBox.setSelected(true);
        toDateTimePicker = new DateTimePicker();
        styleDateTimePicker(toDateTimePicker);
        toDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        generateReport = new ModernButton(resourceBundle.getString("generateReport"), ACTION_COLOR, ACTION_HOVER_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.weightx = 0;
        content.add(from, gbc);

        gbc.gridx = 1;
        content.add(fromDateCheckBox, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        content.add(fromDateTimePicker, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        content.add(to, gbc);

        gbc.gridx = 1;
        content.add(toDateCheckBox, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        content.add(toDateTimePicker, gbc);

        gbc.gridy = 0;
        gbc.gridx = 3;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        content.add(generateReport, gbc);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    /**
     * This method sets the look and feel of the program.
     */
    private void setLookAndFeel(){
        try {
            boolean nimbusApplied = false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    nimbusApplied = true;
                    break;
                }
            }
            if (!nimbusApplied) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            applyGlobalTheme();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new TimeTrackerException("There was a problem while setting the UI look and feel", e);
        }
    }

    private void applyGlobalTheme() {
        UIManager.put("control", BACKGROUND_COLOR);
        UIManager.put("info", BACKGROUND_COLOR);
        UIManager.put("nimbusBase", CARD_COLOR);
        UIManager.put("nimbusBlueGrey", SURFACE_COLOR);
        UIManager.put("nimbusFocus", ACCENT_COLOR);
        UIManager.put("text", TEXT_PRIMARY);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("OptionPane.background", CARD_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.font", BODY_FONT);
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("TextArea.font", BODY_FONT);
        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("Table.font", BODY_FONT);
        UIManager.put("TableHeader.font", SECTION_FONT);
        UIManager.put("CheckBox.font", BODY_FONT);
    }

    /**
     * This method sets the action listener of the start button.
     * @param actionListener the action listener to implement
     */
    public void setStartActionListener(ActionListener actionListener) {
        start.addActionListener(actionListener);
    }

    /**
     * This method sets the action listener of the stop button.
     * @param actionListener the action listener to implement
     */
    public void setStopActionListener(ActionListener actionListener) {
        stop.addActionListener(actionListener);
    }

    /**
     * This method returns the string with the value of the timer.
     * @return the value of the timer
     */
    public String getClock() {
        return clock.getText();
    }

    /**
     * This method resets the timer.
     */
    public void resetClock(){
        clock.setText("00:00:00");
    }

    /**
     * This method gets the task description.
     * @return the task description
     */
    public String getTaskDescription() {
        return taskDescription.getText();
    }

    /**
     * This method sets the task description.
     * @param taskDescription the task description
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription.setText(taskDescription);
    }

    /**
     * This is the method used to add the document listener for the text area.
     * @param documentListener the document listener to implement.
     */
    public void addDocumentListenerToTaskDescription(DocumentListener documentListener) {
        this.taskDescription.getDocument().addDocumentListener(documentListener);
    }

    /**
     * This method enables the start button.
     */
    public void enableStartButton(){
        if (!this.stop.isEnabled()) {
            this.start.setEnabled(true);
        }
    }

    /**
     * This method enables the stop button.
     */
    public void enableStopButton(){
        if (!this.start.isEnabled()) {
            this.stop.setEnabled(true);
        }
    }

    /**
     * This method disables the start button.
     */
    public void disableStartButton(){
        this.start.setEnabled(false);
    }

    /**
     * This method disables the stop button.
     */
    public void disableStopButton(){
        this.stop.setEnabled(false);
    }

    /**
     * This method can be used to get the label of the timer.
     * @return the label of the timer
     */
    public JLabel getClockLabel() {
        return this.clock;
    }

    /**
     * This method erase the task description.
     */
    public void eraseTaskDescription() {
        this.taskDescription.setText("");
    }

    /**
     * This method adds a task to the Daily.
     * @param task the task that needs to be added to the Daily.
     */
    public void addRowOnTable(Task task) {
        this.tableModel.addRow(new String[] {
                task.getTaskName(),
                task.getStartTime().toLocalTime().toString(),
                task.getEndTime().toLocalTime().toString(),
                task.getDelta()
        });
    }

    /**
     * This is the method used to retrieve the now label.
     * @return the label with the current date and time
     */
    public JLabel getNow() {
        return now;
    }

    /**
     * This is the method used to get the button that generates the report.
     * @return retrieve the button to generate the report
     */
    public JButton getGenerateReport() {
        return generateReport;
    }

    /**
     * This is the method used to get the "from" date and time filter for the report.
     * @return the "from" date and time filter for the report
     */
    public LocalDateTime getFromDate() {
        return fromDateTimePicker.getDateTimePermissive();
    }

    /**
     * This is the method used to get the "to" date and time filter for the report.
     * @return the "to" date and time filter for the report
     */
    public LocalDateTime getToDate() {
        return toDateTimePicker.getDateTimePermissive();
    }

    /**
     * This method is used to create a new message for the user.
     * @return the handler to make a message for the user
     */
    public ErrorMessage getInfoViewHandler() {
        return new ErrorMessage(frame);
    }

    /**
     * This method returns a boolean representing the status of the checkbox of the "from"
     * filter for the report.
     * @return true is selected, false otherwise
     */
    public boolean fromReportEnabled() {
        return fromDateCheckBox.isSelected();
    }

    /**
     * This method returns a boolean representing the status of the checkbox of the "to"
     * filter for the report.
     * @return true is selected, false otherwise
     */
    public boolean toReportEnabled() {
        return toDateCheckBox.isSelected();
    }

    /**
     * This is the method that displays a welcome message to the user.
     */
    public void welcomeMessage() {
        final JWindow welcomeMessage = new JWindow();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2, true),
                new EmptyBorder(30, 48, 30, 48)
        ));

        JLabel hello = new JLabel(resourceBundle.getString("welcome"), SwingConstants.CENTER);
        hello.setFont(WELCOME_FONT);
        hello.setForeground(TEXT_PRIMARY);
        panel.add(hello, BorderLayout.CENTER);

        welcomeMessage.setBackground(new Color(0, 0, 0, 0));
        welcomeMessage.setContentPane(panel);
        welcomeMessage.pack();
        welcomeMessage.setLocationRelativeTo(null);
        welcomeMessage.setVisible(true);

        Timer timer = new Timer(1200, actionEvent -> welcomeMessage.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SECTION_FONT);
        titleLabel.setForeground(TEXT_SECONDARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createChip(JLabel label) {
        JPanel chip = new JPanel(new BorderLayout());
        chip.setBackground(CARD_COLOR);
        chip.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        label.setForeground(TEXT_PRIMARY);
        chip.add(label, BorderLayout.CENTER);
        return chip;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JCheckBox createCheckBox() {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setForeground(TEXT_PRIMARY);
        checkBox.setFocusable(false);
        return checkBox;
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(SURFACE_COLOR);
        scrollPane.setBackground(SURFACE_COLOR);
    }

    private void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setForeground(TEXT_PRIMARY);
        table.setBackground(SURFACE_COLOR);
        table.setSelectionBackground(new Color(14, 116, 144));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(SECTION_FONT);
        header.setBackground(CARD_COLOR);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable currentTable, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        currentTable, value, isSelected, hasFocus, row, column
                );
                label.setBorder(new EmptyBorder(0, 16, 0, 16));
                label.setFont(BODY_FONT);
                if (isSelected) {
                    label.setBackground(currentTable.getSelectionBackground());
                    label.setForeground(currentTable.getSelectionForeground());
                } else {
                    label.setBackground(row % 2 == 0 ? SURFACE_COLOR : SURFACE_ALT_COLOR);
                    label.setForeground(TEXT_PRIMARY);
                }
                return label;
            }
        };
        table.setDefaultRenderer(Object.class, renderer);
    }

    private void styleDateTimePicker(DateTimePicker dateTimePicker) {
        dateTimePicker.setOpaque(false);
        styleComponentTree(dateTimePicker);
    }

    private void styleComponentTree(Component component) {
        if (component instanceof JPanel) {
            ((JPanel) component).setOpaque(false);
        }
        if (component instanceof JLabel) {
            component.setFont(BODY_FONT);
            component.setForeground(TEXT_PRIMARY);
        }
        if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setFont(BODY_FONT);
            textField.setForeground(TEXT_PRIMARY);
            textField.setCaretColor(TEXT_PRIMARY);
            textField.setBackground(SURFACE_COLOR);
            textField.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(6, 10, 6, 10)
            ));
        }
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setFont(BODY_FONT);
            button.setForeground(TEXT_PRIMARY);
            button.setBackground(SURFACE_ALT_COLOR);
            button.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(6, 10, 6, 10)
            ));
            button.setFocusPainted(false);
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                styleComponentTree(child);
            }
        }
    }

    private static class ModernButton extends JButton {

        private final Color backgroundColor;
        private final Color hoverColor;
        private boolean hovered;

        private ModernButton(String text, Color backgroundColor, Color hoverColor) {
            super(text);
            this.backgroundColor = backgroundColor;
            this.hoverColor = hoverColor;
            setFont(BODY_FONT);
            setForeground(TEXT_PRIMARY);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(14, 20, 14, 20));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(isEnabled() ? (hovered ? hoverColor : backgroundColor) : DISABLED_COLOR);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_ARC_SIZE, BUTTON_ARC_SIZE);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }

}
