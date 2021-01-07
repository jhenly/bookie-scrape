package com.bookiescrape.app.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;

import com.bookiescrape.app.launch.OSType;

/**
 * Utility class with file and directory utility methods.
 * @author Jonathan Henly
 */
public final class FileUtils {
    
    private static final String USER_HOME_PROPERTY = "user.home";
    
    private static String appDirName;
    private static Path userHomeDir;
    private static Path appDir;
    private static Path bootDir;
    private static Path configDir;
    
    
    private FileUtils() {}
    
    /**
     * Sets the application's directory name.
     * <p>
     * This method must be invoked before using any utility methods associated
     * with the application's directory.
     * @param dirName - the application's directory name
     * @throws NullPointerException if the specified directory name is
     *         {@code null}
     * @throws IllegalArgumentException if the specified directory name is blank
     */
    public static void setAppDirectoryName(String dirName) {
        String nonNullDirName = Objects.requireNonNull(dirName, "application directory name cannot be null.");
        throwIfStringIsBlank(dirName, "application's directory name cannot be blank.");
        
        appDirName = nonNullDirName;
    }
    
    
    /**************************************************************************
     *                                                                        *
     * User Home Directory                                                    *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets the user's home directory as a {@code Path} instance.
     * @return the user's home directory as a {@code Path} instance
     * @see Path
     */
    public static Path getUserHomeDirectory() {
        if (userHomeDir == null) { userHomeDir = initUserHomeDir(); }
        return userHomeDir;
    }
    
    /** Initializes userHomeDir if it has not been. */
    private static Path initUserHomeDir() {
        String userHome = getUserHomeProperty();
        
        if (userHome.isBlank()) { return handleEmptyUserHomeProperty(); }
        
        Path uh = null;
        try {
            uh = Path.of(userHome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return uh;
    }
    
    /** Handles the user home directory not being set. */
    private static Path handleEmptyUserHomeProperty() {
        return null;
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Application Directory                                                  *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Checks if the application's directory exists.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * called before {@link FileUtils#setAppDirectoryName(String)}.
     * @return {@code true} if the application's directory exists, otherwise
     *         {@code false}
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static boolean appDirectoryExists() {
        return Files.isDirectory(getAppDirectory());
    }
    
    /**
     * Checks if a file exists in the application's directory.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * called before {@link FileUtils#setAppDirectoryName(String)}.
     * @param file - the filename optionally preceded by any sub directory
     *        of the application's directory
     * @return {@code true} if the application's directory exists, otherwise
     *         {@code false}
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static boolean fileExistsInAppDirectory(String file) {
        if (!appDirectoryExists()) { return false; }
        
        return Files.exists(getAppDirectory().resolve(file));
    }
    
    /**
     * Checks if a specified subdirectory exists in the application's directory.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * called before {@link FileUtils#setAppDirectoryName(String)}.
     * @param subDir - the subdirectory in the application's directory to
     *        check for existence of
     * @return {@code true} if the specified subdirectory exists in the
     *         application's directory, otherwise {@code false}
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static boolean subAppDirectoryExists(String subDir) {
        if (!appDirectoryExists()) { return false; }
        
        return Files.isDirectory(getAppDirectory().resolve(subDir));
    }
    
    /**
     * Creates the application's directory if it does not exist.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * called before {@link FileUtils#setAppDirectoryName(String)}.
     * @return a {@code Path} instance representing the application directory
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static Path createAppDirectory() throws IOException {
        if (appDirectoryExists()) { return appDir; }
        
        return Files.createDirectory(getAppDirectory());
    }
    
    /**
     * Creates a specified subdirectory in the application's directory if it
     * does not exist.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * called before {@link FileUtils#setAppDirectoryName(String)}.
     * 
     * @param subDir - the subdirectory to create in the application's directory
     * @return a {@code Path} instance representing the specified sub directory
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the specified sub directory string
     *         is blank
     * @throws NullPointerException if the specified sub directory string is
     *         null or if the application's directory name has not been set
     *         via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static Path createSubAppDirectory(String subDir) throws IOException {
        Objects.requireNonNull(subDir, "subDir cannot be null.");
        throwIfStringIsBlank(subDir, "subDir cannot be blank.");
        
        if (!appDirectoryExists()) { createAppDirectory(); }
        
        Path subDirPath = getAppDirectory().resolve(subDir);
        if (Files.exists(subDirPath)) { return subDirPath; }
        
        return Files.createDirectories(subDirPath);
    }
    
    /**
     * Gets the application directory as a {@code Path} instance.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * called before {@link FileUtils#setAppDirectoryName(String)}.
     * @return the application directory as a {@code Path} instance
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     * @see Path
     */
    public static Path getAppDirectory() {
        throwIfAppDirNameNotSet();
        if (appDir == null) { appDir = initAppDirectory(); }
        return appDir;
    }
    
    /** Initializes the application directory path. */
    private static Path initAppDirectory() {
        Path osUserAppsDir = getOSUserAppsDirectory(getUserHomeDirectory());
        
        return osUserAppsDir.resolve(appDirName);
    }
    
    /** Gets the OS specific application directory. */
    private static Path getOSUserAppsDirectory(Path userHome) {
        switch (OSType.getDetectedOSType()) {
            case Windows:
                return getWindowsUserAppsDirectory(userHome);
            case MacOS:
                return getMacUserAppsDirectory(userHome);
            case Ubuntu:
            case Unix:
            case Linux:
                return getNixUserAppsDirectory(userHome);
            
            case Other:
            default:
                return userHome;
        }
    }
    
    /** Returns a windows specific user apps directory. */
    private static Path getWindowsUserAppsDirectory(Path userHome) {
        final String APP_DATA_LOCAL = "AppData\\Local";
        
        return getSubDirectoryIfItExists(userHome, APP_DATA_LOCAL);
    }
    
    /** Returns a *nix specific user apps directory. */
    private static Path getNixUserAppsDirectory(Path userHome) {
        final String LOCAL_SHARE = ".local/share";
        
        return getSubDirectoryIfItExists(userHome, LOCAL_SHARE);
    }
    
    /** Returns a MacOS specific user apps directory. */
    private static Path getMacUserAppsDirectory(Path userHome) {
        final String LIBRARY_APP_SUPPORT = "Library/Application Support";
        
        return getSubDirectoryIfItExists(userHome, LIBRARY_APP_SUPPORT);
    }
    
    /** Returns a sub directory if it exists, otherwise returns dir. */
    private static Path getSubDirectoryIfItExists(Path dir, String subDir) {
        Path tmpSubDir = dir.resolve(subDir);
        return Files.isDirectory(tmpSubDir) ? tmpSubDir : dir;
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Properties API                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Loads properties from a specified properties file path.
     * @param propFileName - path to the properties file
     * @return properties loaded from the specified file
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static Properties loadPropertiesFileFromAppDirectory(String propFileName) throws IOException {
        return loadPropertiesFile(getAppDirectory().resolve(propFileName));
    }
    
    /**
     * Loads properties from a specified properties file path.
     * @param propFilePath - path to the properties file
     * @return properties loaded from the specified file
     * @throws IOException if an I/O error occurs
     */
    public static Properties loadPropertiesFile(Path propFilePath) throws IOException {
        Properties props = new Properties();
        
        try (BufferedReader reader = openFileForReading(propFilePath)) {
            props.load(reader);
        }
        
        return props;
    }
    
    /**
     * Writes properties to a specified properties file path.
     * @param props - properties to save to file
     * @param propFileName - path to the properties file
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if the application's directory name has not
     *         been set via {@link FileUtils#setAppDirectoryName(String)}
     */
    public static void writePropertiesToFileInAppDirectory(Properties props, String propFileName) throws IOException {
        writePropertiesToFile(props, getAppDirectory().resolve(propFileName));
    }
    
    /**
     * Writes properties to a specified properties file path.
     * @param propFilePath - path to the properties file
     * @param props - properties to save to file
     * @throws IOException if an I/O error occurs
     */
    public static void writePropertiesToFile(Properties props, Path propFilePath) throws IOException {
        try (BufferedWriter writer = openFileForWriting(propFilePath)) {
            props.store(writer, null);
        }
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Buffered Reader/Writer API                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Opens a buffered reader on the specified file.
     * <p>
     * Callers of this method must close the returned buffered reader.
     * @param filePath - path to the file to read
     * @return an opened buffered reader to read from
     * @throws IOException if an I/O error occurs when opening the file
     */
    public static BufferedReader openFileForReading(String filePath) throws IOException {
        return openFileForReading(Path.of(filePath));
    }
    
    /**
     * Opens a buffered reader on the specified file.
     * <p>
     * Callers of this method must close the returned buffered reader.
     * @param filePath - path to the file to read
     * @return an opened buffered reader to read from
     * @throws IOException if an I/O error occurs when opening the file
     */
    public static BufferedReader openFileForReading(Path filePath) throws IOException {
        return Files.newBufferedReader(filePath);
    }
    
    /**
     * Opens a buffered writer on the specified file.
     * <p>
     * Callers of this method must close the returned buffered writer.
     * @param filePath - path to the file to write
     * @param options - file writing options
     * @return an opened buffered writer to write to
     * @throws IOException if an I/O error occurs when opening the file
     */
    public static BufferedWriter openFileForWriting(String filePath, OpenOption... options) throws IOException {
        return openFileForWriting(Path.of(filePath), options);
    }
    
    /**
     * Opens a buffered writer on the specified file.
     * <p>
     * Callers of this method must close the returned buffered writer.
     * @param filePath - path to the file to write
     * @param options - file writing options
     * @return an opened buffered writer to write to
     * @throws IOException if an I/O error occurs when opening the file
     */
    public static BufferedWriter openFileForWriting(Path filePath, OpenOption... options) throws IOException {
        return Files.newBufferedWriter(filePath, options);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Internal To External API                                               *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Copies an internal JAR resource to an external file.
     * @param resource - the resource to copy's input stream
     * @param filePath - the external file to create or overwrite
     * @throws IOException if an I/O error occurs
     */
    public static void copyJarResourceToFile(InputStream resource, String filePath) throws IOException {
        copyJarResourceToFile(resource, Path.of(filePath));
    }
    
    /**
     * Copies an internal JAR resource to an external file.
     * @param resource - the resource to copy's input stream
     * @param filePath - the external file to create or overwrite
     * @throws IOException if an I/O error occurs
     */
    public static void copyJarResourceToFile(InputStream resource, Path filePath) throws IOException {
        Files.copy(resource, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /** 
     * Helper to get the user home directory from
     * {@link System#getProperty(String)}.
     * <p>
     * If querying the system properties for {@code "user.home"} throws an
     * exception or results in {@code null}, then the empty string ({@code ""})
     * will be returned.
     * <p>
     * Any thrown exceptions will be caught by this method and logged.
     * 
     * @return the system properties' user home, or the empty string
     *         ({@code ""})
     */
    private static String getUserHomeProperty() {
        String userHome = null;
        try {
            userHome = System.getProperty(USER_HOME_PROPERTY);
        } catch (Exception e) {
            // TODO properly log exception from getting user home
            System.err.println(e.getLocalizedMessage());
        }
        
        return userHome == null ? "" : userHome;
    }
    
    /** Helper that throws if appDirName has not been set. */
    private static void throwIfAppDirNameNotSet() {
        Objects.requireNonNull(appDirName, "application's directory name has not been set.");
    }
    
    /** Helper that throws if a passed in string is blank. */
    private static void throwIfStringIsBlank(String str, String message) {
        if (str.isBlank()) { throw new IllegalArgumentException(message); }
    }
    
    
} // class FileUtils
