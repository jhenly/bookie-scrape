package com.bookiescrape.app.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bookiescrape.app.util.OperatingSystemUtils.OperatingSystem;
import com.bookiescrape.app.util.OperatingSystemUtilsTest.GetDetectedOS;

@RunWith(Suite.class)
@SuiteClasses({ GetDetectedOS.class })
public class OperatingSystemUtilsTest {
    private static String OS_NAME_KEY = "os.name";
    
    // known operating systems
    private static String WINDOWS = "Windows";
    private static String MAC_OS = "Mac OS X";
    private static String MAC_OS_DARWIN = "Darwin";
    private static String UBUNTU = "Ubuntu";
    private static String UNIX = "Unix";
    private static String LINUX = "Linux";
    
    // unknown operating systems
    private static String CENT_OS = "CentOS";
    private static String SOLARIS = "Solaris";
    
    public static class GetDetectedOS {
        
        private static void test(String osName, OperatingSystem expected) {
            System.setProperty(OS_NAME_KEY, osName);
            
            assertEquals("System.getProperty(\"os.name\") != " + osName + ",", System.getProperty(OS_NAME_KEY, ""),
                osName);
            
            // get operating system from mock operating system, mock operating
            // system will need to be updated if operating system is updated
            OperatingSystem detected = MockOperatingSystemUtils.getDetectedOS().getOperatingSystem();
            
            assertEquals("With osName=\"" + osName + "\", expected != detected,", expected, detected);
        }
        
        @Test
        public void windows_operating_system_enum_should_be_returned_when_os_name_is_windows() {
            test(WINDOWS, OperatingSystem.WINDOWS);
        }
        
        @Test
        public void mac_os_operating_system_enum_should_be_returned_when_os_name_is_mac_os() {
            test(MAC_OS, OperatingSystem.MAC_OS);
        }
        
        @Test
        public void mac_os_operating_system_enum_should_be_returned_when_os_name_is_mac_os_darwin() {
            test(MAC_OS_DARWIN, OperatingSystem.MAC_OS);
        }
        
        @Test
        public void ubuntu_operating_system_enum_should_be_returned_when_os_name_is_ubuntu() {
            test(UBUNTU, OperatingSystem.UBUNTU);
        }
        
        @Test
        public void unix_operating_system_enum_should_be_returned_when_os_name_is_unix() {
            test(UNIX, OperatingSystem.UNIX);
        }
        
        @Test
        public void linux_operating_system_enum_should_be_returned_when_os_name_is_linux() {
            test(LINUX, OperatingSystem.LINUX);
        }
        
        @Test
        public void other_operating_system_enum_should_be_returned_when_os_name_is_cent_os() {
            test(CENT_OS, OperatingSystem.OTHER);
        }
        
        @Test
        public void other_operating_system_enum_should_be_returned_when_os_name_is_solaris() {
            test(SOLARIS, OperatingSystem.OTHER);
        }
        
    } // class GetDetectedOs
    
    
} // class OperatingSystemUtilsTest
