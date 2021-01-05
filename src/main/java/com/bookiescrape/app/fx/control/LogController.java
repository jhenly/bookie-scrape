package com.bookiescrape.app.fx.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.fx.log.LogAppender;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


/**
 * The controller class for {@code LogLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class LogController extends MediatableController {
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);
    
    @FXML
    private TextArea logTextArea;
    
    /**
     * Initializes the default controller class.
     * <p>
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        LogAppender.setTextArea(logTextArea);
    }
    
    /** Default empty constructor used by fxml. */
    public LogController() {}
    
}
