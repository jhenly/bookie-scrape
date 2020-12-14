package com.bookiescrape.app.fx;

import static org.junit.Assert.assertEquals;
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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bookiescrape.app.TestUtils;
import com.bookiescrape.app.fx.FontUtilsTest.LoadFontsInDirectoryFile;
import com.bookiescrape.app.fx.FontUtilsTest.LoadFontsInDirectoryPath;

import javafx.scene.text.Font;


/**
 * Contains JUnit4 tests for {@linkplain com.bookiescrape.app.fx.TestUtils}.
 *
 * @author Jonathan Henly
 */
@RunWith(Suite.class)
@SuiteClasses({ LoadFontsInDirectoryFile.class,
                LoadFontsInDirectoryPath.class })
public class FontUtilsTest {
    
    private static final String TEST_FONT_DIR = "/fxml/font";
    private static final String TEST_FONT_RES_PATH =
        TestUtils.getTestResource(TEST_FONT_DIR).getAbsolutePath();
    private static final String NON_EXISTANT_DIR = TEST_FONT_RES_PATH + "/bla";
    private static final String NON_DIR_FILE =
        TEST_FONT_RES_PATH + "BellotaText-Bold.ttf";
    
    private static File nonExistantDir;
    private static File nonDirFile;
    private static File fontDir;
    
    /**
     * Runs once before any tests (Called once before anything else)
     */
    @BeforeClass
    public static void setUpOnceFirst() {
        System.out.println(TEST_FONT_RES_PATH);
        nonExistantDir = new File(NON_EXISTANT_DIR);
        nonDirFile = new File(NON_DIR_FILE);

        fontDir = new File(TEST_FONT_RES_PATH);
    }
    
    
    /**
     * Cleans up any other resources. (Called once after everything else)
     */
    @AfterClass
    public static void tearDownOnceLast() {}
    
    /**
     * Sets up the test fixture. (Called before every test case method.)
     */
    @Before
    public void setUp() {}
    
    /**
     * Tears down the test fixture. (Called after every test case method.)
     */
    @After
    public void tearDown() {}
    
    /* Functionality Testing */
    
    public static class LoadFontsInDirectoryPath {

        // font names that are added from 'src/test/fxml/font/'
        private static final List<String> FONT_NAMES_TO_ADD = List.of(
            "Noto Sans KR", "Bellota Text Bold", "Noto Sans KR Medium",
            "Nunito Sans Regular", "Nunito Sans SemiBold",
            "Nunito Sans SemiBold Italic", "Nunito Sans Italic",
            "Nunito Sans Bold", "Nunito Sans Bold Italic", "Gelasio Regular");


        @BeforeClass
        public static void setUpOnceFirst() {}
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            Path nullPath = null;
            FontUtils.loadFontsInDirectory(nullPath);
        }
        
        
        @Test(expected = IllegalArgumentException.class)
        public void should_throw_iae_when_path_does_not_exist()
            throws IOException
        {
            FontUtils.loadFontsInDirectory(nonExistantDir.toPath());
        }
        
        
        @Test(expected = IllegalArgumentException.class)
        public void should_throw_iae_when_path_is_not_a_dir()
            throws IOException
        {
            FontUtils.loadFontsInDirectory(nonDirFile.toPath());
        }

        @Test
        public void passing_dir_containing_fonts_should_load_fonts_in_dir()
            throws IOException
        {
            // load fonts in test font directory
            List<Font> fonts = FontUtils.loadFontsInDirectory(fontDir.toPath());

            fonts.forEach(font -> {
                assertTrue(
                    "font names to add does not contain " + font.getName(),
                    FONT_NAMES_TO_ADD.contains(font.getName()));
            });

            // make sure that all test fonts were loaded
            assertEquals("all of the fonts were not loaded", fonts.size(),
                FONT_NAMES_TO_ADD.size());
        }

    }

    public static class LoadFontsInDirectoryFile {
        
        @Test(expected = NullPointerException.class)
        public void should_throw_npe_when_passed_null() throws IOException {
            File nullFile = null;
            FontUtils.loadFontsInDirectory(nullFile);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void should_throw_iae_when_file_does_not_exist()
            throws IOException
        {
            FontUtils.loadFontsInDirectory(nonExistantDir);
        }

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_iae_when_file_is_not_a_dir()
            throws IOException
        {
            FontUtils.loadFontsInDirectory(nonDirFile);
        }

    }


    /* Exception Testing */


}
