package controller;

import model.TimeUtils;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * This class has just one task to do, updating the clock of the program.<br>
 * It extends {@link TimerTask TimerTask} so that can be executed as a
 * {@link java.util.Timer Timer} Thread.
 * @author Patrich Tivoli
 */
public class ClockManager extends TimerTask {

    private JLabel clock;

    public ClockManager(JLabel clock) {
        this.clock = clock;
    }

    /**
     * Here in this method, the clock is updated with the current time and date that are taken from
     * the {@link LocalDateTime#now() now} method.<br>
     * The method {@link TimeUtils#getFancyDateTime(LocalDateTime) getFancyDateTime} is used to convert
     * the original {@link LocalDateTime#toString() toString()} method output in a more fancy {@link String string}.
     * @see TimeUtils
     * @see LocalDateTime
     * @see JLabel
     */
    @Override
    public void run() {
        clock.setText(TimeUtils.getFancyDateTime(LocalDateTime.now()));
    }

}
