package com.bookiescrape.app.fx.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.fx.log.LogAppender;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;


/**
 * The controller class associated with the {@code LogView.fxml} view.
 *
 * @author Jonathan Henly
 */
public class LogController extends MediatableController {
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);
    
    private static final String[] LOG_LEVELS = {};
    
    /**************************************************************************
     *                                                                        *
     * FXML Injected Members                                                  *
     *                                                                        *
     *************************************************************************/
    
    @FXML
    private TextArea rawLogTextArea;
    @FXML
    private HBox rawLogHbox;
    @FXML
    private ChoiceBox<String> logLevelChoiceBox;
    
    
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
        System.out.println("    logTextArea = " + rawLogTextArea);
        
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
        System.out.println("    logTextArea = " + rawLogTextArea);
        
        // connect raw log text area with the log appender
        LogAppender.setTextArea(rawLogTextArea);
        
        logLevelChoiceBox.getItems().addAll();
    }
    
    
}
