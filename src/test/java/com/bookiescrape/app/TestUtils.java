package com.bookiescrape.app;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.junit.Test;


public final class TestUtils {
    private static final Class<TestUtils> CLASS = TestUtils.class;
    
    private static final String PROJECT_DIR = (new File("")).getAbsolutePath();
    private static final String TEST_RES_PATH =
        PROJECT_DIR + "/src/test/resources";
    private static final String MAIN_RES_PATH =
        PROJECT_DIR + "/src/main/resources";
    
    /**
     * Gets the absolute path to the test resources directory.
     *
     * @return absolute path to the test resources directory
     */
    public static String getTestResPath() { return TEST_RES_PATH; }
    
    /**
     * Gets the absolute path to the main resources directory.
     *
     * @return absolute path to the main resources directory
     */
    public static String getMainResPath() { return MAIN_RES_PATH; }
    
    
    /**
     * Gets a file from the test resources directory.
     *
     * @param relativePath - file path relative to test resources directory
     * @return a file instance created from appending the specified file
     *         resource path to the test resources directory's absolute path
     */
    public static File getTestFile(String relativePath) {
        String path = Objects.requireNonNull(relativePath);
        return new File(getTestResPath() + "/" + path);
    }
    
    /**
     * Gets a file from the main resources directory.
     *
     * @param relativePath - file path relative to main resources directory
     * @return a file instance created from appending the specified file
     *         resource path to the main resources directory's absolute path
     */
    public static File getMainFile(String relativePath) {
        String path = Objects.requireNonNull(relativePath);
        return new File(getMainResPath() + "/" + path);
    }
    
    /**
     * Gets a file representation of a specified resource.
     * <p>
     * The returned file is created using the following: <blockquote>
     *
     * <pre>
     * <code>
     * String path = getTestResPath() + relativePath;
     * File resource = new File(
     *     TestUtils.class.getResource(path).getFile()
     * );
     * </code>
     * </pre>
     *
     * </blockquote>
     *
     * @param resourcePath - path to the resource
     * @return a file instance created from a resource
     */
    public static File getTestResource(String resourcePath) {
        String path = Objects.requireNonNull(resourcePath);
        return new File(CLASS.getResource(path).getFile());
    }
    
    
    /* Begin Test Methods For TestUtils */
    
    public static class TestUtilsTest {
        
        private static final String CON_PROPS = "config.properties";
        
        public static class CurrentDirectory {
            private static final String PROJECT_NAME = "bookie-scrape";
            
            @Test
            public void project_directory_ends_with_project_name() {
                final String msg = String.format(
                    "The current working directory [ %s ] does not end with [ %s ]",
                    PROJECT_DIR, PROJECT_NAME);
                assertTrue(msg, PROJECT_DIR.endsWith(PROJECT_NAME));
            }
        }
        
        
        public static class GetTestResPath {
            @Test
            public void test_resources_path_exists() {
                assertTrue(Files.exists(Path.of(getTestResPath())));
            }
        }
        
        public static class GetMainResPath {
            @Test
            public void main_resources_path_exists() {
                assertTrue(Files.exists(Path.of(getMainResPath())));
            }
        }
        
        public static class GetTestFile {
            @Test
            public void return_config_properties_from_test_resources_dir() {
                File testConProps = getTestFile(CON_PROPS);
                String filePath = testConProps.getAbsolutePath();
                
                assertTrue(filePath.startsWith(getTestResPath()));
                assertTrue(filePath.endsWith(CON_PROPS));
                assertTrue(Files.exists(testConProps.toPath()));
            }
            
            @Test(expected = NullPointerException.class)
            public void passing_null_string_should_throw_npe() {
                String nullStr = null;
                getTestFile(nullStr);
            }
        }
        
        public static class GetMainFile {
            @Test
            public void return_config_properties_from_main_resources_dir() {
                File mainConProps = getMainFile(CON_PROPS);
                String filePath = mainConProps.getAbsolutePath();
                
                assertTrue(filePath.startsWith(getMainResPath()));
                assertTrue(filePath.endsWith(CON_PROPS));
                assertTrue(Files.exists(mainConProps.toPath()));
            }
            
            @Test(expected = NullPointerException.class)
            public void passing_null_string_should_throw_npe() {
                String nullStr = null;
                getMainFile(nullStr);
            }
        }
        
        public static class GetTestResource {
            private static final String TEST_CLASSES_PATH = "/";
            private static final String TEST_CLASSES = "test-classes";
            
            @Test
            public void return_test_resource_dir_as_file() {
                File testClasses = getTestResource(TEST_CLASSES_PATH);
                
                assertTrue(
                    testClasses.getAbsolutePath().endsWith(TEST_CLASSES));
                assertTrue(Files.isDirectory(testClasses.toPath()));
            }
            
            @Test(expected = NullPointerException.class)
            public void passing_null_string_should_throw_npe() {
                String nullStr = null;
                getTestResource(nullStr);
            }
        }
        
        
    } // TestUtilsTest
    
    
}
