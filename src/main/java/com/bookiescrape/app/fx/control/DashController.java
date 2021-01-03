package com.bookiescrape.app.fx.control;

import javafx.fxml.FXML;


/**
 * The controller class for {@code DashboardLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class DashController extends MediatableController {
    
    /**
     * Constructor is called before the initialize() method.
     */
    public DashController() {}
    
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
    
}
