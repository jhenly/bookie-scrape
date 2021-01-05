package com.bookiescrape.app.fx.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.fx.FXMLReference;
import com.bookiescrape.app.tray.SystemTrayController;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The controller mediator handles the interactions between controller classes.
 * @author Jonathan Henly
 */
public class ControllerMediator {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerMediator.class);
    
    private static final String DASH_TITLE = "Dashboard";
    private static final String SETTINGS_TITLE = "Settings";
    private static final String LOG_TITLE = "View Logs";
    
    private Stage primaryStage;
    
    /* views and their controllers */
    private Parent rootView;
    private RootController rootController;
    private Parent dashView;
    private DashController dashController;
    private Parent settingsView;
    private SettingsController settingsController;
    private Parent logView;
    private LogController logController;
    
    // system tray controller
    private SystemTrayController sysTrayController;
    // notification controller
    private NotifierController notiController;
    
    // the currently active/showing sub view
    private Parent activeSubView;
    
    /* application window's control buttons action handlers */
    private EventHandler<ActionEvent> onWindowCloseButtonActionHandler;
    private EventHandler<ActionEvent> onWindowMinButtonActionHandler;
    private EventHandler<ActionEvent> onWindowMaxButtonActionHandler;
    
    /**************************************************************************
     *                                                                        *
     * Controller(s)                                                          *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Constructs a {@code ControllerMediator} instance.
     * @param stage - primary stage reference
     * @param root - the root fxml reference
     * @param dashboard - the dashboard fxml reference
     * @param settings - the settings fxml reference
     * @param log - the log fxml reference
     * @see ControllerMediator
     */
    public ControllerMediator(Stage stage, FXMLReference root, FXMLReference dashboard, FXMLReference settings,
        FXMLReference log) {
        this(stage, null, root, dashboard, settings, log);
    }
    
    /**
     * Constructs a {@code ControllerMediator} instance with system tray
     * support.
     * @param stage - primary stage reference
     * @param sysTray - reference to the system tray controller
     * @param root - the root fxml reference
     * @param dashboard - the dashboard fxml reference
     * @param settings - the settings fxml reference
     * @param log - the log fxml reference
     * @see ControllerMediator
     */
    public ControllerMediator(Stage stage, SystemTrayController sysTray, FXMLReference root, FXMLReference dashboard,
        FXMLReference settings, FXMLReference log) {
        this.primaryStage = stage;
        
        setRootReference(root);
        setDashboardReference(dashboard);
        setSettingsReference(settings);
        setLogReference(log);
        
        sysTrayController = sysTray;
        
        createNotificationController();
        
        LOG.warn("ControllerMediator: finished creating all mediatable instances");
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Application Control Button Handlers                                    *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Sets the action event handler that handles actions on the application
     * window's close button.
     * @param handler - handler for the application window's close button
     *        actions
     */
    public void setOnWindowCloseButtonActionHandler(EventHandler<ActionEvent> handler) {
        onWindowCloseButtonActionHandler = handler;
    }
    
    /**
     * Sets the action event handler that handles actions on the application
     * window's minimize button.
     * @param handler - handler for the application window's minimize button
     *        actions
     */
    public void setOnWindowMinimizeButtonActionHandler(EventHandler<ActionEvent> handler) {
        onWindowMinButtonActionHandler = handler;
    }
    
    /**
     * Sets the action event handler that handles actions on the application
     * window's maximize button.
     * @param handler - handler for the application window's maximize button
     *        actions
     */
    public void setOnWindowMaximizeButtonActionHandler(EventHandler<ActionEvent> handler) {
        onWindowMaxButtonActionHandler = handler;
    }
    
    /**************************************************************************
     *                                                                        *
     * Sub View Requests                                                      *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Request to change root's sub view to the settings sub view.
     */
    public void requestShowSettingsView() {
        requestShowSubView(settingsView, SETTINGS_TITLE, true, false, true);
    }
    
    /**
     * Request to change root's sub view to the view logs sub view.
     */
    public void requestShowViewLogsView() {
        requestShowSubView(logView, LOG_TITLE, true, false, false);
    }
    
    /**
     * Request to change root's sub view to the dashboard sub view.
     */
    public void requestShowDashboardView() {
        requestShowSubView(dashView, DASH_TITLE, false, true, false);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Notification Requests                                                  *
     *                                                                        *
     *************************************************************************/
    
    public void requestInfoNotify(String caption, String message) {
        notiController.infoNotify(caption, message);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Root Actions                                                           *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Called when top settings button is activated, requests to show the
     * settings sub view.
     */
    void settingsButtonSelected() {
        requestShowSettingsView();
        // showSubView(settingsView, SETTINGS_TITLE, true, false);
    }
    
    /** 
     * Called when top log button is activated, requests to show the view logs
     * sub view.
     */
    void viewLogsSelected() {
        requestShowViewLogsView();
        // showSubView(logView, LOG_TITLE, true, false);
    }
    
    /**
     * Requests to change root's sub view to the dashboard view when root's
     * close sub view button is activated.
     */
    void closeSubViewSelected() {
        requestShowDashboardView();
        // showSubView(dashView, DASH_TITLE, false, true);
    }
    
    /**
     * Handles actions coming from the top most close button.
     */
    void closeWindowSelected(ActionEvent action) {
        onWindowCloseButtonActionHandler.handle(action);
    }
    
    /**
     * Handles actions on the top right most minimize button.
     */
    void minimizeWindowSelected(ActionEvent action) {
        onWindowMinButtonActionHandler.handle(action);
        // primaryStage.setIconified(!primaryStage.isIconified());
    }
    
    /**
     * Handles actions on the top right most maximize button.
     */
    void maximizeWindowSelected(ActionEvent action) {
        onWindowMaxButtonActionHandler.handle(action);
        // primaryStage.setMaximized(!primaryStage.isMaximized());
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Dashboard Actions                                                      *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Settings Actions                                                       *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Log Actions                                                            *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Helper method that shows a specified sub view in root.
     * <p>
     * This method shows, brings to the front and centers the primary stage if
     * it's not showing. If the primary stage is showing and the specified view
     * is already active, then this method just returns.
     *  
     * @param view - the sub view to show in root
     * @param title - the sub view title to display
     * @param isClosable - whether or not to display the sub view close button
     * @param clearActiveTopBtns - whether or not to change root's top buttons
     *        to their inactive state
     * @param bottomBtns - whether or not to display root's 'Cancel' and 'Apply
     *        &amp; Restart' buttons
     */
    private void requestShowSubView(Parent view, String title, boolean isClosable, boolean clearActiveTopBtns,
        boolean bottomBtns) {
        if (!primaryStage.isShowing()) {
            showPrimaryStage(true);
        } else {
            if (subViewIsActive(view)) { return; }
        }
        
        showSubViewInRoot(view, title, isClosable, clearActiveTopBtns, bottomBtns);
    }
    
    /**
     * Shows the primary stage and brings it to the front.
     * @param centerScreen - if {@code true} centers the primary stage in the
     *        primary screen
     */
    private void showPrimaryStage(boolean centerScreen) {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
            
            if (centerScreen) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
                primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
            }
        }
    }
    
    /** Helper that changes root's sub view. */
    private void showSubViewInRoot(Parent view, String viewTitle, boolean isClosable, boolean clearActiveTopBtns,
        boolean bottomBtns) {
        
        if (view == settingsView) {
            rootController.setSettingsTopButtonActive();
        } else if (view == logView) {
            rootController.setLogsTopButtonActive();
        }
        
        rootController.setSubView(view, viewTitle, isClosable, clearActiveTopBtns, bottomBtns);
        activeSubView = view;
    }
    
    /** Helper that checks if a specified view is active. */
    private boolean subViewIsActive(Parent view) {
        return activeSubView == view;
    }
    
    /**
     * Creates the notification controller with system tray notification
     * support, unless the system tray controller is {@code null}.
     */
    private void createNotificationController() {
        if (sysTrayController != null) {
            notiController = new NotifierController(primaryStage, sysTrayController);
        } else {
            notiController = new NotifierController(primaryStage);
        }
        notiController.setControllerMediator(this);
    }
    
    /**
     * Sets the root controller and the root view.
     * @param rootReference - the root reference
     */
    private void setRootReference(FXMLReference rootReference) {
        rootView = rootReference.getView();
        rootController = rootReference.getController();
        rootController.setControllerMediator(this);
    }
    
    /**
     * Sets the dashboard controller and the dashboard view.
     * @param dashboardReference - the dashboard reference
     */
    private void setDashboardReference(FXMLReference dashboardReference) {
        dashView = dashboardReference.getView();
        dashController = dashboardReference.getController();
        dashController.setControllerMediator(this);
    }
    
    /**
     * Sets the settings controller and the settings view.
     * @param settingsReference - the settings reference
     */
    private void setSettingsReference(FXMLReference settingsReference) {
        settingsView = settingsReference.getView();
        settingsController = settingsReference.getController();
        settingsController.setControllerMediator(this);
    }
    
    /**
     * Sets the log controller and the log view.
     * @param logReference - the log reference
     */
    private void setLogReference(FXMLReference logReference) {
        logView = logReference.getView();
        logController = logReference.getController();
        logController.setControllerMediator(this);
    }
    
    
} // class ControllerMediator
