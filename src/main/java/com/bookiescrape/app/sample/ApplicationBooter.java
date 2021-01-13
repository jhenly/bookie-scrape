package com.bookiescrape.app.sample;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.launch.ApplicationLauncher;
import com.bookiescrape.app.settings.Settings;
import com.bookiescrape.app.settings.UserSettings;
import com.bookiescrape.app.util.FileUtils;

/**
 * Application booting handler class.
 * 
 * @author Jonathan Henly
 * @see BootProperty
 */
public final class ApplicationBooter {
    
    /** 
     * Enum representing the boot properties keys.
     * 
     * @see BootProperty#VERSION
     * @see BootProperty#USER_SETTINGS_FILE
     */
    public static enum BootProperty {
        /** Boot properties' version key. */
        VERSION("version"),
        /** Boot properties' user settings file key.*/
        USER_SETTINGS_FILE("user_settings_file");
        
        private String key;
        
        BootProperty(String key) { this.key = key; }
        
        /**
         * Gets this {@code BootProperty} enum's key.
         * @return this {@code BootProperty} enum's key
         * @see BootProperty
         */
        public String getKey() { return key; }
    }
    
    private static final String APP_DIR = Main.APP_DIR_NAME;
    private static final String LOGS_DIR = "logs";
    private static final String ICONS_DIR = "icons";
    
    private static final String BOOT_PROPS_FILE = "boot.properties";
    private static final String DEFAULT_PROPS_FILE = "default.properties";
    private static final String USER_PROPS_FILE = "user.properties";
    
    
    private static boolean booted;
    private static Properties bootProps;
    private static ApplicationMediator appMediator;
    private static Consumer<Exception> fatalHook;
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Sets a hook that will be called if a fatal exception occurs during
     * {@link #boot()}.
     * <p>
     * If a fatal exception occurs while booting up the application, then the
     * hook set by this method will be invoked via 
     * {@code hook.accept(fatalException)}, where {@code fatalException} is
     * the fatal exception that was thrown during application boot.
     * <p>
     * <b>Note:</b> the hook must be set before invoking {@link #boot()}.
     * @param hook - the consumer to call {@code accept(Throwable)} on if a
     *        fatal exception occurs 
     * @see #boot()
     */
    public static void setOnFatalExceptionHook(Consumer<Exception> hook) { fatalHook = hook; }
    
    /**
     * If necessary, installs application files into the user's OS dependent 
     * application directory, then boots up the application.
     * <p>
     * This method will not throw any exceptions that extend
     * {@link java.lang.Exception}, therefore any exception encountered during
     * the boot process is fatal.
     * <p>
     * A hook may be set, via {@link #setOnFatalExceptionHook(Consumer)}, to be
     * executed if a fatal exception occurs when booting. This hook is to allow
     * the caller to notify the user of a fatal exception.
     * <p>
     * <b>Note:</b> invoking this method more than once will have no effect.
     * 
     * @see #setOnFatalExceptionHook(Consumer)
     */
    public static void boot() {
        if (booted) { return; }
        booted = true;
        
        // set the application's directory name before doing anything else
        FileUtils.setAppDirectoryName(APP_DIR);
        
        // install the application or replace any missing files before booting
        try {
            preBootSequence();
        } catch (Exception e) {
            if (fatalHook != null) {
                LOG.info("fatal hook set, calling 'fatalHook.accept(Exception)");
                fatalHook.accept(e);
                return;
            }
            
            e.printStackTrace();
        }
        
        // load boot settings and user settings
        try {
            runBootSequence();
        } catch (Exception e) {
            if (fatalHook != null) {
                LOG.info("fatal hook set, calling 'fatalHook.accept(Exception)");
                fatalHook.accept(e);
                return;
            }
            
            e.printStackTrace();
        }
        
        LOG.info("finished boot sequence");
        
        ApplicationLauncher.launchApplication(appMediator);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /** Helper that installs the application or replaces any missing files. */
    private static void preBootSequence() throws IOException {
        if (!FileUtils.appDirectoryExists()) {
            installApplication();
        } else {
            if (!FileUtils.fileExistsInAppDirectory(BOOT_PROPS_FILE)) { createBootProperties(); }
            if (!FileUtils.fileExistsInAppDirectory(USER_PROPS_FILE)) { createUserProperties(); }
            if (!FileUtils.subAppDirectoryExists(LOGS_DIR)) { createLogsDirectory(); }
            if (!FileUtils.subAppDirectoryExists(ICONS_DIR)) { createIconsDirectory(); }
        }
        
    }
    
    /** Helper that creates all app directories and copies all files from jar. */
    private static void installApplication() throws IOException {
        try {
            FileUtils.createAppDirectory();
        } catch (IOException ioe) {
            throw logAndReturnUnableToCreateDirectoryException(ioe, "application directory",
                FileUtils.getAppDirectory());
        }
        
        createBootProperties();
        createUserProperties();
        createLogsDirectory();
        createIconsDirectory();
    }
    
    private static void runBootSequence() throws IOException {
        if (bootProps == null) {
            bootProps = loadBootProperties(FileUtils.getAppDirectory().resolve(BOOT_PROPS_FILE));
        }
        
        // load user's properties files
        String propFilePath = getBootProperty(BootProperty.USER_SETTINGS_FILE);
        Path userPropertiesFile = Path.of(propFilePath);
        
        Settings userSettings = null;
        try {
            userSettings = UserSettings.loadSettings(userPropertiesFile);
        } catch (NoSuchFileException nsfe) {
            throw logAndReturnUserPropertiesNotFoundException(nsfe);
        }
        
        // create application mediator with user settings
        appMediator = new ApplicationMediator(userSettings);
    }
    
    private static void createBootProperties() throws IOException {
        Path bootPropsFilePath = FileUtils.getAppDirectory().resolve(BOOT_PROPS_FILE);
        
        // copy boot properties file from jar
        try (InputStream is = ApplicationBooter.class.getResourceAsStream(BOOT_PROPS_FILE)) {
            
            try {
                FileUtils.copyJarResourceToFile(is, bootPropsFilePath);
            } catch (IOException ioe) {
                throw logAndReturnUnableToCopyPropertiesFromJarException(ioe, BOOT_PROPS_FILE, bootPropsFilePath);
            }
            
        }
        
        // load boot properties so we can edit them
        bootProps = loadBootProperties(bootPropsFilePath);
        
        // set user settings file to user properties file in app directory
        setBootProperty(BootProperty.USER_SETTINGS_FILE,
            FileUtils.getAppDirectory().resolve(USER_PROPS_FILE).toString());
    }
    
    private static void createUserProperties() throws IOException {
        Path userPropsFilePath = FileUtils.getAppDirectory().resolve(USER_PROPS_FILE);
        
        try (InputStream is = ApplicationBooter.class.getResourceAsStream(DEFAULT_PROPS_FILE)) {
            FileUtils.copyJarResourceToFile(is, userPropsFilePath);
        } catch (IOException ioe) {
            throw logAndReturnUnableToCopyPropertiesFromJarException(ioe, DEFAULT_PROPS_FILE, userPropsFilePath);
        }
        
    }
    
    /** Helper that creates app's logs directory. */
    private static void createLogsDirectory() throws IOException {
        try {
            FileUtils.createSubAppDirectory(LOGS_DIR);
        } catch (IOException ioe) {
            throw logAndReturnUnableToCreateDirectoryException(ioe, "logs directory",
                FileUtils.getAppDirectory().resolve(LOGS_DIR));
        }
        
    }
    
    /** Helper that creates app's icons directory and copies icons from jar. */
    private static void createIconsDirectory() throws IOException {
        try {
            FileUtils.createSubAppDirectory(ICONS_DIR);
        } catch (IOException ioe) {
            throw logAndReturnUnableToCreateDirectoryException(ioe, "icons directory",
                FileUtils.getAppDirectory().resolve(LOGS_DIR));
        }
        
    }
    
    /** Helper that loads boot properties from a file path. */
    private static Properties loadBootProperties(Path bootPropsPath) throws IOException {
        Properties bprops = null;
        
        try {
            bprops = FileUtils.loadPropertiesFile(bootPropsPath);
        } catch (IOException ioe) {
            throw logAndReturnUnableToLoadPropertiesException(ioe, "boot", bootPropsPath);
        }
        
        return bprops;
    }
    
    /**
     * Gets the value of a specified boot property.
     * @param key - which boot property's value to get
     * @return the value associated with the specified boot property
     * @throws NullPointerException if the specified key is {@code null}
     */
    public static String getBootProperty(BootProperty key) {
        BootProperty nonNullKey = Objects.requireNonNull(key, "boot property key can not be null");
        
        return bootProps.getProperty(nonNullKey.getKey());
    }
    
    /**
     * Sets a specified boot property to a specified value and writes boot
     * properties to file.
     * @param key - the boot property to set a new value for
     * @param value - the new boot property value
     * @throws IOException if an I/O error occurrs
     * @throws NullPointerException if the specified key is {@code null}
     */
    public static void setBootProperty(BootProperty key, String value) throws IOException {
        BootProperty nonNullKey = Objects.requireNonNull(key, "boot property key can not be null");
        
        bootProps.setProperty(nonNullKey.getKey(), value);
        
        // write changes to boot properties file
        Path bootPropsFile = FileUtils.getAppDirectory().resolve(BOOT_PROPS_FILE);
        FileUtils.writePropertiesToFile(bootProps, bootPropsFile);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Logging and Exception Convenience Methods                              *
     *                                                                        *
     *************************************************************************/
    
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBooter.class);
    
    
    /** Logs and returns the exception thrown when unable to create an application dir. */
    private static IOException logAndReturnUnableToCreateDirectoryException(IOException ioe, String whichDir,
        Path dirPath) {
        
        String msg = String.format("could not create %s directory \"%s\" ", whichDir, dirPath);
        
        return logErrorMsgAndReturnIOException(msg, ioe);
    }
    
    /** Logs and returns the exception thrown when unable to copy boot props from jar. */
    private static IOException logAndReturnUnableToCopyPropertiesFromJarException(IOException ioe, String propsFile,
        Path copyTo) {
        
        String msg = String.format("could not copy \"%s\" from JAR to \"%s\" ", propsFile, copyTo);
        
        return logErrorMsgAndReturnIOException(msg, ioe);
    }
    
    /** Logs and returns the exception thrown when unable to load properties from file. */
    private static IOException logAndReturnUnableToLoadPropertiesException(IOException ioe, String whichProps,
        Path bootProps) {
        
        String msg = String.format("could not load %s properties from \"%s\" ", whichProps, bootProps);
        
        return logErrorMsgAndReturnIOException(msg, ioe);
    }
    
    /** Logs and returns the exception thrown when user props can't be found. */
    private static NoSuchFileException logAndReturnUserPropertiesNotFoundException(NoSuchFileException nsfe) {
        String msg = "could not find user properties file ";
        LOG.error(msg, nsfe);
        
        return new NoSuchFileException(String.format("%s \"%s\"", msg, nsfe.getLocalizedMessage()));
    }
    
    
    /** Helper that logs an error message and returns a new IOException. */
    private static IOException logErrorMsgAndReturnIOException(String msg, IOException ioe) {
        LOG.error(msg, ioe);
        return new IOException(msg, ioe);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private Constructor(s)                                                 *
     *                                                                        *
     *************************************************************************/
    
    /** This class should not be subclassed. */
    private ApplicationBooter() {}
    
}
