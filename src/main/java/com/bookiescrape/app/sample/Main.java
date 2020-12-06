package com.bookiescrape.app.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.bookiescrape.app.fx.ui.ResizeHelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Main application class.
 * <p>
 * This class creates and launches the JavaFX application.
 *
 * @author Jonathan Henly
 */
public class Main extends Application {
    
    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    private static final String MAIN_FXML = "/fxml/main.fxml";
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // load fonts before loading main.fxml
        loadFonts();

        URL mainFxmlUrl = getClass().getResource(MAIN_FXML);
        
        // load the main fxml file
        Parent root = FXMLLoader.load(mainFxmlUrl);
        
        // create window with no title bar or default min, max, close buttons
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));

        // add listener to stage for window edge resizing
        ResizeHelper.addResizeListener(primaryStage);
        
        primaryStage.show();
        
        // get root node's bounds to calculate min width and height
        Bounds rootBounds = root.getBoundsInLocal();
        double deltaW = primaryStage.getWidth() - rootBounds.getWidth();
        double deltaH = primaryStage.getHeight() - rootBounds.getHeight();

        Bounds prefBounds = getPrefBounds(root);
        
        primaryStage.setMinWidth(prefBounds.getWidth() + deltaW);
        primaryStage.setMinHeight(prefBounds.getHeight() + deltaH);
    }
    
    /* method to help ensure stage respects min height and width */
    private static Bounds getPrefBounds(Node node) {
        double prefWidth;
        double prefHeight;

        Orientation bias = node.getContentBias();
        if (bias == Orientation.HORIZONTAL) {
            prefWidth = node.prefWidth(-1);
            prefHeight = node.prefHeight(prefWidth);
        } else if (bias == Orientation.VERTICAL) {
            prefHeight = node.prefHeight(-1);
            prefWidth = node.prefWidth(prefHeight);
        } else {
            prefWidth = node.prefWidth(-1);
            prefHeight = node.prefHeight(-1);
        }

        return new BoundingBox(0, 0, prefWidth, prefHeight);
    }


    /**
     * Load 'src/main/resources/fxml/font/Roboto*' fonts.
     */
    private static final void loadFonts() {
        List<String> fonts = new ArrayList<>();

        // Belotta Text - Fonts
        fonts.add(toTtfFontPath("BellotaText-Regular"));
        fonts.add(toTtfFontPath("BellotaText-Bold"));
        fonts.add(toTtfFontPath("BellotaText-BoldItalic"));

        // Gelasio - Fonts
        fonts.add(toTtfFontPath("Gelasio-Regular"));
        fonts.add(toTtfFontPath("Gelasio-Medium"));
        fonts.add(toTtfFontPath("Gelasio-SemiBold"));
        fonts.add(toTtfFontPath("Gelasio-Bold"));

        // Roboto Font Family
        fonts.add(toTtfFontPath("Roboto-Regular"));
        fonts.add(toTtfFontPath("Roboto-Bold"));
        fonts.add(toTtfFontPath("Roboto-Italic"));
        fonts.add(toTtfFontPath("Roboto-BoldItalic"));
        fonts.add(toTtfFontPath("Roboto-Black"));
        fonts.add(toTtfFontPath("Roboto-BlackItalic"));
        fonts.add(toTtfFontPath("Roboto-Medium"));
        fonts.add(toTtfFontPath("Roboto-MediumItalic"));
        fonts.add(toTtfFontPath("Roboto-Light"));
        fonts.add(toTtfFontPath("Roboto-LightItalic"));
        fonts.add(toTtfFontPath("Roboto-Thin"));
        fonts.add(toTtfFontPath("Roboto-ThinItalic"));
        
        
        loadFonts(fonts);
    }

    // the resulting font directory after packaging jar
    private static final String FONT_DIR_PATH = "/fxml/font/";
    
    /* helper method */
    private static final String toTtfFontPath(String font) {
        return FONT_DIR_PATH + font + ".ttf";
    }
    
    /**
     * We just need to load them with the following method and then use CSS to
     * do the rest.
     *
     * @param fonts - list of the fonts to load
     */
    private static final void loadFonts(List<String> fonts) {
        // used in loadFont method, the value doesn't matter
        final double ARBITRARY_DOUBLE = 12;
        
        for (String font : fonts) {
            
            try (InputStream fontis = Main.class.getResourceAsStream(font)) {
                // the following method's size argument doesn't matter
                Font.loadFont(fontis, ARBITRARY_DOUBLE);

            } catch (IOException e) {
                // need this catch block because closing fontis could throw
                System.err.println(e.getLocalizedMessage());
                // e.printStackTrace();
            }
            
        }
    }
    
    
}
