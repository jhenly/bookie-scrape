package com.bookiescrape.app.sample;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
     * @param args
     *             - command line arguments
     */
    public static void main(String[] args) {
        // launch the JavaFX application
        launch(args);
    }
    
}
