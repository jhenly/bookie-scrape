package com.bookiescrape.app.fx.control;

import java.util.ResourceBundle;

import com.bookiescrape.app.sample.Main;

import javafx.fxml.FXML;


public class Dashboard {

    @FXML
    private ResourceBundle resources;

    // reference to the main application.
    private Main main;

    /**
     * Constructor is called before the initialize() method.
     */
    Dashboard() {}

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
