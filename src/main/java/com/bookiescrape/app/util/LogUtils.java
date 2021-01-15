package com.bookiescrape.app.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class with logging related methods.
 * 
 * @author Jonathan Henly
 * 
 * @see LogUtils.LogLevel
 * @see LogUtils#getLogger(Class)
 * @see LogUtils#getLogger(String)
 */
public final class LogUtils {
    
    
    /** Utility class, don't subclass this class. */
    private LogUtils() {}
    
    /**
     * Enum representing the different log levels.
     * @see LogLevel#TRACE
     * @see LogLevel#INFO
     * @see LogLevel#DEBUG
     * @see LogLevel#WARN
     * @see LogLevel#ERROR
     */
    public static enum LogLevel {
        /** Represents the trace log level. */
        TRACE("trace"),
        /** Represents the info log level. */
        INFO("info"),
        /** Represents the debug log level. */
        DEBUG("debug"),
        /** Represents the warning log level. */
        WARN("warn"),
        /** Represents the error log level. */
        ERROR("error");
        
        
        private static List<String> logLevels;
        
        private String level;
        /** Constructs a LogLevel enum. */
        private LogLevel(String level) { this.level = level; }
        
        /**
         * Gets this enum's log level as a string.
         * @return - this enum's log level
         */
        public String getLevel() { return level; }
        
        /**
         * Gets the string representations of the {@code LogLevel} enums, with
         * their first letter capitalized.
         * @return a list containing the string representations of the
         *         {@code LogLevel} enums with their first letter capitalized
         */
        public static final List<String> getLogLevels() {
            if (logLevels == null) {
                logLevels = Arrays.stream(LogLevel.values())
                    .map(level -> StringUtils.capitalizeFirstAlphabeticChar(level.getLevel()))
                    .collect(Collectors.toList());
            }
            
            return logLevels;
        }
        
    } // enum LogLevel
    
    
    /**************************************************************************
     *                                                                        *
     * Static API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets a logger named after the specified class, using
     * {@code LoggerFactory.getLogger(clazz)}.
     * 
     * @param clazz - the class to name the logger after
     * @return the logger named after the specified class
     * @see LoggerFactory#getLogger(Class)
     */
    public static Logger getLogger(final Class<?> clazz) { return LoggerFactory.getLogger(clazz); }
    
    /**
     * Gets a logger named after the specified string, using
     * {@code LoggerFactory.getLogger(name)}.
     * 
     * @param name - what to name the logger
     * @return the logger named after the specified string
     * @see LoggerFactory#getLogger(String)
     */
    public static Logger getLogger(final String name) { return LoggerFactory.getLogger(name); }
    
}
