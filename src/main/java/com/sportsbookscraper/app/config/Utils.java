package com.sportsbookscraper.app.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class with static convenience methods and static constants.
 * 
 * @author Jonathan Henly
 */
final class Utils {
    
    private Utils() {}
    
    /**
     * Creates a comma delimited string from the elements in a specified list.
     * <p>
     * This method does not append a trailing comma after the last element in
     * the specified list. The only case in which the returned string will have
     * a trailing comma, is when the result of invoking {@code toString()} on
     * the last element in the specified list, is a string containing a trailing
     * comma.
     * <p>
     * If the specified list contains no elements, then the empty string
     * ({@code ""}) will be returned.
     * 
     * 
     * @param list
     *             - the list to create the comma delimited string from
     * @return a string comprised of the string representations of the elements
     *         in the specified list, delimited by commas
     */
    static String listToCommaDelimitedString(List<?> list) {
        if (list.isEmpty()) { return ""; }
        
        String delimitedString = list.get(0).toString();
        for (int i = 1; i < list.size(); i++) {
            // append a comma to the previous element
            delimitedString += ',' + list.get(i).toString();
        }
        
        return delimitedString;
    }
    
    /**
     * Creates a list of strings from the splitting of a specified comma
     * delimited string.
     * <p>
     * <b>Note:</b> if the empty string ({@code ""}) results from invoking
     * {@linkplain String#strip() strip()} on a string split from the specified
     * comma delimited string, then nothing will be added to the list and the
     * next split string will be processed.
     * <p>
     * If the specified comma delimited string is empty or it only contains
     * whitespace characters and commas, then an empty list will be returned via
     * {@linkplain Collections#emptyList()}.
     * 
     * 
     * @param commaDelimStr
     *                      - the comma delimited string to create the list from
     * @return a list containing the strings split from the specified comma
     *         delimited string
     */
    static final List<String> commaDelimitedStringToList(String commaDelimStr) {
        String[] strs = commaDelimStr.split(",");
        
        if (strs.length == 0) { return Collections.emptyList(); }
        
        List<String> ret = new ArrayList<>();
        
        // iterate over strs, remove trail/leading whitespace and add to
        // list if not an empty string
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i].strip();
            if (!str.isEmpty()) {
                ret.add(str);
            }
        }
        
        return ret;
    }
    
}
