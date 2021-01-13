package com.bookiescrape.app.fx.log;

import java.util.function.Consumer;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.scene.control.TextArea;

/**
 * Log4J {@code WriterAppender} implementation that appends
 * {@code LoggingEvent} messages to the log view's logging text area.
 * 
 * @author Jonathan Henly
 */
public class LogAppender extends WriterAppender {
    
    private static volatile TextArea textArea;
    
    // start out appending logging event messages to a string builder
    private static volatile StringBuilder preLoadLogMsgQueue = new StringBuilder();
    private static volatile Consumer<String> currentAppendMethod = preLoadLogMsgQueue::append;
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    @Override
    public void append(final LoggingEvent loggingEvent) {
        /* if the log text area has not been loaded, then append the logging event
         * message to the pre log string builder, otherwise append the logging event
         * message to the log text area */
        currentAppendMethod.accept(this.layout.format(loggingEvent));
    }
    
    /** 
     * Sets the logging event text area where logging messages are appended.
     * <p>
     * This method also appends any queued logging event messages that occurred
     * prior to invoking this method.
     * @param textArea - where the logs are appended to
     */
    public static void setTextArea(final TextArea textArea) {
        LogAppender.textArea = textArea;
        
        // append any queued logging event messages to the specified text area
        appendToTextArea(preLoadLogMsgQueue.toString());
        preLoadLogMsgQueue.setLength(0);
        preLoadLogMsgQueue = null;
        
        // switch to appending logging event messages to the log text area
        currentAppendMethod = LogAppender::appendToTextArea;
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /** Appends a logging event message to the end of the log text area. */
    private static void appendToTextArea(final String message) {
        textArea.selectEnd();
        textArea.insertText(textArea.getText().length(), message);
    }
    
    
} // class LogAppender
