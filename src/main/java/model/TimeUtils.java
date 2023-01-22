package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This class is used to manipulate things related to the Time.
 * @author Patrich Tivoli
 */
public class TimeUtils {

    /**
     * Private constructor to hide the implicit one.
     */
    private TimeUtils() { }

    /**
     * This method returns a string structured in this way: YYYY/MM/DD HH:mm:ss
     * from a {@link LocalDateTime LocalDateTime}.
     * @param localDateTime the object that needs to be converted
     * @return a string structured in this way: YYYY/MM/DD HH:mm:ss
     */
    public static String getFancyDateTime(LocalDateTime localDateTime) {
        return
                getFancyDate(localDateTime.toLocalDate()) + " " +
                getFancyTime(localDateTime.toLocalTime());
    }

    /**
     * This method returns a string structured in this way: YYYY/MM/DD
     * from a {@link LocalDate LocalDate}.
     * @param localDate the object that needs to be converted
     * @return a string structured in this way: YYYY/MM/DD
     */
    public static String getFancyDate(LocalDate localDate) {
        return
                localDate.getYear() + "/" +
                adapterCiphers(localDate.getMonthValue()) + "/" +
                adapterCiphers(localDate.getDayOfMonth());
    }

    /**
     * This method returns a string structured in this way: HH:mm:ss
     * from a {@link LocalTime LocalTime}.
     * @param localTime the object that needs to be converted
     * @return a string structured in this way: HH:mm:ss
     */
    public static String getFancyTime(LocalTime localTime) {
        return
                adapterCiphers(localTime.getHour()) + ":" +
                adapterCiphers(localTime.getMinute()) + ":" +
                adapterCiphers(localTime.getSecond());
    }

    /**
     * This method makes a string from an integer, the resultant string will
     * be composed by two characters. Since it's just needed for the time there's no need
     * to have more than two characters.<br>
     * Examples:<br>
     * <ul>
     *     <li>Input: 0 Output: "00"</li>
     *     <li>Input: 1 Output: "01"</li>
     *     <li>Input: 12 Output: "12"</li>
     *     <li>Input: 13 Output: "13"</li>
     *     <li>Input: 123 Output: "23"</li>
     * </ul>
     * Because of this, tasks that last longer than
     * 99 hours, 59 minutes and 59 seconds are not supported by this program.
     * @param number the integer that need to be converted
     * @return the string representing the integer but with two characters
     */
    private static String adapterCiphers(int number) {
        String numberString = "0" + number;
        int lengthNumberString = numberString.length();
        return numberString.substring(lengthNumberString - 2, lengthNumberString);
    }

}
