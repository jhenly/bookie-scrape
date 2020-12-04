package com.bookiescrape.app.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    
    private static final String MAIN_FXML = "/fxml/main.fxml";
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // load fonts before loading main.fxml
        loadRobotoFont();

        URL mainFxmlUrl = getClass().getResource(MAIN_FXML);
        
        // load the main fxml file
        Parent root = FXMLLoader.load(mainFxmlUrl);
        
        // create window with no title bar or default min, max, close buttons
        primaryStage.initStyle(StageStyle.UNDECORATED);
        
        // create and set the main scene
        primaryStage.setScene(new Scene(root));
        
        // display the window
        primaryStage.show();
    }

    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        // launch the JavaFX application
        launch(args);
    }
    
    
    /**
     * Load 'src/main/resources/fxml/font/Roboto*' fonts.
     */
    private static final void loadRobotoFont() {
        List<String> fonts = new ArrayList<>();
        
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
                Font f = Font.loadFont(fontis, ARBITRARY_DOUBLE);
                
                // debugging
                System.out
                    .println("Font: " + font + "  FontName: " + f.getName());

            } catch (IOException e) {
                // need this catch block because closing fontis could throw
                System.err.println(e.getLocalizedMessage());
                // e.printStackTrace();
            }
            
        }
    }
    
    
}
