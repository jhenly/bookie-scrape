package com.bookiescrape.app.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * Creates a comma delimited string from the elements in a specified list.
     * <p>
     * If the specified list is {@code null} or
     * {@linkplain List#isEmpty() is empty}, then an empty string ({@code ""})
     * will be returned.
     * <p>
     * If the specified list contains any {@code null} elements, then they
     * will be skipped over. If the list contains any objects for
     * which invoking their {@linkplain Object#toString() toString()} method
     * results in an empty string ({@code ""}), then the empty string will be
     * included in the resulting comma delimited string. An example of this
     * can be found below.
     * <p>
     * This method does not append a trailing comma after the last element in
     * the specified list. The only case in which the returned string will have
     * a trailing comma, is when the result of invoking
     * {@linkplain Object#toString() toString()} on the last element in the
     * specified list, is a string containing a trailing comma.
     * <p>
     * Example lists:
     * <pre> List&lt;Object&gt; one = Arrays.asList(new Object[] { "boo", "foo" });
     *
     * String trailingComma = "woo,"; // string has a trailing comma
     * List&lt;Object&gt; two = Arrays.asList(new Object[] { "boo", "foo", trailingComma });
     *
     * List&lt;Object&gt; three = Arrays.asList(new Object[] { "boo", null, "", "foo", null });</pre>
     * <p>
     * Results of calling this method with the three example lists above:
     * <pre><table class="plain">
     * <thead>
     * <tr>
     *  <th scope="col" style="text-align:right; padding:0 10 0 0">List</th>
     *  <th scope="col" style="text-align:left">Result</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr><th scope="row" style="font-weight:normal; text-align:right; padding:0 10 0 0">{@code one}</th>
     *     <td style="text-align:left">{@code "boo,foo"}</td></tr>
     * <tr><th scope="row" style="font-weight:normal; text-align:right; padding:0 10 0 0">{@code two}</th>
     *     <td style="text-align:left">{@code "boo,foo,woo,"}</td></tr>
     * <tr><th scope="row" style="font-weight:normal; text-align:right; padding:0 10 0 0">{@code three}</th>
     *     <td style="text-align:left">{@code "boo,,foo"}</td></tr>
     * </tbody>
     * </table></pre>
     *
     * @param list - the list to create the comma delimited string from
     * @return a string comprised of the string representations of the elements
     *         in the specified list, delimited by commas
     */
    public static String listToCommaDelimitedString(List<?> list) {
        return (list == null || list.isEmpty()) ? ""
            : list.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(","));
    }
    
    
    /**
     * Returns a list of strings created from splitting a specified comma
     * delimited string.
     * <p>
     * If the specified comma delimited string is {@code null} or
     * {@linkplain String#isBlank() blank}, then an empty list will be returned
     * via {@linkplain Collections#emptyList()}.
     * <p>
     * If an empty string ({@code ""}) results from invoking
     * {@linkplain String#strip() strip()} on a string split from the specified
     * comma delimited string, then nothing will be added to the list and the
     * next split string will be processed.
     *
     * @param cds - the comma delimited string to split and create the list from
     * @return a list containing the strings split from the specified comma
     *         delimited string
     */
    public static List<String> commaDelimitedStringToList(String cds) {
        if (cds == null || cds.isBlank())
            return Collections.emptyList();
        
        /* iterate over the strings split from the comma delimited string, remove
         * leading and trailing whitespace, add them to the list if they are not
         * blank */
        return Arrays.stream(cds.split(",")).map(String::strip).filter(str -> !str.isBlank())
            .collect(Collectors.toList());
    }
    
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
