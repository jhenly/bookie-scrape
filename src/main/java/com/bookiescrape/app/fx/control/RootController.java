package com.bookiescrape.app.fx.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.CellStyle;

import com.bookiescrape.app.fx.view.SubView;
import com.jhenly.juifx.control.FillButton;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


/**
 * The controller class associated with the {@code RootView.fxml} view.
 *
 * @author Jonathan Henly
 */
public class RootController extends MediatableController {
    
    /* Package Private Constants */
    
    /** Constant representing the inactive state of a top button. */
    static int TOP_BTN_INACTIVE_STATE = 0;
    /** Constant representing the selected state of a top button. */
    static int TOP_BTN_SELECTED_STATE = 1;
    /** Settings top bottom {@link Button#getId()}. */
    static final String SETTINGS_BUTTON_ID = "settingsButton";
    /** View logs top bottom {@link Button#getId()}. */
    static final String LOG_BUTTON_ID = "logButton";
    
    
    /* Private Constants */
    /* Private Members and FXML Members */
    
    /**************************************************************************
     *                                                                        *
     * FXML Injected Members                                                  *
     *                                                                        *
     *************************************************************************/
    
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private HBox mainTopHBox;
    @FXML
    private HBox topHbox; // contains top view buttons and control buttons
    @FXML
    private BorderPane rootViewPane;
    @FXML
    private StackPane subViewStackPane; // where the sub views are shown
    @FXML
    private HBox bottomButtonsHbox; // contains cancel and apply buttons
    
    @FXML
    private Button closeButton;
    @FXML
    private Button maxButton;
    @FXML
    private Button minButton;
    
    @FXML
    private Button settingsButton;
    @FXML
    private Button logButton;
    
    @FXML
    private FillButton settingsButton2;
    @FXML
    private FillButton logButton2;
    
    @FXML
    private Label subViewTitleLabel;
    @FXML
    private Button closeSubViewButton;
    
    @FXML
    private Label scraperStatusLabel;
    @FXML
    private Circle scraperStatusCircle;
    
    @FXML
    private Button cancelButton;
    @FXML
    private Button applyButton;
    
    // holds the currently selected top hbox button, if one is selected
    private Button activeTopButton;
    
    private List<String> bookiesList = new ArrayList<>();
    private CellStyle style = null;
    
    private double stageXOffset;
    private double stageYOffset;
    
    // primary stage reference
    private Stage primaryStage;
    
    // root view reference
    private Parent rootView;
    
    // reference to the actively showing sub view
    private SubView activeSubView;
    
    /**************************************************************************
     *                                                                        *
     * FXML Injected Members                                                  *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Private Members                                                        *
     *                                                                        *
     *************************************************************************/
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s) / Initializer                                           *
     *                                                                        *
     *************************************************************************/
    
    /** 
     * Root controller's constructor.
     * <p>
     * This constructor is automatically called before this controller's
     * associated view ({@code RootView.fxml}) has been loaded.
     */
    public RootController() {
        System.out.println("RootController::RootController()");
    }
    
    /**
     * Root controller's initializer.
     * <p>
     * This method is automatically called after this controller's associated
     * view ({@code RootView.fxml}) has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("RootController::initialize()");
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Package Private API                                                    *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets the root view's active sub view.
     * @return the root view's active sub view
     */
    SubView getActiveSubView() { return activeSubView; }
    
    /**
     * Adds the specified sub views to the root view's sub view stack pane.
     * <p>
     * This method simply iterates over the specified sub views and calls
     * {@link #addSubViewToSubViewStackPane(SubView)}, meaning each sub view
     * will be hidden after adding it to the stack pane.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * the {@code subViews} parameter is {@code null} or if any of the sub
     * views in {@code subViews} are {@code null}.
     * 
     * @param subViews the sub views to add
     * @throws NullPointerException if the {@code subViews} parameter is
     *         {@code null} or if any of the sub views in {@code subViews}
     *         are {@code null}
     * @see SubView
     */
    void addSubViewsToSubViewStackPane(SubView... subViews) {
        Objects.requireNonNull(subViews, "subViews reference is null.");
        
        for (SubView subView : subViews) {
            addSubViewToSubViewStackPane(subView);
        }
    }
    
    /**
     * Adds a specified sub view to the root view's sub view stack pane.
     * <p>
     * This method hides the sub view after adding it to the stack pane.
     * <p>
     * <b>Note:</b> this method will throw a {@code NullPointerException} if
     * the specified sub view is {@code null}.
     * 
     * @param subView - the sub view to add
     * @throws NullPointerException if the passed in sub view is {@code null}
     * @see SubView
     */
    void addSubViewToSubViewStackPane(SubView subView) {
        Objects.requireNonNull(subView, "cannot add a null sub view.");
        
        subViewStackPane.getChildren().add(subView.getParentNode());
        // hide the sub view
        subView.hide();
    }
    
    /**
     * Shows a specified sub view in the root view's sub view
     * {@code StackPane}.
     * <p>
     * This method also sets the active sub view, changes the sub view title
     * label, changes the active top button, shows the sub view close button
     * if applicable, and shows the cancel and apply buttons if applicable.
     * <p>
     * <b>Note:</b> the specified sub view should be added to the root view's
     * sub view {@code StackPane}, via
     * {@link #addSubViewToSubViewStackPane(SubView)}, prior to calling this
     * method.
     * 
     * @param subView - the sub view to show in the root view
     * @throws NullPointerException if the specified sub view is {@code null}
     */
    void showSubView(SubView subView) {
        Objects.requireNonNull(subView, "cannot show a null sub view.");
        
        // view already showing is handled in controller mediator
        switchActiveSubView(subView);
        
        closeSubViewButton.setVisible(subView.isClosable());
        subViewTitleLabel.setText(subView.getTitle());
        bottomButtonsHbox.setVisible(subView.showBottomButtons());
    }
    
    /** Switches any active view with the passed in view. */
    private void switchActiveSubView(SubView subView) {
        // hide any active view
        if (activeSubView != null) { activeSubView.hide(); }
        
        activeSubView = subView;
        activeSubView.show();
        
        // switch active top button to view's associated top button
        switchActiveTopButton(subView.getTopButtonId());
    }
    
    private void switchActiveTopButton(String buttonId) {
        // clear any active top button and return if button id is null
        if (buttonId == null) {
            setActiveTopButton(null);
            return;
        }
        
        // activate the top button associated with the button id
        switch (buttonId) {
            case SETTINGS_BUTTON_ID:
                setActiveTopButton(settingsButton);
                break;
            
            case LOG_BUTTON_ID:
                setActiveTopButton(logButton);
                break;
            
            default:
        }
    }
    
    /* helper to set top buttons active */
    private void setActiveTopButton(Button button) {
        // don't do anything if the button is already active
        if (activeTopButton == button) { return; }
        
        // if another button is active then set it to inactive
        deactivateActiveTopButton();
        
        // set the new active top hbox button and change its state to selected
        activeTopButton = button;
        if (button != null) { changeTopButtonState(button, TOP_BTN_SELECTED_STATE); }
    }
    
    /** Deactivates any active top button and sets activeTopButton to null. */
    private void deactivateActiveTopButton() {
        if (activeTopButton != null) {
            changeTopButtonState(activeTopButton, TOP_BTN_INACTIVE_STATE);
            // activeTopButton.pseudoClassStateChanged();
            activeTopButton = null;
        }
    }
    
    @FXML
    void onTopButtonAction(ActionEvent event) {
        Button topButton = (Button) event.getSource();
        
        // don't do anything if the button is already active
        if (activeTopButton == topButton)
            return;
            
        // set any active top button to inactive and set top button to selected
        // setActiveTopButton(topButton);
        
        // notify controller mediator that a top button was pressed
        switch (topButton.getId()) {
            case SETTINGS_BUTTON_ID:
                getControllerMediator().settingsButtonSelected();
                break;
            
            case LOG_BUTTON_ID:
                getControllerMediator().viewLogsSelected();
                break;
            
            default: // we should never reach this point
        }
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
     * @param button - the button to change the state of, this argument cannot
     *        be {@code null}
     * @param state - the state to change to
     */
    void changeTopButtonState(Button button, int state) {
        Button btn = Objects.requireNonNull(button);
        
        // get the state nodes' parent container
        ObservableList<Node> states = ((Parent) btn.getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable();
        
        Node inactive = states.get(TOP_BTN_INACTIVE_STATE);
        Node selected = states.get(TOP_BTN_SELECTED_STATE);
        
        inactive.setVisible(state == TOP_BTN_INACTIVE_STATE);
        selected.setVisible(state == TOP_BTN_SELECTED_STATE);
    }
    
    /**
     * Used to record the start of dragging the main window across the screen.
     *
     * @param event - the mouse pressed event caused by mouse pressing main's
     *        top HBox
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
     * @param event - the drag event caused by dragging the main window across
     *        the screen
     */
    @FXML
    void onMainTopHBoxMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();
        
        stage.setX(event.getScreenX() - stageXOffset);
        stage.setY(event.getScreenY() - stageYOffset);
    }
    
    /**
     * Handles actions coming from the top most close button.
     * @param action - the action event to handle
     */
    @FXML
    void onCloseButtonAction(ActionEvent action) { getControllerMediator().closeWindowSelected(action); }
    
    /**
     * Handles actions on the top right most minimize button.
     * @param action - the action event to handle
     */
    @FXML
    void onMinimizeButtonAction(ActionEvent action) { getControllerMediator().minimizeWindowSelected(action); }
    
    /**
     * Handles actions on the top right most maximize button.
     * @param action - the action event to handle
     */
    @FXML
    void onMaximizeButtonAction(ActionEvent action) { getControllerMediator().maximizeWindowSelected(action); }
    
    
    @FXML
    void onViewCloseAction(ActionEvent action) { getControllerMediator().closeSubViewSelected(); }
    
    @FXML
    void onCloseSubViewAction(ActionEvent action) { getControllerMediator().closeSubViewSelected(); }
    
    private static final String MONEY_LINE = "https://classic.sportsbookreview.com/betting-odds/money-line/";
    
    /* enforces window to not become smaller than root's min bounds */
    private void setPrimaryStageMinBounds() {
        // get root and active view's bounds to calculate min width and height
        Bounds rootPrefBounds = getPrefBounds(rootView);
        Bounds activePrefBounds = getPrefBounds(activeSubView.getParentNode());
        
        double minWidth = (rootPrefBounds.getWidth() - activePrefBounds.getWidth() > 0.0) ? rootPrefBounds.getWidth()
            : activePrefBounds.getWidth();
        double minHeight = rootPrefBounds.getHeight() + activePrefBounds.getHeight();
        
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
    }
    
    private void printBounds(String name, Bounds toPrint) {
        System.out.printf("%s bounds -  width: %.1f  height: %.1f%n", name, toPrint.getWidth(), toPrint.getHeight());
        double deltaW = primaryStage.getWidth() - toPrint.getWidth();
        double deltaH = primaryStage.getHeight() - toPrint.getHeight();
        System.out.printf("%s deltas -  deltaW: %.1f  deltaH: %.1f%n", name, deltaW, deltaH);
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
    
}
