package com.bookiescrape.app.sample;

import java.net.URL;

import com.bookiescrape.app.fx.ui.ResizeHelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
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

    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) { launch(args); }
    
    
    // the resulting font directory after packaging jar
    private static final String FONT_DIR_PATH = "/fxml/font/";

    // fxml file paths
    private static final String MAIN_FXML = "/fxml/main.fxml";
    private static final String DEFAULT_FXML = "/fxml/default.fxml";
    private static final String SETTINGS_FXML = "/fxml/settings.fxml";
    private static final String LOG_FXML = "/fxml/log.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load fonts in '/fxml/font/'
        // FontUtils();

        URL mainFxmlUrl = getClass().getResource(MAIN_FXML);
        URL defaultUrl = getClass().getResource(DEFAULT_FXML);
        URL settingsUrl = getClass().getResource(SETTINGS_FXML);
        URL logUrl = getClass().getResource(LOG_FXML);

        // load the main fxml file
        Parent root = FXMLLoader.load(mainFxmlUrl);

        // load all of the view fxml's
        Parent defaultView = FXMLLoader.load(defaultUrl);
        Parent settingsView = FXMLLoader.load(settingsUrl);
        Parent logView = FXMLLoader.load(logUrl);

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

    /* method to help ensure resizing stage respects min height and width */
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

}
