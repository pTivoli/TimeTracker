package com.time.tracker;

import controller.Controller;
import view.View;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is the Main class in which everything starts.
 * @author Patrich Tivoli
 */
public class Main {

    /**
     * This is the main method in which everything starts. <br>
     * The Locale taken is the default one, so that the program adapts its language
     * to the os' language. <br>
     * After initializing the ResourceBundle the View and the Controller are initialized and started.
     * @param args Arguments that may come from a terminal
     * @see Locale
     * @see ResourceBundle
     * @see View
     * @see Controller
     */
    public static void main(String[] args) {
        Locale locale = Locale.getDefault();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundle", locale);
        View view = new View(resourceBundle);
        new Controller(resourceBundle, view);
    }

}