package com.bookiescrape.app.fx.control;

import java.util.List;

import org.slf4j.Logger;

import com.bookiescrape.app.fx.log.LogAppender;
import com.bookiescrape.app.util.LogUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * The controller class associated with the {@code LogView.fxml} view.
 *
 * @author Jonathan Henly
 */
public class LogController extends MediatableController {
    private static final Logger LOG = LogUtils.getLogger(LogController.class);
    
    private static final List<String> LOG_LEVELS = LogUtils.LogLevel.getLogLevels();
    private static final String RAW_LOG_OUTPUT = "Raw";
    
    /**************************************************************************
     *                                                                        *
     * FXML Injected Members                                                  *
     *                                                                        *
     *************************************************************************/
    
    @FXML private TextArea rawLogTextArea;
    @FXML private VBox logAreaVbox;
    @FXML private HBox rawLogHbox;
    @FXML private Label filterByLabel;
    @FXML private ComboBox<String> filterByComboBox;
    
    
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
        
        // add log levels and raw log output to log view's choice box
        filterByComboBox.getItems().add(RAW_LOG_OUTPUT);
        filterByComboBox.getItems().addAll(LOG_LEVELS);
    }
    
    
}
