package com.bookiescrape.app.fx.control;

import com.bookiescrape.app.sample.Main;

import javafx.fxml.FXML;


/**
 * The controller class for {@code SettingsLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class Settings {

    // reference to main application
    private Main main;

    /**
     * Default empty constructor used by fxml.
     */
    public Settings() {}
    
    /**
     * Initializes the default controller class.
     * <p>
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        
    }
    
    /**
     * Called by the main application to give a reference to itself.
     *
     * @param mainRef - reference to Main's controller
     */
    public void setMain(Main mainRef) {
        main = mainRef;

        // add observable list data to the table
        // personTable.setItems(mainApp.getPersonData());
    }

}
