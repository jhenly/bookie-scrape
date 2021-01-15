package com.bookiescrape.app.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
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
    private static final String NON_ALPHA_NUM_WHITE_STRING = "[{()}]!@#$%^&*_-+=`~\"',./<>?\\|;:";
    
    private static final int NOT_FOUND = -1;
    
    @BeforeClass
    public static void setUpOnceFirst() {
        
    }
    
    public static class CapitalizeFirstAlphabeticChar {
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void should_return_empty_string_when_passed_empty_string() throws IOException {
            assertEquals(EMPTY_STRING, call(EMPTY_STRING));
        }
        
        private String call(String string) {
            return StringUtils.capitalizeFirstAlphabeticChar(string);
        }
        
    }
    
    public static class FirstAlphabeticIndex {
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void should_return_not_found_when_passed_empty_string() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstAlphabeticIndex(string);
        }
        
    }
    
    public static class FirstNumericIndex {
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void should_return_not_found_when_passed_empty_string() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstNumericIndex(string);
        }
        
    } // class FirstNumericIndex
    
    public static class FirstNonWhitespaceIndex {
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void should_return_not_found_when_passed_empty_string() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstNonWhitespaceIndex(string);
        }
        
    } // FirstNonWhitespaceIndex
    
    public static class FirstWhitespaceIndex {
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            call(NULL_STRING);
        }
        
        @Test
        public void should_return_not_found_when_passed_empty_string() throws IOException {
            assertEquals(NOT_FOUND, call(EMPTY_STRING));
        }
        
        private int call(String string) {
            return StringUtils.firstWhitespaceIndex(string);
        }
        
    } // FirstWhiteSpaceIndex
    
}
