package controller;

import javax.swing.*;
import java.util.TimerTask;

/**
 * This class updates the timer that is used to count the time of a single task.
 * @author Patrich Tivoli
 */
public class TimerManager extends TimerTask {

    private String clock;
    private JLabel labelClock;

    public TimerManager(String clock, JLabel labelClock){
        this.clock = clock;
        this.labelClock = labelClock;
    }

    /**
     * This method runs the methods to update the {@link String string} of the clock and then the {@link JLabel label} of
     * the clock.
     * @see String
     * @see JLabel
     */
    @Override
    public void run() {
        clock = updateHour();
        labelClock.setText(clock);
    }

    /**
     * This method updates the {@link String string} of the clock.<br>
     * First of all, all the substrings are converted into integers, for substrings it is meant to be
     * seconds, minutes and hours; after updating them, the string is re-built again so that it has the
     * following pattern: HH:mm:ss.
     * @return the string updated of one second with the pattern HH:mm:ss
     */
    private String updateHour() {
        int seconds = getSeconds();
        int minutes = getMinutes();
        int hours = getHours();
        if (++seconds >= 60) {
            minutes++;
            seconds = 0;
        }
        if (minutes >= 60) {
            hours++;
            minutes = 0;
        }
        return composeString(seconds, minutes, hours);
    }

    /**
     * It returns the seconds from a string formatted in this way: HH:mm:ss.<br>
     * Just the {@link String#substring(int, int) substring} method is called, together with
     * the {@link Integer#parseInt(String) parseInt} method in order to get the integer.
     * @return the number of seconds from a string formatted in  this way: HH:mm:ss.
     */
    private int getSeconds(){
        return Integer.parseInt(clock.substring(6,8));
    }

    /**
     * It returns the minutes from a string formatted in this way: HH:mm:ss.<br>
     * Just the {@link String#substring(int, int) substring} method is called, together with
     * the {@link Integer#parseInt(String) parseInt} method in order to get the integer.
     * @return the number of minutes from a string formatted in  this way: HH:mm:ss.
     */
    private int getMinutes(){
        return Integer.parseInt(clock.substring(3,5));
    }

    /**
     * It returns the hours from a string formatted in this way: HH:mm:ss.<br>
     * Just the {@link String#substring(int, int) substring} method is called, together with
     * the {@link Integer#parseInt(String) parseInt} method in order to get the integer.
     * @return the number of hours from a string formatted in  this way: HH:mm:ss.
     */
    private int getHours(){
        return Integer.parseInt(clock.substring(0,2));
    }

    /**
     * This method compose a string formatted in this way: HH:mm:ss from its parameters.<br>
     * The method {@link TimerManager#adapterCiphers(int) adapterCiphers} is used in order to
     * always have a string composed by two characters.
     * @param seconds the number of seconds
     * @param minutes the number of minutes
     * @param hours the number of hours
     * @return a string formatted in this way: HH:mm:ss
     */
    private String composeString(int seconds, int minutes, int hours){
        return adapterCiphers(hours) + ":" + adapterCiphers(minutes) + ":" + adapterCiphers(seconds);
    }

    /**
     * This method takes an integer and builds a string with the value of the integer formatted with
     * two characters.
     * @param number the number to be converted
     * @return the converted string
     */
    private String adapterCiphers(int number) {
        String numberString = "0" + number;
        int lengthNumberString = numberString.length();
        return numberString.substring(lengthNumberString - 2, lengthNumberString);
    }

}
