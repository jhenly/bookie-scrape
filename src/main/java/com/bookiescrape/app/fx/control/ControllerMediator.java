package com.bookiescrape.app.fx.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.fx.FXMLReference;
import com.bookiescrape.app.fx.view.SubView;
import com.bookiescrape.app.sample.ApplicationMediator;
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
    
    /* root sub view titles */
    private static final String DASH_TITLE = "Dashboard";
    private static final String SETTINGS_TITLE = "Settings";
    private static final String LOG_TITLE = "View Logs";
    
    private Stage primaryStage;
    
    /* root view and its controller */
    private Parent rootView;
    private RootController rootController;
    
    /* root sub views and their controllers */
    private SubView dashSubView;
    private DashController dashController;
    private SubView settingsSubView;
    private SettingsController settingsController;
    private SubView logSubView;
    private LogController logController;
    
    private ApplicationMediator appMediator;
    
    // notification controller
    private NotifierController notiController;
    
    /* application window's control buttons' action handlers */
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
     * @param appMediator - the application mediator
     * @param stage - primary stage reference
     * @param root - the root fxml reference
     * @param dashboard - the dashboard fxml reference
     * @param settings - the settings fxml reference
     * @param log - the log fxml reference
     * @see ControllerMediator
     */
    public ControllerMediator(ApplicationMediator appMediator, Stage stage, FXMLReference root, FXMLReference dashboard,
        FXMLReference settings, FXMLReference log) {
        this(appMediator, stage, null, root, dashboard, settings, log);
    }
    
    /**
     * Constructs a {@code ControllerMediator} instance with system tray
     * support.
     * @param appMediator - the application mediator
     * @param stage - primary stage reference
     * @param sysTray - reference to the system tray controller
     * @param root - the root fxml reference
     * @param dashboard - the dashboard fxml reference
     * @param settings - the settings fxml reference
     * @param log - the log fxml reference
     * @see ControllerMediator
     */
    public ControllerMediator(ApplicationMediator appMediator, Stage stage, SystemTrayController sysTray,
        FXMLReference root, FXMLReference dashboard, FXMLReference settings, FXMLReference log) {
        this.appMediator = appMediator;
        this.primaryStage = stage;
        
        // root reference must be set before any sub view reference
        setRootReference(root);
        // set sub view references
        setDashboardReference(dashboard);
        setSettingsReference(settings);
        setLogReference(log);
        
        createNotificationController(sysTray);
        
        // add sub views to root view
        rootController.addSubViewsToSubViewStackPane(dashSubView, settingsSubView, logSubView);
        
        LOG.info("finished creating all mediatable instances");
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
    public void requestShowSettingsView() { requestShowSubView(settingsSubView); }
    
    /**
     * Request to change root's sub view to the view logs sub view.
     */
    public void requestShowViewLogsView() { requestShowSubView(logSubView); }
    
    /**
     * Request to change root's sub view to the dashboard sub view.
     */
    public void requestShowDashboardView() { requestShowSubView(dashSubView); }
    
    
    /**************************************************************************
     *                                                                        *
     * Notification Requests                                                  *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Requests to notify the user with an info notification.
     * @param caption - the info notification's caption
     * @param message - the info notification's message
     */
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
     * Helper method that shows a specified sub view in the root view.
     * <p>
     * This method shows, brings to the front and centers the primary stage if
     * it's not showing. If the the specified sub view is already active, then
     * this method just returns.
     *  
     * @param subView - the sub view to show in the root view
     */
    private void requestShowSubView(SubView subView) {
        // unhide stage if hidden and bring stage to front center
        showPrimaryStage(true);
        
        // don't do anything if the sub view is already active
        if (subViewIsActive(subView)) { return; }
        
        showSubViewInRoot(subView);
        if (subView == settingsSubView) {
            // initially show general settings in settings view
            settingsController.setGeneralActive();
        }
        
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
    private void showSubViewInRoot(SubView subView) {
        rootController.showSubView(subView);
        // activeSubView = view;
    }
    
    /** Helper that checks if a specified sub view is active. */
    private boolean subViewIsActive(SubView subView) {
        return rootController.getActiveSubView() == subView;
    }
    
    /**
     * Creates the notification controller with system tray notification
     * support, unless the system tray controller is {@code null}.
     */
    private void createNotificationController(SystemTrayController trayController) {
        if (trayController != null) {
            notiController = new NotifierController(primaryStage, trayController);
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
        dashSubView = new SubView(dashboardReference.getView(), DASH_TITLE);
        dashController = dashboardReference.getController();
        dashController.setControllerMediator(this);
    }
    
    /**
     * Sets the settings controller and the settings view.
     * @param settingsReference - the settings reference
     */
    private void setSettingsReference(FXMLReference settingsReference) {
        settingsSubView
            = new SubView(settingsReference.getView(), SETTINGS_TITLE, RootController.SETTINGS_BUTTON_ID, true, true);
        settingsController = settingsReference.getController();
        settingsController.setControllerMediator(this);
    }
    
    /**
     * Sets the log controller and the log view.
     * @param logReference - the log reference
     */
    private void setLogReference(FXMLReference logReference) {
        logSubView = new SubView(logReference.getView(), LOG_TITLE, RootController.LOG_BUTTON_ID, true, false);
        logController = logReference.getController();
        logController.setControllerMediator(this);
    }
    
    
} // class ControllerMediator
