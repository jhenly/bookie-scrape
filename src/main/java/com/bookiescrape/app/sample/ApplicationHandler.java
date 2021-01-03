package com.bookiescrape.app.sample;

import java.io.IOException;

import com.bookiescrape.app.fx.FXMLReference;
import com.bookiescrape.app.fx.FontUtils;
import com.bookiescrape.app.fx.control.ControllerMediator;
import com.bookiescrape.app.fx.ui.ResizeHelper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 */
public abstract class ApplicationHandler extends Application {
    
    // the font resource path
    private static final String FONT_RES_PATH = "/fxml/font/";
    
    // fxml layout file paths
    private static final String ROOT_FXML = "/fxml/RootLayout.fxml";
    private static final String DASHBOARD_FXML = "/fxml/DashLayout.fxml";
    private static final String SETTINGS_FXML = "/fxml/SettingsLayout.fxml";
    private static final String LOG_FXML = "/fxml/LogLayout.fxml";
    
    private Stage primaryStage;
    
    private ControllerMediator controllerMediator;
    
    // used by stage's scene, set by loadFxmlAndCreateControllerMediator()
    private Parent rootView;
    
    
    /**
     * JavaFX needed no-arg constructor .
     */
    public ApplicationHandler() {}
    
    @Override
    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        try {
            // load all of the fxml files and create controller mediator
            controllerMediator = loadFxmlAndCreateControllerMediator();
            
        } catch (Exception e) {
            // TODO properly notify user of unrecoverable exception
            System.err.println(e.getMessage());
            
            e.printStackTrace();
            /* exception is unrecoverable, exit javafx */
            Platform.exit();
        }
        
        // allow system tray supporting implementing classes to do their thing
        setUpSystemTrayIfSupported();
        
        // configure and set up listeners in controller mediator
        configureControllerMediator();
        
        // load fonts from resources
        FontUtils.loadFontsFromResources(FONT_RES_PATH);
        
        // create window with no title bar or default min, max, close buttons
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(rootView));
        
        // add listener to stage for window edge resizing
        ResizeHelper.addResizeListener(primaryStage);
        
        primaryStage.show();
        
        // TODO hide stage if app should start minimized in system tray
        
        controllerMediator.requestShowDashboardView();
        
        setPrimaryStageMinBounds();
    }
    
    
    /** 
     * Loads all of the fxmls into FXMLReferences and creates a
     * ControllerMediator from them.
     * <p>
     * This must be called after the primary stage has been set.
     */
    private ControllerMediator loadFxmlAndCreateControllerMediator() throws IOException {
        // load fxml references from fxml file
        FXMLReference rootReference = loadReference(ROOT_FXML);
        FXMLReference dashReference = loadReference(DASHBOARD_FXML);
        FXMLReference settingsReference = loadReference(SETTINGS_FXML);
        FXMLReference logReference = loadReference(LOG_FXML);
        
        // need this to init stage's scene
        rootView = rootReference.getView();
        
        return new ControllerMediator(primaryStage, rootReference, dashReference, settingsReference, logReference);
    }
    
    /** Convenience helper that loads fxml references. */
    private static FXMLReference loadReference(String ref) throws IOException {
        return FXMLReference.loadFxml(ApplicationHandler.class.getResource(ref));
    }
    
    /**
     * Helper method that configures and sets up handlers in controller
     * mediator.
     */
    private void configureControllerMediator() {
        /* set application control button handlers */
        controllerMediator.setOnWindowCloseButtonActionHandler(getApplicationCloseButtonHandler());
        controllerMediator.setOnWindowMinimizeButtonActionHandler(getApplicationMinimizeButtonHandler());
        controllerMediator.setOnWindowMinimizeButtonActionHandler(getApplicationMaximizeButtonHandler());
    }
    
    /** Allow system tray supporting implementing classes to do their thing. */
    private void setUpSystemTrayIfSupported() {
        // do nothing if system tray is not supported
        if (!systemTrayIsSupported()) { return; }
        
        try {
            // call 'this' abstract method
            setUpSystemTray();
        } catch (Exception e) {
            // TODO properly log any exception
            System.err.println(e.getMessage());
        }
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
     * Returns {@code true} if the system tray is supported, otherwise 
     * {@code false}.
     * 
     * @return {@code true} if the system tray is supported, otherwise
     *         {@code false}
     */
    public abstract boolean systemTrayIsSupported();
    
    /**
     * Returns the {@code SystemTrayController} associated with this application
     * launcher if one exists, otherwise this method returns {@code null}.
     * <p>
     * The {@link #systemTrayIsSupported()} method should be used before this
     * method to check if this application launcher has an associated
     * {@code SystemTrayController}.
     * 
     * @return the {@code SystemTrayController} associated with this application
     *         launcher, or {@code null} if this application launcher does not
     *         have an associated {@code SystemTrayController}
     * @see SystemTrayController
     */
    protected abstract SystemTrayController getSystemTrayController();
    
    /**
     * Called from the {@link #start(Stage)} method after assigning primary
     * stage and loading fxml into the controller mediator.
     * <p>
     * Implementing classes that have system tray support should override this
     * method to set up the system tray.
     */
    protected abstract void setUpSystemTray();
    
    /**************************************************************************
     *                                                                        *
     * Protected API                                                          *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets the controller mediator instance.
     * @return the controller mediator
     */
    protected ControllerMediator getControllerMediator() { return controllerMediator; }
    
    /**
     * Returns an event handler that handles actions on the applications
     * minimize button.
     * <p>
     * By default the returned event handler just calls
     * {@link Stage#setIconified(boolean) Stage.setIconified(true)}.
     * @return application's minimize button action event handler
     */
    protected EventHandler<ActionEvent> getApplicationMinimizeButtonHandler() {
        return action -> getPrimaryStage().setIconified(true);
    }
    
    /**
     * Returns an event handler that handles actions on the applications
     * maximize button.
     * <p>
     * By default the returned event handler just toggles the stage's maximized
     * state.
     * @return application's maximize button action event handler
     */
    protected EventHandler<ActionEvent> getApplicationMaximizeButtonHandler() {
        return action -> getPrimaryStage().setMaximized(!getPrimaryStage().isMaximized());
    }
    
    
    /**
     * Returns an event handler that handles actions on the applications close
     * button.
     * <p>
     * By default the returned event handler just calls {@link Platform#exit()}.
     * @return application's close button action event handler
     */
    protected EventHandler<ActionEvent> getApplicationCloseButtonHandler() { return action -> Platform.exit(); }
    
    
} // class ApplicationHandler
