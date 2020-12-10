package com.bookiescrape.app.fx.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.text.Font;


/**
 * Utility class that consists exclusively of static methods pertaining to
 * fonts.
 *
 * @author Jonathan Henly
 */
public final class FontUtils {

    // true type font file extension
    private static final String TTF_EXT = ".ttf";
    // open type font file extension
    private static final String OTF_EXT = ".otf";
    // true type collection file extension
    private static final String TTC_EXT = ".ttc";
    // web font type
    private static final String WOFF_EXT = ".woff";
    // web font type
    private static final String WOFF2_EXT = ".woff2";
    // list of font file extensions
    private static final List<String> VALID_FONT_EXTS =
        List.of(TTF_EXT, OTF_EXT, TTC_EXT, WOFF_EXT, WOFF2_EXT);

    // don't subclass this class
    private FontUtils() {}


    /**
     * Loads all of the valid font files in a specified directory.
     * <p>
     * The font files in the specified directory are loaded using
     * {@linkplain Font#loadFont(InputStream, double)}. If the specified
     * directory contains any true type collection files, then they are loaded
     * using {@linkplain Font#loadFonts(InputStream, double)}.
     *
     * @param fontDir - file representing the font directory
     * @return a list containing the loaded fonts
     * @throws IOException - if an I/O error occurs
     * @see Font#loadFont(InputStream, double)
     * @see Font#loadFonts(InputStream, double)
     */
    public static List<Font> loadFontsInDirectory(File fontDir)
        throws IOException
    {
        Path dir = validateDirectory(fontDir);
        return loadFontsInDirectoryHelper(dir);
    }

    /**
     * Loads all of the valid font files in a specified directory.
     * <p>
     * Font files in the specified directory are loaded using
     * {@linkplain Font#loadFont(InputStream, double)}. If the specified
     * directory contains any true type collection files, then they are loaded
     * using {@linkplain Font#loadFonts(InputStream, double)}.
     * <p>
     *
     * @param fontDir - path to the font directory
     * @return a list containing the loaded fonts
     * @throws IOException - if an I/O error occurs
     * @see Font#loadFont(InputStream, double)
     * @see Font#loadFonts(InputStream, double)
     */
    public static List<Font> loadFontsInDirectory(Path fontDir)
        throws IOException
    {
        Path dir = validateDirectory(fontDir);
        return loadFontsInDirectoryHelper(dir);
    }
    
    /* helper method that loads fonts from a specified directory */
    private static List<Font> loadFontsInDirectoryHelper(Path dir)
        throws IOException
    {
        final List<String> fontFiles = getFontFilesInDirectory(dir, "file://");
        return checkAndLogFontsFromDirectory(fontFiles, dir);
    }
    
    /**
     * Loads all of the valid font files in a specified resources directory.
     * <p>
     * Font files in the specified resources directory are loaded using
     * {@linkplain Font#loadFont(InputStream, double)}. If the specified
     * directory contains any true type collection files, then they are loaded
     * using {@linkplain Font#loadFonts(InputStream, double)}.
     * <p>
     *
     * @param resDir - path to the font resources directory
     * @return a list containing the loaded fonts
     * @throws IOException - if an I/O error occurs
     * @see Font#loadFont(InputStream, double)
     * @see Font#loadFonts(InputStream, double)
     */
    public static List<Font> loadFontsFromResources(String resDir)
        throws IOException
    {
        URI resUri = validateResourceDirectory(resDir);
        return loadFontsInResourcesDirectory(resDir, resUri);
    }

    /* loadFontsInResourcesDirectory helper method */
    private static List<Font>
    loadFontsInResourcesDirectory(String resDir, URI resUri) throws IOException
    {
        final List<String> fontFiles;
        final Path resDirPath;
        if (resUri.getScheme().equals("jar")) {
            
            // get jar's font directory, jar file system needs to be closed
            try (final FileSystem jarFile =
                FileSystems.newFileSystem(resUri, Collections.emptyMap()))
            {
                System.out.println("IN JAR FILE");
                resDirPath = jarFile.getPath(resDir);
                // load fonts from jar file's font directory
                fontFiles = getFontFilesInDirectory(resDirPath, "");
            }

            return checkAndLogFontsFromResources(fontFiles, resDirPath);
        } else {
            
            resDirPath = Paths.get(resUri);
            fontFiles = getFontFilesInDirectory(resDirPath, "file://");

            return checkAndLogFontsFromDirectory(fontFiles, resDirPath);
        }
        
    }
    
    /* helper method that gets the files in a specified directory */
    private static List<String>
    getFontFilesInDirectory(Path fontDir, String scheme) throws IOException
    {
        final List<String> fontFiles;

        // build list of font files in specified font directory
        try (final Stream<Path> paths = Files.list(fontDir)) {

            // only include files that have an extension in VALID_FONT_EXTS
            fontFiles = paths.filter(p -> fontPathHasValidFileExtension(p))
                .map(p -> scheme + p.toString()).collect(Collectors.toList());

        }
        
        return fontFiles;
    }
    
    /* helper method that loads and if necessary, logs loaded fonts */
    private static List<Font>
    checkAndLogFontsFromDirectory(List<String> fontFiles, Path dir)
    {
        final List<Font> loadedFonts;
        
        if (fontFiles != null && !fontFiles.isEmpty()) {
            loadedFonts = loadFontsFromDirectory(fontFiles);
        } else {
            System.out.printf("no font files where loaded from [ %s ]%n",
                dir.toString());
            loadedFonts = Collections.emptyList();
        }
        
        return loadedFonts;
    }
    
    /* helper method that loads and if necessary, logs loaded fonts */
    private static List<Font>
    checkAndLogFontsFromResources(List<String> fontFiles, Path dir)
    {
        final List<Font> loadedFonts;
        
        if (fontFiles != null && !fontFiles.isEmpty()) {
            loadedFonts = loadFontsFromResources(fontFiles);
        } else {
            System.out.printf("no font files where loaded from [ %s ]%n",
                dir.toString());
            loadedFonts = Collections.emptyList();
        }
        
        return loadedFonts;
    }

    /**
     * Fonts just need to be loaded in code, afterward they can be used with
     * CSS.
     *
     * @param fonts - list of the fonts to load
     */
    private static List<Font> loadFontsFromDirectory(List<String> fonts) {
        
        final List<Font> loadedFonts = new ArrayList<>(fonts.size());
        
        // iterate over list of font file strings and load them
        fonts.stream().forEach(font -> {
            if (font.endsWith(TTC_EXT)) {
                // true type collection needs to use Fonts#loadFonts
                Font[] ff = loadTTCFont(font);
                if (ff != null) {
                    loadedFonts.addAll(Arrays.asList(ff));
                }
            } else {
                // other font types can be loaded with loadFont
                Font f = loadFont(font);
                if (f != null) {
                    loadedFonts.add(f);
                }
            }
            
        });
        
        return loadedFonts;
    }
    
    /**
     * Fonts just need to be loaded in code, afterward they can be used with
     * CSS.
     *
     * @param fonts - list of the fonts to load
     */
    private static List<Font> loadFontsFromResources(List<String> fonts) {
        // used to get a reference to the FontUtils class loader
        final Class<FontUtils> loader = FontUtils.class;
        
        final List<Font> loadedFonts = new ArrayList<>(fonts.size());
        
        // iterate over list of font file strings and load them
        fonts.stream().forEach(font -> {
            
            try (final InputStream fontis = loader.getResourceAsStream(font)) {
                
                if (font.endsWith(TTC_EXT)) {
                    Font[] ff = loadTTCFont(fontis, font);
                    if (ff != null) {
                        loadedFonts.addAll(Arrays.asList(ff));
                    }
                } else {
                    Font f = loadFont(fontis, font);
                    if (f != null) {
                        loadedFonts.addAll(Arrays.asList(f));
                    }
                }
                
            } catch (IOException e) {
                // need this catch block because closing fontis could throw
                System.err.println(e.getLocalizedMessage());
                // e.printStackTrace();
            }
            
        });
        
        return loadedFonts;
    }

    // this value doesn't matter but it must be > 0
    private static final double ARBITRARY_DOUBLE = 12.0;
    
    /* loads TTC fonts from resource stream */
    private static Font[] loadTTCFont(InputStream is, String font) {
        // true type collection needs to use Font.loadFonts
        final Font[] ff = Font.loadFonts(is, ARBITRARY_DOUBLE);

        // debug
        if (ff == null) {
            logFontNotLoaded(font);
        } else {
            for (Font f : ff) {
                System.out.printf("font: [%s] has [%s]%n", font, f.toString());
            }
        }

        return ff;
    }
    
    /* loads TTC fonts from string url */
    private static Font[] loadTTCFont(String font) {
        // true type collection needs to use Font.loadFonts
        final Font[] ff = Font.loadFonts(font, ARBITRARY_DOUBLE);

        // debug
        if (ff == null) {
            logFontNotLoaded(font);
        } else {
            for (Font f : ff) {
                System.out.printf("font: [%s] has [%s]%n", font, f.toString());
            }
        }

        return ff;
    }
    
    /* loads non TTC fonts from resource stream */
    private static Font loadFont(InputStream is, String font) {
        final Font f = Font.loadFont(is, ARBITRARY_DOUBLE);

        // debug
        if (f == null) {
            logFontNotLoaded(font);
        } else {
            System.out.printf("font: [%s] has has been loaded as [%s]%n", font,
                f.toString());
        }

        return f;
    }

    /* loads non TTC fonts from string url */
    private static Font loadFont(String font) {
        final Font f = Font.loadFont(font, ARBITRARY_DOUBLE);

        // debug
        if (f == null) {
            logFontNotLoaded(font);
        } else {
            System.out.printf("font: [%s] has has been loaded as [%s]%n", font,
                f.toString());
        }

        return f;
    }
    
    /* logs that a font was not loaded */
    private static void logFontNotLoaded(String font) {
        System.out.printf("the font [ %s ] was not loaded%n", font);
    }
    
    /* helper method that validates a passed in font path's file extension */
    private static boolean fontPathHasValidFileExtension(Path fontPath) {
        for (String ext : VALID_FONT_EXTS) {
            if (fontPath.toString().endsWith(ext)) { return true; }
        }
        return false;
    }

    /* helper method that validates a path argument */
    private static Path validateDirectory(Path dirPath) {
        Path dir = Objects.requireNonNull(dirPath);
        
        // throw IAException if path does not exist or is not a directory
        throwIfPathDoesNotExist(dir);
        throwIfNonDirectory(dir);
        
        return dir;
    }
    
    /* helper method that validates a path argument */
    private static Path validateDirectory(final File dirPath) {
        final File dir = Objects.requireNonNull(dirPath);
        final Path path;

        try {
            path = dir.toPath();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                "the path [ %s ] is not a valid path", dirPath.getPath()));
        }

        return validateDirectory(path);
    }
    
    /* helper method validates a resource directory path */
    private static URI validateResourceDirectory(String resPath) {
        final String path = Objects.requireNonNull(resPath);

        final URL resource = FontUtils.class.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException(String
                .format("the resource directory [ %s ] does not exist", path));
        }

        final URI resourceUri;
        try {
            resourceUri = resource.toURI();
        } catch (URISyntaxException urise) {
            throw new IllegalArgumentException(urise.getLocalizedMessage());
        }
        
        return resourceUri;
    }
    
    /* helper method that throws an IAException if a path does not exist */
    private static void throwIfPathDoesNotExist(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(String
                .format("the path [ %s ] does not exist", path.toString()));
        }
    }
    
    /* helper method that throws if a path does not point to a driectory */
    private static void throwIfNonDirectory(Path dir) {
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException(
                String.format("the path [ %s ] does not point to a directory",
                    dir.toString()));
        }
    }
    
    
}
