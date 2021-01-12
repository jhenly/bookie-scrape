package com.bookiescrape.app.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bookiescrape.app.util.SettingsUtilsTest.CommaDelimitedStringToList;
import com.bookiescrape.app.util.SettingsUtilsTest.ListToCommaDelimitedString;


@RunWith(Suite.class)
@SuiteClasses({ ListToCommaDelimitedString.class, CommaDelimitedStringToList.class })
public class SettingsUtilsTest {

    private static List<String> EMPTY_LIST;
    private static String EMPTY_STRING;

    private static List<String> NULL_LIST;
    private static String NULL_STRING;
    
    private static String COMMA_AND_WHITESPACE_STRING;

    private static List<String> LIST_OF_STRINGS;
    private static String COMMA_DELIM_STRING_IN;
    private static String COMMA_DELIM_STRING_OUT;
    
    private static List<String> LIST_OF_STRINGS_WITH_NULLS;
    private static String COMMA_DELIM_STRING_FROM_LIST_OF_STRINGS_WITH_NULLS;
    
    private static List<Object> LIST_OF_OBJECTS_WITH_NULLS;
    private static String COMMA_DELIM_STRING_FROM_LIST_OF_OBJECTS_WITH_NULLS;

    private static List<Object> LIST_OF_OBJECTS_WITH_NULLS_AND_EMPTY_STRINGS;
    private static String COMMA_DELIM_STRING_FROM_LIST_OF_OBJECTS_WITH_NULLS_AND_EMPTY_STRINGS;
    
    /** Runs once before any tests, initializes static test variables. */
    @BeforeClass
    public static void setUpOnceFirst() {
        EMPTY_LIST = Collections.emptyList();
        EMPTY_STRING = "";
        
        NULL_LIST = null;
        NULL_STRING = null;

        COMMA_AND_WHITESPACE_STRING = ",\t\t,,\n, ,   ,, ,,  ,,\t  ,,\r, ,,,";

        // initialize list of strings and matching comma delimited string
        String a = "a", boo = "boo", foo = "foo", doo = "doo", goo = "goo", loo = "loo";
        LIST_OF_STRINGS = List.of(a, boo, foo, doo, goo, loo);
        COMMA_DELIM_STRING_IN = a + ',' + boo + " , , , " + foo + "," + doo + ", " + goo + " , " + loo + ",, ,";
        COMMA_DELIM_STRING_OUT = String.format("%s,%s,%s,%s,%s,%s", a, boo, foo, doo, goo, loo);

        // initialize list of strings with nulls and matching comma delimited string
        LIST_OF_STRINGS_WITH_NULLS = Arrays.asList(new String[] { null, boo, foo, null, goo, loo, null });
        COMMA_DELIM_STRING_FROM_LIST_OF_STRINGS_WITH_NULLS = String.format("%s,%s,%s,%s", boo, foo, goo, loo);

        // initialize list of objects with nulls and matching comma delimited string
        Integer num = 123;
        Character car = 'c';
        LIST_OF_OBJECTS_WITH_NULLS = Arrays.asList(new Object[] { null, boo, car, foo, null, num, loo, null });
        COMMA_DELIM_STRING_FROM_LIST_OF_OBJECTS_WITH_NULLS =
        String.format("%s,%s,%s,%s,%s", boo, car.toString(), foo, num.toString(), loo);

        // initialize list of objects with nulls and empty string and matching comma
        // delimited string
        LIST_OF_OBJECTS_WITH_NULLS_AND_EMPTY_STRINGS =
        Arrays.asList(new Object[]
        { null, boo, car, "", foo, null, num, "", loo, null });
        COMMA_DELIM_STRING_FROM_LIST_OF_OBJECTS_WITH_NULLS_AND_EMPTY_STRINGS =
        String.format("%s,%s,%s,%s,%s,%s,%s", boo, car.toString(), "", foo, num.toString(), "", loo);
    }
    
    
    /**
     * Tests the {@link SettingsUtils#listToCommaDelimitedString(List)} method.
     */
    public static class ListToCommaDelimitedString {
        
        @Test
        public void passing_a_null_or_empty_list_should_return_an_empty_string() {
            assertEquals(SettingsUtils.listToCommaDelimitedString(NULL_LIST), EMPTY_STRING);
            assertEquals(SettingsUtils.listToCommaDelimitedString(EMPTY_LIST), EMPTY_STRING);
        }

        @Test
        public void passing_valid_list_of_strings_should_return_valid_comma_delimited_string() {
            String commaDelimStr = SettingsUtils.listToCommaDelimitedString(LIST_OF_STRINGS);
            assertEquals(commaDelimStr, COMMA_DELIM_STRING_OUT);
        }

        @Test
        public void passing_valid_list_of_strings_with_nulls_should_return_valid_comma_delimited_string() {
            String commaDelimStr = SettingsUtils.listToCommaDelimitedString(LIST_OF_STRINGS_WITH_NULLS);
            assertEquals(commaDelimStr, COMMA_DELIM_STRING_FROM_LIST_OF_STRINGS_WITH_NULLS);
        }

        @Test
        public void passing_valid_list_of_objects_with_nulls_should_return_valid_comma_delimited_string() {
            String commaDelimStr = SettingsUtils.listToCommaDelimitedString(LIST_OF_OBJECTS_WITH_NULLS);
            assertEquals(commaDelimStr, COMMA_DELIM_STRING_FROM_LIST_OF_OBJECTS_WITH_NULLS);
        }
        
        @Test
        public void
        passing_valid_list_of_objects_with_nulls_and_empty_strings_should_return_valid_comma_delimited_string()
        {
            String commaDelimStr =
            SettingsUtils.listToCommaDelimitedString(LIST_OF_OBJECTS_WITH_NULLS_AND_EMPTY_STRINGS);
            assertEquals(commaDelimStr, COMMA_DELIM_STRING_FROM_LIST_OF_OBJECTS_WITH_NULLS_AND_EMPTY_STRINGS);
        }

    } // class ListToCommaDelimitedString


    /**
     * Tests the {@link SettingsUtils#commaDelimitedStringToList(String)}
     * method.
     */
    public static class CommaDelimitedStringToList {

        @Test
        public void passing_a_null_or_empty_string_should_return_an_empty_list() {
            assertEquals(SettingsUtils.commaDelimitedStringToList(NULL_STRING), EMPTY_LIST);
            assertEquals(SettingsUtils.commaDelimitedStringToList(EMPTY_STRING), EMPTY_LIST);
        }
        
        @Test
        public void passing_valid_comma_delimited_string_should_return_valid_list_of_strings() {
            List<String> list = SettingsUtils.commaDelimitedStringToList(COMMA_DELIM_STRING_OUT);
            assertEquals(list, LIST_OF_STRINGS);
        }

        @Test
        public void passing_rough_but_valid_comma_delimited_string_should_return_valid_list_of_strings() {
            List<String> list = SettingsUtils.commaDelimitedStringToList(COMMA_DELIM_STRING_IN);
            assertEquals(list, LIST_OF_STRINGS);
        }
        
        @Test
        public void passing_a_string_with_only_commas_and_whitespace_returns_an_empty_list() {
            List<String> list = SettingsUtils.commaDelimitedStringToList(COMMA_AND_WHITESPACE_STRING);
            assertEquals(list, EMPTY_LIST);
        }
        
    } // class CommaDelimitedStringToList


}
