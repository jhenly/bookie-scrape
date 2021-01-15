package com.bookiescrape.app.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bookiescrape.app.util.StringUtilsTest.CapitalizeFirstAlphabeticChar;
import com.bookiescrape.app.util.StringUtilsTest.FirstAlphabeticIndex;
import com.bookiescrape.app.util.StringUtilsTest.FirstNonWhitespaceIndex;
import com.bookiescrape.app.util.StringUtilsTest.FirstNumericIndex;
import com.bookiescrape.app.util.StringUtilsTest.FirstWhitespaceIndex;

@RunWith(Suite.class)
@SuiteClasses({ CapitalizeFirstAlphabeticChar.class, FirstAlphabeticIndex.class, FirstNumericIndex.class,
                FirstNonWhitespaceIndex.class, FirstWhitespaceIndex.class })
public class StringUtilsTest {
    
    private static final String NULL_STRING = null;
    private static final String EMPTY_STRING = "";
    private static final String WHITESPACE_STRING = " \t\r\n";
    private static final String ALPHA_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUM_STRING = "0123456789";
    private static final String SYMBOLS_STRING = "[{()}]!@#$%^&*_-+=`~\"',./<>?\\|;:";
    
    private static final int NOT_FOUND = -1;
    
    
    /** Tests the {@linkplain StringUtils#capitalizeFirstAlphabeticChar(String)} method. */
    public static class CapitalizeFirstAlphabeticChar {
        
        private static final String STRING_TO_CAP = " \t1234string";
        private static final String CAPPED_STRING = " \t1234String";
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void passing_an_empty_string_should_return_an_empty_string() throws IOException {
            assertEquals(EMPTY_STRING, call(EMPTY_STRING));
        }
        
        @Test
        public void passing_a_string_containing_only_whitespace_should_return_the_same_string() throws IOException {
            assertEquals(WHITESPACE_STRING, call(WHITESPACE_STRING));
        }
        
        @Test
        public void passing_a_string_containing_only_numbers_should_return_the_same_string() throws IOException {
            assertEquals(NUM_STRING, call(NUM_STRING));
        }
        
        @Test
        public void passing_a_string_containing_only_symbols_should_return_the_same_string() throws IOException {
            assertEquals(SYMBOLS_STRING, call(SYMBOLS_STRING));
        }
        
        @Test
        public void passing_string_with_whitespace_numbers_and_letters_should_return_string_with_first_letter_capitalized() {
            assertEquals(CAPPED_STRING, call(STRING_TO_CAP));
        }
        
        private String call(String string) {
            return StringUtils.capitalizeFirstAlphabeticChar(string);
        }
        
    } // class CapitalizeFirstAlphabeticChar
    
    
    /** Tests the {@linkplain StringUtils#firstAlphabeticIndex(String)} method. */
    public static class FirstAlphabeticIndex {
        
        private static final int INDEX_FOUR = 4;
        private static final String ALPHA_AT_INDEX_FOUR = " 2\t_a";
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void passing_an_empty_string_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        @Test
        public void passing_string_containing_only_whitespace_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(WHITESPACE_STRING));
        }
        
        @Test
        public void passing_string_containing_only_numbers_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(NUM_STRING));
        }
        
        @Test
        public void passing_string_containing_only_symbols_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(SYMBOLS_STRING));
        }
        
        @Test
        public void passing_string_with_alpha_at_index_four_should_return_index_four() throws IOException {
            assertEquals(INDEX_FOUR, call(ALPHA_AT_INDEX_FOUR));
        }
        
        
        private int call(String string) {
            return StringUtils.firstAlphabeticIndex(string);
        }
        
    } // class FirstAlphabeticIndex
    
    
    /** Tests the {@linkplain StringUtils#firstNumericIndex(String)} method. */
    public static class FirstNumericIndex {
        private static final int INDEX_FOUR = 4;
        private static final String NUMBER_AT_INDEX_FOUR = "a b@1 jds5";
        
        @Test
        public void passing_string_with_number_at_index_four_should_return_index_four() throws IOException {
            assertEquals(INDEX_FOUR, call(NUMBER_AT_INDEX_FOUR));
        }
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void passing_an_empty_string_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        @Test
        public void passing_string_containing_only_whitespace_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(WHITESPACE_STRING));
        }
        
        @Test
        public void passing_string_containing_only_letters_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(ALPHA_STRING));
        }
        
        @Test
        public void passing_string_containing_only_symbols_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(SYMBOLS_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstNumericIndex(string);
        }
        
    } // class FirstNumericIndex
    
    
    /** Tests the {@linkplain StringUtils#firstNonWhitespaceIndex(String)} method. */
    public static class FirstNonWhitespaceIndex {
        private static final int INDEX_FOUR = 4;
        private static final String NON_WHITE_SPACE_AT_INDEX_FOUR = " \n\r\tA";
        
        @Test
        public void passing_string_with_non_whitespace_at_index_four_should_return_index_four() throws IOException {
            assertEquals(INDEX_FOUR, call(NON_WHITE_SPACE_AT_INDEX_FOUR));
        }
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void passing_an_empty_string_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        @Test
        public void passing_string_containing_only_whitespace_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(WHITESPACE_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstNonWhitespaceIndex(string);
        }
        
    } // class FirstNonWhitespaceIndex
    
    
    /** Tests the {@linkplain StringUtils#firstWhitespaceIndex(String)} method. */
    public static class FirstWhitespaceIndex {
        
        private static final int INDEX_FOUR = 4;
        private static final String WHITE_SPACE_AT_INDEX_FOUR = "a1[D B_c\r";
        
        @Test
        public void passing_string_with_whitespace_at_index_four_should_return_index_four() throws IOException {
            assertEquals(INDEX_FOUR, call(WHITE_SPACE_AT_INDEX_FOUR));
        }
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void passing_string_containing_only_letters_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(ALPHA_STRING));
        }
        
        @Test
        public void passing_string_containing_only_numbers_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(NUM_STRING));
        }
        
        @Test
        public void passing_string_containing_only_symbols_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(SYMBOLS_STRING));
        }
        
        @Test
        public void passing_an_empty_string_should_return_not_found() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstWhitespaceIndex(string);
        }
        
    } // class FirstWhiteSpaceIndex
    
    
} // class StringUtilsTest
