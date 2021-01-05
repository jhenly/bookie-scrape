package com.bookiescrape.app.fx.log;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.scene.control.TextArea;

/**
 * Log4J appender implementation.
 * 
 * @author Jonathan Henly
 */
public class LogAppender extends WriterAppender {
    
    private static volatile TextArea textArea;
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    
    /** 
     * Sets the text area where logging is written to.
     * @param textArea - where the logs are written to
     */
    public static void setTextArea(final TextArea textArea) { LogAppender.textArea = textArea; }
    
    @Override
    public void append(final LoggingEvent loggingEvent) {
        String message = this.layout.format(loggingEvent);
        if (textArea != null) {
            textArea.selectEnd();
            textArea.insertText(textArea.getText().length(), message);
        }
    }
    
    
}
