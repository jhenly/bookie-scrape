package com.bookiescrape.app.util;

import java.util.Objects;

/**
 * Utility class with string related methods.
 * 
 * @author Jonathan Henly
 */
public final class StringUtils {
    
    /** Utility class, don't subclass this class. */
    private StringUtils() {}
    
    /**************************************************************************
     *                                                                        *
     * Static API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Capitalizes the first alphabetic character in a specified string.
     * 
     * @param string - the string to capitalize the first alphabetic
     *        character of
     * @return the specified string with its first alphabetic character
     *         capitalized
     * @throws NullPointerException if the specified string is {@code null}
     */
    public static String capitalizeFirstAlphabeticChar(String string) {
        String nonNullString = getNonNullStringOrThrow(string);
        
        return capitalizeFirstAlphabeticChar(nonNullString.toCharArray());
    }
    
    /** Helper that capitalizes the first char in a char array. */
    private static String capitalizeFirstAlphabeticChar(char[] string) {
        int firstCharIndex = firstAlphabeticIndex(string);
        
        // return string if it's empty or blank
        if (firstCharIndex == -1) { return String.valueOf(string); }
        
        return capitalizeChar(string, firstCharIndex);
    }
    
    /** Helper that capitalizes a char at a given index in a string. */
    private static String capitalizeChar(char[] string, int index) {
        // capitalize char at index in string
        string[index] = Character.toUpperCase(string[index]);
        return String.valueOf(string);
    }
    
    /**
     * Gets the frist non whitespace character's index or -1 if the specified
     * string is {@linkplain String#isEmpty() empty} or
     * {@linkplain String#isBlank() blank}.
     * 
     * @param string - the string to find the first non whitespace character's
     *        index
     * @return the first non whitespace character index or -1
     * @throws NullPointerException if the specified string is {@code null}
     */
    public static int firstNonWhitespaceIndex(String string) {
        String nonNullString = getNonNullStringOrThrow(string);
        
        return firstNonWhitespaceIndex(nonNullString.toCharArray());
    }
    
    /** Returns the first non whitespace index or -1 if string is blank. */
    private static int firstNonWhitespaceIndex(char[] string) {
        for (int i = 0; i < string.length; ++i) {
            if (!Character.isWhitespace(string[i])) { return i; }
        }
        
        return -1;
    }
    
    /**
     * Gets the frist whitespace character's index or -1 if the specified
     * string contains no whitespace characters.
     * 
     * @param string - the string to find the first whitespace character's
     *        index
     * @return the first whitespace character index or -1
     * @throws NullPointerException if the specified string is {@code null}
     */
    public static int firstWhitespaceIndex(String string) {
        String nonNullString = getNonNullStringOrThrow(string);
        
        return firstWhitespaceIndex(nonNullString.toCharArray());
    }
    
    /** Returns the first whitespace index or -1. */
    private static int firstWhitespaceIndex(char[] string) {
        for (int i = 0; i < string.length; ++i) {
            if (Character.isWhitespace(string[i])) { return i; }
        }
        
        return -1;
    }
    
    /**
     * Gets the first alphabetic ({@code A-Za-z}) character index or -1 if
     * the string contains no alphabetic characters.
     * 
     * @param string - the string to find the first alphabetic character index
     *        of
     * @return the first alphabetic ({@code A-Za-z}) character index or -1
     * @throws NullPointerException if the specified string is {@code null}
     */
    public static int firstAlphabeticIndex(String string) {
        String nonNullString = getNonNullStringOrThrow(string);
        
        return firstAlphabeticIndex(nonNullString.toCharArray());
    }
    
    /** Returns the first alphabetic char index or -1 if string is blank. */
    private static int firstAlphabeticIndex(char[] string) {
        for (int i = 0; i < string.length; ++i) {
            if (Character.isLetter(string[i])) { return i; }
        }
        
        return -1;
    }
    
    /**
     * Gets the first numeric ({@code 0-9}) character index or -1 if
     * the string contains no numeric characters.
     * 
     * @param string - the string to find the first numeric character index
     *        of
     * @return the first numeric ({@code 0-9}) character index or -1
     * @throws NullPointerException if the specified string is {@code null}
     */
    public static int firstNumericIndex(String string) {
        String nonNullString = getNonNullStringOrThrow(string);
        
        return firstNumericIndex(nonNullString.toCharArray());
    }
    
    /** Returns the first numeric char index or -1 if string is blank. */
    private static int firstNumericIndex(char[] string) {
        for (int i = 0; i < string.length; ++i) {
            if (Character.isDigit(string[i])) { return i; }
        }
        
        return -1;
    }
    
    /** Helper that returns the specified string or throws an NPE. */
    private static String getNonNullStringOrThrow(String string) {
        return Objects.requireNonNull(string, "string cannot be null");
    }
    
}
