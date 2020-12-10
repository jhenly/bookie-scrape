package com.bookiescrape.app.fx.control;

import com.bookiescrape.app.sample.Main;

import javafx.fxml.FXML;


/**
 * The controller class for {@code DashboardLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class Dashboard {
    
    // reference to the main application.
    private Main main;
    
    /**
     * Constructor is called before the initialize() method.
     */
    public Dashboard() {}
    
    /**
     * Initializes the default controller class.
     * <p>
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // initialize the person table with the two columns.

        // firstNameColumn.setCellValueFactory(cellData ->
        // cellData.getValue().firstNameProperty());
        // lastNameColumn.setCellValueFactory(cellData ->
        // cellData.getValue().lastNameProperty());
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
