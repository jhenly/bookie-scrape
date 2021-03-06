package com.bookiescrape.app.sample;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.fx.FXMLReference;
import com.bookiescrape.app.fx.FontUtils;
import com.bookiescrape.app.fx.control.ControllerMediator;
import com.bookiescrape.app.fx.ui.ResizeHelper;
import com.bookiescrape.app.tray.SystemTrayController;

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
import javafx.scene.image.Image;
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
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationHandler.class);
    
    // the font resource path
    private static final String FONT_RES_PATH = "../fx/view/font/";
    
    // fxml view file paths
    private static final String ROOT_FXML = "../fx/view/RootView.fxml";
    private static final String DASHBOARD_FXML = "../fx/view/DashView.fxml";
    private static final String SETTINGS_FXML = "../fx/view/SettingsView.fxml";
    private static final String LOG_FXML = "../fx/view/LogView.fxml";
    
    private Stage primaryStage;
    
    private ControllerMediator controllerMediator;
    
    // used by stage's scene, set by loadFxmlAndCreateControllerMediator()
    private Parent rootView;
    
    
    /**
     * JavaFX needed no-arg constructor .
     */
    public ApplicationHandler() {}
    
    /**
     * Gets the primary stage.
     *
     * @return the primary stage
     */
    public final Stage getPrimaryStage() { return primaryStage; }
    
    
    @Override
    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        LOG.info("loading fonts");
        // load fonts from resources
        FontUtils.loadFontsFromResources(FONT_RES_PATH);
        
        try {
            LOG.info("creating controller mediator");
            
            // load all of the fxml files and create controller mediator
            controllerMediator = loadFxmlAndCreateControllerMediator();
            
        } catch (Exception e) {
            LOG.error("unrecoverable error occurred while creating controller mediator");
            LOG.error("{}", e.getLocalizedMessage());
            
            // TODO properly notify user of unrecoverable occurred exception
            System.err.println(e.getMessage());
            
            e.printStackTrace();
            /* exception is unrecoverable, exit javafx */
            Platform.exit();
        }
        
        // allow system tray supporting implementing classes to do their thing
        setUpSystemTrayIfSupported();
        
        // create window with no title bar or default min, max, close buttons
        primaryStage.initStyle(StageStyle.UNDECORATED);
        
        // load and set all application icon dimensions
        setAllAppIconImages(primaryStage, Main.ICON_APP_BLUE);
        
        primaryStage.setScene(new Scene(rootView));
        
        // add listener to stage for window edge resizing
        ResizeHelper.addResizeListener(primaryStage);
        
        primaryStage.show();
        
        // configure and set up listeners in controller mediator
        configureControllerMediator();
        
        // TODO hide stage if app should start minimized in system tray
        controllerMediator.requestShowDashboardView();
        
        setPrimaryStageMinBounds();
        
        LOG.info("finished application launch sequence");
    }
    
    
    /** 
     * Loads all of the fxmls into FXMLReferences and creates a
     * ControllerMediator from them.
     * <p>
     * This must be called after the primary stage has been set.
     */
    private ControllerMediator loadFxmlAndCreateControllerMediator() throws IOException {
        // load fxml references from fxml view files
        LOG.info("loading root view and controller");
        FXMLReference rootReference = loadReference(ROOT_FXML);
        
        LOG.info("loading dashboard view and controller");
        FXMLReference dashReference = loadReference(DASHBOARD_FXML);
        
        LOG.info("loading settings view and controller");
        FXMLReference settingsReference = loadReference(SETTINGS_FXML);
        
        LOG.info("loading log view and controller");
        FXMLReference logReference = loadReference(LOG_FXML);
        
        // need this to init stage's scene
        rootView = rootReference.getView();
        
        if (systemTrayIsSupported() && getSystemTrayController() != null) {
            return new ControllerMediator(getApplicationMediator(), primaryStage, getSystemTrayController(),
                rootReference, dashReference, settingsReference, logReference);
        }
        
        return new ControllerMediator(getApplicationMediator(), primaryStage, rootReference, dashReference,
            settingsReference, logReference);
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
        
        LOG.info("setting up system tray");
        try {
            // call 'this' abstract method
            setUpSystemTray();
        } catch (Exception e) {
            // TODO properly log any exception
            
            LOG.error("system tray setup failed, {}", e);
            
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
    
    /** Helper that loads and sets all of the application icons */
    private static void setAllAppIconImages(Stage primaryStage, String[] iconResources) {
        List<Image> appIcons = new ArrayList<>();
        
        for (String iconString : iconResources) {
            Image iconImage = loadAppIconImage(iconString);
            if (iconImage != null) {
                appIcons.add(iconImage);
            }
        }
        
        primaryStage.getIcons().addAll(appIcons);
    }
    
    /** Helper that loads the application's icon. */
    private static Image loadAppIconImage(String resource) {
        Image iconImage = null;
        
        try (InputStream resStream = Main.class.getResourceAsStream(resource)) {
            iconImage = new Image(resStream);
        } catch (Exception e) {
            // TODO log the input stream exception
            System.err.println(e.getMessage());
        }
        
        return iconImage;
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
//
    /**
     * Gets the controller mediator instance.
     * @return the controller mediator
     */
    protected ControllerMediator getControllerMediator() { return controllerMediator; }
    
    protected abstract ApplicationMediator getApplicationMediator();
    
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
