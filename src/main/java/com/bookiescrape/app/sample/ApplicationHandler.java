package com.bookiescrape.app.sample;

import java.io.IOException;

import com.bookiescrape.app.fx.FXMLReference;
import com.bookiescrape.app.fx.FontUtils;
import com.bookiescrape.app.fx.control.RootController;
import com.bookiescrape.app.fx.ui.ResizeHelper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 * Abstract class that's used by {@link Main} to launch and handle the
 * {@link javafx.application.Application}.
 *
 * @author Jonathan Henly
 * @see Main
 *
 */
abstract class ApplicationHandler extends Application {
    
    // the font resource path
    private static final String FONT_RES_PATH = "/fxml/font/";
    
    // fxml layout file paths
    private static final String ROOT_FXML = "/fxml/RootLayout.fxml";
    private static final String DASHBOARD_FXML = "/fxml/DashLayout.fxml";
    private static final String SETTINGS_FXML = "/fxml/SettingsLayout.fxml";
    private static final String LOG_FXML = "/fxml/LogLayout.fxml";
    
// TASK following code is used with SystemTray which does not currently work
//      on Linux/Unix
//    private SystemTrayController trayControl;

    private Stage primaryStage;
    
    private Parent rootView;
    private Parent dashView;
    private Parent settingsView;
    private Parent logView;
    
    private RootController rootController;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        // load fonts from resources
        FontUtils.loadFontsFromResources(FONT_RES_PATH);
        
        // load the root layout's fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ROOT_FXML));
        rootView = loader.load();
        
        // create window with no title bar or default min, max, close buttons
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(rootView));
        
// TASK following code is used with SystemTray which does not currently work
//      on Linux/Unix
//
//        System.out.println("PRE TRAY");
//        try {
//            trayControl = new SystemTrayController();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("POST TRAY");

        // add listener to stage for window edge resizing
        ResizeHelper.addResizeListener(primaryStage);
        
        primaryStage.show();
        
        setPrimaryStageMinBounds();
        
        rootController = loader.getController();
        rootController.setPrimaryStage(primaryStage);
        rootController.setRootView(rootView);
        
        // initialize all of the fxml files
        initFxmlReferences();
        
        // finally show the dashboard
        rootController.showDashboard();

    }
    
    @Override
    public void stop() throws Exception {
// TASK following code is used with SystemTray which does not currently work
//      on Linux/Unix
//        trayControl.shutdown();
    }
    
    /* load all of the views into FXMLReferences and give the references to the root
     * controller */
    private void initFxmlReferences() throws IOException {
        // load dashboard reference from fxml file
        FXMLReference dashReference = FXMLReference.loadFxml(getClass().getResource(DASHBOARD_FXML));
        // set dashboard reference in root
        rootController.setDashReference(dashReference);
        
        // load settings reference from fxml file
        FXMLReference settingsReference = FXMLReference.loadFxml(getClass().getResource(SETTINGS_FXML));
        // set settings reference in root
        rootController.setSettingsReference(settingsReference);
        
        // load log reference from fxml file
        FXMLReference logReference = FXMLReference.loadFxml(getClass().getResource(LOG_FXML));
        // set log reference in root
        rootController.setLogReference(logReference);
    }
    
    private void initSettingsView() {
        
        Bounds rootBounds = getPrefBounds(rootView);
        Bounds settingsBounds = getPrefBounds(settingsView);
        System.out.printf("pref root width: %.1f  height: %.1f%n", rootBounds.getWidth(), rootBounds.getHeight());
        System.out.printf("pref settings width: %.1f  height: %.1f%n", settingsBounds.getWidth(),
            settingsBounds.getHeight());
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
    
    /**
     * Method that calculates a specified node's preferred bounds.
     *
     * @param node - the node to calculate the preferred bounds of
     * @return a specified node's preferred bounds
     */
    public static Bounds getPrefBounds(Node node) {
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
    
    
    /**************************************************************************
     *                                                                        *
     * Abstract API                                                           *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Returns an event handler that handles actions on the applications
     * minimize button.
     *
     * @return application's minimize button action event handler
     */
    protected abstract EventHandler<ActionEvent> getApplicationMinimizeHandler();
    
    /**
     * Returns an event handler that handles actions on the applications close
     * button.
     *
     * @return application's close button action event handler
     */
    protected abstract EventHandler<ActionEvent> getApplicationCloseHandler();
    
    
    /**************************************************************************
     *                                                                        *
     * Protected API                                                    *
     *                                                                        *
     *************************************************************************/
    //
    /**
     * No-arg constructor solely for implementing class(es).
     */
    public ApplicationHandler() {}
}
