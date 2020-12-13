package com.bookiescrape.app.fx.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.CellStyle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


/**
 * The controller class for {@code RootLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class RootController {
    
    /* Package Private Constants */
    
    /** Constant representing the inactive state of a top button. */
    static int TOP_BTN_INACTIVE_STATE = 0;
    /** Constant representing the selected state of a top button. */
    static int TOP_BTN_SELECTED_STATE = 1;
    
    /* Private Constants */
    private static final String SETTINGS_BUTTON_ID = "settingsButton";
    private static final String LOG_BUTTON_ID = "historyButton";
    
    /* Private Members and FXML Members */
    
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private HBox mainTopHBox;
    @FXML
    private HBox rootMiddleHbox;
    @FXML
    private BorderPane rootViewPane;
    @FXML
    private HBox bottomRightHBox;
    
    @FXML
    private Button closeButton;
    
    @FXML
    private Button settingsButton;
    private boolean settingsActive = false;
    @FXML
    private Button logButton;
    private boolean logActive = false;
    
    @FXML
    private Button viewClose;
    @FXML
    private Label viewTitleLabel;
    
    // holds the currently selected top hbox button, if one is selected
    private Button activeTopButton;
    
    @FXML
    private Label scraperStatusLabel;
    @FXML
    private Circle scraperStatusCircle;
    
    private List<String> bookiesList = new ArrayList<>();
    private CellStyle style = null;
    
    private double stageXOffset;
    private double stageYOffset;
    
    private Parent settingsView;
    private SettingsController settingsController;
    
    /**
     * 
     * @param settingsController
     */
    public void setSettingsView(Parent settingsView,
        SettingsController settingsController) {
        this.settingsView = settingsView;
        this.settingsController = settingsController;
    }
    
    @FXML
    private void initialize() {}
    
    /**
     * Used to record the start of dragging the main window across the screen.
     *
     * @param event
     *              - the mouse pressed event caused by mouse pressing main's
     *              top HBox
     */
    @FXML
    void onMainTopHBoxMousePressed(MouseEvent event) {
        stageXOffset = event.getSceneX();
        stageYOffset = event.getSceneY();
    }
    
    /**
     * Handles dragging the window across the screen after mouse pressing main's
     * top HBox.
     *
     * @param event
     *              - the drag event caused by dragging the main window across
     *              the screen
     */
    @FXML
    void onMainTopHBoxMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();
        
        stage.setX(event.getScreenX() - stageXOffset);
        stage.setY(event.getScreenY() - stageYOffset);
    }
    
    /**
     * Handles actions coming from the top most close button.
     *
     * @param event
     *              - the action event to handle
     */
    @FXML
    void onCloseButtonAction(ActionEvent event) {
        // TODO minimize application (preferably to tray) rather than exiting
        Platform.exit();
    }
    
    /**
     * Handles actions on the top right most minimize button.
     *
     * @param event
     *              - the action event to handle
     */
    @FXML
    void onMinimizeButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene()
            .getWindow();
        
        stage.setIconified(!stage.isIconified());
    }
    
    /**
     * Handles actions on the top right most maximize button.
     *
     * @param event
     *              - the action event to handle
     */
    @FXML
    void onMaximizeButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene()
            .getWindow();
        
        stage.setMaximized(!stage.isMaximized());
    }
    
    @FXML
    void onTopButtonAction(ActionEvent event) {
        Button topButton = (Button) event.getSource();
        
        // don't do anything if the button is already active
        if (activeTopButton == topButton) { return; }
        
        // set any active top button to inactive and set top button to selected
        setActiveTopButton(topButton);
        
        // call on action method for the active button
        switch (topButton.getId()) {
            case SETTINGS_BUTTON_ID:
                settingsButtonActivated();
                rootViewPane.setCenter(settingsView);
                settingsActive = true;
                logActive = false;
                break;
            
            case LOG_BUTTON_ID:
                logButtonActivated();
                settingsActive = false;
                logActive = true;
                break;
            
            default:
                settingsActive = false;
                logActive = false;
        }
    }
    
    /* top buttons hover effect methods helper */
    private boolean topButtonIsActive(Button button) {
        return activeTopButton == button;
    }
    
    /* helper to set top buttons active */
    private void setActiveTopButton(Button button) {
        Button btn = Objects.requireNonNull(button);
        
        // if another button is active then set it to inactive
        if (activeTopButton != null) {
            changeTopButtonState(activeTopButton, TOP_BTN_INACTIVE_STATE);
        }
        
        // set the new active top hbox button and change its state to selected
        changeTopButtonState(btn, TOP_BTN_SELECTED_STATE);
        activeTopButton = btn;
    }
    
    /**
     * Changes the visible state of a specified top button.
     * <p>
     * The supported values for the parameter {@code state} follow:
     * <ul>
     * <li>{@linkplain #TOP_BTN_INACTIVE_STATE}</li>
     * <li>{@linkplain #TOP_BTN_SELECTED_STATE}</li>
     * </ul>
     * Passing a value other than those listed will produce undefined behavior.
     * <p>
     * This method assumes the passed in button has the following node
     * hierarchy:
     *
     * <pre>
     * &lt;Button&gt;
     *   &lt;Parent&gt;
     *     &lt;Node&gt; &lt;!-- Inactive State Node --&gt; &lt;/Node&gt;
     *     &lt;Node&gt; &lt;!-- Selected State Node --&gt; &lt;/Node&gt;
     *   &lt;/Parent&gt;
     * &lt;/Button&gt;
     * </pre>
     *
     * @param button
     *               - the button to change the state of, this argument cannot
     *               be {@code null}
     * @param state
     *               - the state to change to
     */
    void changeTopButtonState(Button button, int state) {
        Button btn = Objects.requireNonNull(button);
        
        // get the state nodes' parent container
        ObservableList<Node> states = ((Parent) btn.getChildrenUnmodifiable()
            .get(0)).getChildrenUnmodifiable();
        
        Node inactive = states.get(TOP_BTN_INACTIVE_STATE);
        Node selected = states.get(TOP_BTN_SELECTED_STATE);
        
        inactive.setVisible(state == TOP_BTN_INACTIVE_STATE);
        selected.setVisible(state == TOP_BTN_SELECTED_STATE);
    }
    
    /* called when top settings button is activated, shows settings view */
    private void settingsButtonActivated() {
        // TODO implement showing the settings view
        viewClose.setVisible(true);
        
        viewTitleLabel.setText("Settings");
    }
    
    /* called when top log button is activated, shows log view */
    private void logButtonActivated() {
        // TODO implement showing the log view
        viewClose.setVisible(true);
        
        viewTitleLabel.setText("Logs");
    }
    
    @FXML
    void onViewCloseAction(ActionEvent action) {
        if (activeTopButton == null) { return; }
        
        changeTopButtonState(activeTopButton, TOP_BTN_INACTIVE_STATE);
    }
    
    
    private static final String MONEY_LINE = "https://classic.sportsbookreview.com/betting-odds/money-line/";
    
}
