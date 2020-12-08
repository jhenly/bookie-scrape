package com.bookiescrape.app.fx.ui;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.scene.text.Font;


public class FontUtilsTest {
    private static final String TEST_FONT_DIR = "/fxml/font/";
    private static final String NON_EXISTANT_DIR = TEST_FONT_DIR + "bla";
    private static final String NON_DIR_FILE =
        TEST_FONT_DIR + "BellotaText-Bold.ttf";
    private static final String DIR_WITH_NO_FONTS = "/fxml/css";
    
    private static File nullFile;
    private static Path nullPath;
    private static File nonExistantDir;
    private static File nonDirFile;
    private static File dirWithNoFonts;
    private static File fontDir;
    private static List<String> startFonts;

    /**
     * Runs once before any tests (Called once before anything else)
     */
    @BeforeClass
    public static void setUpOnceFirst() {
        nullFile = null;
        nullPath = null;
        nonExistantDir = new File(NON_EXISTANT_DIR);
        nonDirFile = new File(NON_DIR_FILE);
        
        dirWithNoFonts = new File(
            FontUtilsTest.class.getResource(DIR_WITH_NO_FONTS).getFile());
        fontDir =
            new File(FontUtilsTest.class.getResource(TEST_FONT_DIR).getFile());
        
        startFonts = Font.getFontNames();
    }

    /**
     * Cleans up any other resources. (Called once after everything else)
     */
    @AfterClass
    public static void tearDownOnceLast() {

    }

    /**
     * Sets up the test fixture. (Called before every test case method.)
     */
    @Before
    public void setUp() {
        
    }

    /**
     * Tears down the test fixture. (Called after every test case method.)
     */
    @After
    public void tearDown() {
        
    }

    /* Functionality Testing */

    @Test
    public void
    passing_dir_with_no_fonts_to_load_fonts_in_directory_should_not_load_fonts()
    {
        FontUtils.loadFontsInDirectory(dirWithNoFonts);
        List<String> fonts = Font.getFontNames();
        
        assertTrue(startFonts.equals(fonts));
    }

    @Test
    public void
    passing_dir_with_fonts_to_load_fonts_in_directory_should_load_the_fonts()
        throws IOException
    {
        FontUtils.loadFontsInDirectory(fontDir);
        assertTrue(true);
    }
    
    /* Exception Testing */
    
    @Test(expected = NullPointerException.class)
    public void
    load_fonts_in_directory_file_should_throw_npe_when_passed_null()
    {
        FontUtils.loadFontsInDirectory(nullFile);
    }

    @Test(expected = NullPointerException.class)
    public void
    load_fonts_in_directory_path_should_throw_npe_when_passed_null()
    {
        FontUtils.loadFontsInDirectory(nullPath);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void
    load_fonts_in_directory_file_should_throw_iae_when_file_does_not_exist()
    {
        FontUtils.loadFontsInDirectory(nonExistantDir);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void
    load_fonts_in_directory_path_should_throw_iae_when_path_does_not_exist()
    {
        FontUtils.loadFontsInDirectory(nonExistantDir.toPath());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void
    load_fonts_in_directory_file_should_throw_iae_when_file_is_not_a_dir()
    {
        FontUtils.loadFontsInDirectory(nonDirFile);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void
    load_fonts_in_directory_path_should_throw_iae_when_path_is_not_a_dir()
    {
        FontUtils.loadFontsInDirectory(nonDirFile.toPath());
    }

}
