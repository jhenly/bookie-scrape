package com.bookiescrape.app.fx.control;

import com.bookiescrape.app.sample.Main;


public class Settings {
    
    // reference to main application
    private Main main;
    
    public Settings() {}

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
