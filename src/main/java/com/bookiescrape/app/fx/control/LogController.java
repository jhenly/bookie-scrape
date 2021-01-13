package com.bookiescrape.app.fx.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.fx.log.LogAppender;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


/**
 * The controller class associated with the {@code LogView.fxml} view.
 *
 * @author Jonathan Henly
 */
public class LogController extends MediatableController {
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);
    
    /**************************************************************************
     *                                                                        *
     * FXML Injected Members                                                  *
     *                                                                        *
     *************************************************************************/
    
    @FXML
    private TextArea logTextArea;
    
    
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
     * Log controller's constructor.
     * <p>
     * This constructor is automatically called before this controller's
     * associated view ({@code LogView.fxml}) has been loaded.
     */
    public LogController() {
        System.out.println("LogController::LogController()");
        System.out.println("    logTextArea = " + logTextArea);
    }
    
    /**
     * Log controller's initializer.
     * <p>
     * This method is automatically called after this controller's associated
     * view ({@code LogView.fxml}) has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("LogController::initialize()");
        System.out.println("    logTextArea = " + logTextArea);
        
        LogAppender.setTextArea(logTextArea);
    }
    
    
}
