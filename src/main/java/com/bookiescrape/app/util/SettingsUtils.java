package com.bookiescrape.app.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Utility class with static convenience methods.
 *
 * @author Jonathan Henly
 */
public final class SettingsUtils {
    
    /** Don't subclass this utility class. */
    private SettingsUtils() {}

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
        if (cds == null || cds.isBlank()) return Collections.emptyList();
        
        /* iterate over the strings split from the comma delimited string, remove
         * leading and trailing whitespace, add them to the list if they are not
         * blank */
        return Arrays.stream(cds.split(",")).map(String::strip).filter(str -> !str.isBlank())
            .collect(Collectors.toList());
    }
    
}
