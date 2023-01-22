package view;

import javax.swing.*;

/**
 * This class is used to display messages to the user.
 * @author Patrich Tivoli
 */
public class ErrorMessage {

    private JFrame frame;

    /**
     * This constructor sets the frame for this class:
     * in order to show the message the frame is needed.
     * @param frame a frame
     * @see JFrame
     */
    public ErrorMessage(JFrame frame) {
        this.frame = frame;
    }

    /**
     * This method shows a message to the user.
     * @param title title of the window
     * @param message message to display
     * @param optionPane if it is an error, warning or info
     * @see JFrame
     * @see JOptionPane
     */
    private void showMessage(String title, String message, int optionPane) {
        JOptionPane.showMessageDialog(frame,
                message,
                title,
                optionPane);
    }

    /**
     * This method shows an error.
     * @param title the title of the error
     * @param message the message of the error
     * @see JFrame
     * @see JOptionPane
     */
    public void showError(String title, String message) {
        this.showMessage(title, message, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method shows a warning.
     * @param title the title of the warning
     * @param message the message of the warning
     * @see JFrame
     * @see JOptionPane
     */
    public void showWarning(String title, String message) {
        this.showMessage(title, message, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * This method shows an info.
     * @param title the title of the info
     * @param message the message of the info
     * @see JFrame
     * @see JOptionPane
     */
    public void showInfo(String title, String message) {
        this.showMessage(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

}
