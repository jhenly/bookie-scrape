package com.bookiescrape.app.sample;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.launch.ApplicationLauncher;
import com.bookiescrape.app.settings.Settings;
import com.bookiescrape.app.settings.UserSettings;
import com.bookiescrape.app.util.FileUtils;

/**
 * 
 * @author Jonathan Henly
 */
public final class ApplicationBooter {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBooter.class);
    
    public static enum BootProperty {
        VERSION("version"), USER_SETTINGS_FILE("user_settings_file");
        
        private String key;
        
        BootProperty(String key) { this.key = key; }
        String getKey() { return key; }
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
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     *  If necessary, installs application files into the user's OS dependent 
     *  application directory, then boots the application.
     */
    public static void boot() {
        if (booted) { throw new UnsupportedOperationException("the application can only be booted once."); }
        booted = true;
        
        FileUtils.setAppDirectoryName(APP_DIR);
        
        try {
            preBootSequence();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        try {
            runBootSequence();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        
        ApplicationLauncher.launchApplication(appMediator);
    }
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     * @throws IOException 
     *************************************************************************/
    
    private static void preBootSequence() throws IOException {
        Path userHome = FileUtils.getUserHomeDirectory();
        System.out.println("User Home Dir: " + userHome);
        
        System.out.println("App Dir: " + FileUtils.getAppDirectory());
        System.out.println("AppDir Exists: " + FileUtils.appDirectoryExists());
        
        if (!FileUtils.appDirectoryExists()) {
            installApplication();
        } else {
            if (!FileUtils.fileExistsInAppDirectory(BOOT_PROPS_FILE)) { createBootProperties(); }
            if (!FileUtils.fileExistsInAppDirectory(USER_PROPS_FILE)) { createUserProperties(); }
            if (!FileUtils.subAppDirectoryExists(LOGS_DIR)) { createLogs(); }
            if (!FileUtils.subAppDirectoryExists(ICONS_DIR)) { createIcons(); }
        }
        
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
            String msg = "could not find user properties file \"" + nsfe.getLocalizedMessage() + "\".";
            LOG.error(msg);
            throw new NoSuchFileException(msg);
        }
        
        // create application mediator with user settings
        appMediator = new ApplicationMediator(userSettings);
    }
    
    private static void installApplication() throws IOException {
        FileUtils.createAppDirectory();
        
        createBootProperties();
        createUserProperties();
        createLogs();
        createIcons();
    }
    
    private static void createBootProperties() throws IOException {
        // copy boot properties file from jar
        try (InputStream is = ApplicationBooter.class.getResourceAsStream(BOOT_PROPS_FILE)) {
            Path bootPropsFile = FileUtils.getAppDirectory().resolve(BOOT_PROPS_FILE);
            FileUtils.copyJarResourceToFile(is, bootPropsFile);
            // load boot properties so we can edit them
            bootProps = loadBootProperties(bootPropsFile);
            // set user settings file to user properties file in app directory
            setBootProperty(BootProperty.USER_SETTINGS_FILE,
                FileUtils.getAppDirectory().resolve(USER_PROPS_FILE).toString());
        }
        
    }
    
    private static void createUserProperties() throws IOException {
        try (InputStream is = ApplicationBooter.class.getResourceAsStream(DEFAULT_PROPS_FILE)) {
            FileUtils.copyJarResourceToFile(is, FileUtils.getAppDirectory().resolve(USER_PROPS_FILE));
        }
    }
    
    private static void createLogs() throws IOException {
        FileUtils.createSubAppDirectory(LOGS_DIR);
        
    }
    
    private static void createIcons() throws IOException {
        FileUtils.createSubAppDirectory(ICONS_DIR);
        
    }
    
    private static Properties loadBootProperties(Path bootPropsPath) throws IOException {
        return FileUtils.loadPropertiesFile(bootPropsPath);
    }
    
    /**
     * Gets the value of a specified boot property.
     * @param key - which boot property's value to get
     * @return the value associated with the specified boot property
     */
    public static String getBootProperty(BootProperty key) {
        return bootProps.getProperty(key.getKey());
    }
    
    /**
     * Sets a specified boot property to a specified value and writes boot
     * properties to file.
     * @param key - the boot property to set a new value for
     * @param value - the new boot property value
     * @throws IOException if an I/O error ocurrs
     */
    public static void setBootProperty(BootProperty key, String value) throws IOException {
        bootProps.setProperty(key.getKey(), value);
        
        Path bootPropsFile = FileUtils.getAppDirectory().resolve(BOOT_PROPS_FILE);
        // write changes to boot properties file
        FileUtils.writePropertiesToFile(bootProps, bootPropsFile);
    }
    
    /** This class should not be subclassed. */
    private ApplicationBooter() {}
    
}
