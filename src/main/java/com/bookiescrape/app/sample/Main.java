package com.bookiescrape.app.sample;

import java.io.IOException;

import com.bookiescrape.app.fx.control.RootController;
import com.bookiescrape.app.fx.ui.FontUtils;
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

    // fxml layout file paths
    private static final String ROOT_FXML = "/fxml/RootLayout.fxml";
    private static final String DASHBOARD_FXML = "/fxml/DashLayout.fxml";
    private static final String SETTINGS_FXML = "/fxml/SettingsLayout.fxml";
    private static final String LOG_FXML = "/fxml/LogLayout.fxml";

    private Stage primaryStage;

    private Parent rootView;
    private Parent dashView;
    private Parent settingsView;
    private Parent logView;

    private RootController rootController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // load fonts in '/fxml/font/'
        FontUtils.loadFontsFromResources(FONT_DIR_PATH);

        // load the root layout's fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ROOT_FXML));
        rootView = loader.load();

        // create window with no title bar or default min, max, close buttons
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(rootView));

        // add listener to stage for window edge resizing
        ResizeHelper.addResizeListener(primaryStage);

        primaryStage.show();

        setPrimaryStageMinBounds();

        rootController = loader.getController();

        // initialize all of the views
        initViews();
    }

    /* initializes all of the views */
    private void initViews() throws IOException {
        // load all of the views

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(SETTINGS_FXML));
        settingsView = loader.load();

        rootController.setSettingsView(settingsView, loader.getController());


        // logView = FXMLLoader.load(getClass().getResource(LOG_FXML));
        // dashboardView =
        // FXMLLoader.load(getClass().getResource(DASHBOARD_FXML));

        initSettingsView();
    }

    private void initSettingsView() {

    }

    /* enforces window to not become smaller than root's min bounds */
    private void setPrimaryStageMinBounds() {
        // get root node's bounds to calculate min width and height
        Bounds rootBounds = rootView.getBoundsInLocal();
        double deltaW = primaryStage.getWidth() - rootBounds.getWidth();
        double deltaH = primaryStage.getHeight() - rootBounds.getHeight();

        Bounds prefBounds = getPrefBounds(rootView);
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

    /**
     * Gets the primary stage.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() { return primaryStage; }

}
