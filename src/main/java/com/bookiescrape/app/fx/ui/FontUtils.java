package com.bookiescrape.app.fx.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    // list of font file extensions
    private static final List<String> FONT_EXTS =
        List.of(TTF_EXT, OTF_EXT, TTC_EXT);
    
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
     * @see Font#loadFont(InputStream, double)
     * @see Font#loadFonts(InputStream, double)
     */
    public static void loadFontsInDirectory(File fontDir) {
        Path dir = validateDirectory(fontDir);
        loadFontsInDirectoryHelper(dir);
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
     * @see Font#loadFont(InputStream, double)
     * @see Font#loadFonts(InputStream, double)
     */
    public static void loadFontsInDirectory(Path fontDir) {
        Path dir = validateDirectory(fontDir);
        loadFontsInDirectoryHelper(dir);
    }
    
    /* helper method that loads fonts from a specified directory */
    private static void loadFontsInDirectoryHelper(Path dir) {
        List<String> fontFiles = null;

        // build list of font files in specified font dir
        try (Stream<Path> paths = Files.walk(dir)) {
            // only include files that have an extension in FONT_EXTS
            fontFiles =
                paths.filter(p -> FONT_EXTS.stream().anyMatch(p::endsWith))
                    .map(p -> p.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (fontFiles != null) {
            loadFonts(fontFiles);
        } else {
            System.out.printf("no font files where loaded from [ %s ].%n",
                dir.toString());
        }
    }
    
    /**
     * Fonts just need to be loaded in code, afterward they can be used with
     * CSS.
     *
     * @param fonts - list of the fonts to load
     */
    private static void loadFonts(List<String> fonts) {
        // this value doesn't matter but it must be > 0
        final double ARBITRARY_DOUBLE = 12;
        // used to get a reference to the FontUtils class loader
        final Class<FontUtils> loader = FontUtils.class;

        // iterate over list of font file strings and load them
        fonts.stream().forEach(font -> {
            
            try (InputStream fontis = loader.getResourceAsStream(font)) {
                if (font.endsWith(TTC_EXT)) {
                    // need to use loadFonts with true type collection
                    Font.loadFonts(fontis, ARBITRARY_DOUBLE);
                } else {
                    // other font types can be loaded with loadFont
                    Font.loadFont(fontis, ARBITRARY_DOUBLE);
                }
            } catch (IOException e) {
                // need this catch block because closing fontis could throw
                System.err.println(e.getLocalizedMessage());
                // e.printStackTrace();
            }

        });
        
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
    private static Path validateDirectory(File dirPath) {
        File dir = Objects.requireNonNull(dirPath);
        return validateDirectory(dir.toPath());
    }


    /* helper method that throws an IAException if a path does not exist */
    private static void throwIfPathDoesNotExist(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(String
                .format("the path [ %s ] does not exist.", path.toString()));
        }
    }

    /* helper method that throws if a path does not point to a driectory */
    private static void throwIfNonDirectory(Path dir) {
        if (!Files.exists(dir)) {
            throw new IllegalArgumentException(
                String.format("the path [ %s ] does not point to a directory.",
                    dir.toString()));
        }
    }


}
