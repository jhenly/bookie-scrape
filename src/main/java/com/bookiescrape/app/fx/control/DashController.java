package com.bookiescrape.app.fx.control;

import javafx.fxml.FXML;


/**
 * The controller class associated with the {@code DashView.fxml} view.
 *
 * @author Jonathan Henly
 */
public class DashController extends MediatableController {
    
    /**************************************************************************
     *                                                                        *
     * FXML Injected Members                                                  *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Private Members                                                        *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s) / Initializer                                           *
     *                                                                        *
     *************************************************************************/
    
    /** 
     * Dashboard controller's constructor.
     * <p>
     * This constructor is automatically called before this controller's
     * associated view ({@code DashView.fxml}) has been loaded.
     */
    public DashController() {}
    
    /**
     * Dashboard controller's initializer.
     * <p>
     * This method is automatically called after the dashboard controller's
     * associated view ({@code DashView.fxml}) has been loaded.
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
