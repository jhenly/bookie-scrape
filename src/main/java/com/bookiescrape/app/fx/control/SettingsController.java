package com.bookiescrape.app.fx.control;

import java.io.File;
import java.util.Objects;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


/**
 * The controller class for {@code SettingsLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class SettingsController extends MediatableController {
    /** Constant representing the inactive state of a side button. */
    static int SIDE_BTN_INACTIVE_STATE = 1;
    /** Constant representing the selected state of a side button. */
    static int SIDE_BTN_SELECTED_STATE = 0;
    
    private static final String GENERAL_BTN_ID = "generalBtn";
    private static final String GENERAL_VBOX_ID = "generalVbox";
    private static final String SCRAPER_BTN_ID = "scraperBtn";
    private static final String EXCEL_BTN_ID = "excelBtn";
    private static final String GSHEETS_BTN_ID = "gsheetsBtn";
    private static final String LOGGING_BTN_ID = "loggingBtn";
    
    @FXML
    private Button generalBtn;
    @FXML
    private VBox generalVbox;
    @FXML
    private Button scraperBtn;
    @FXML
    private VBox scraperVbox;
    @FXML
    private Button loggingBtn;
    @FXML
    private VBox loggingVbox;
    @FXML
    private Button excelBtn;
    @FXML
    private VBox excelVbox;
    @FXML
    private Button gsheetsBtn;
    @FXML
    private VBox gsheetsVbox;
    
    private Button activeSideBtn;
    private VBox activeSideVbox;
    
    @FXML
    private TextField outputExcelFilePathFeild;
    
    /**
     * Default empty constructor used by fxml.
     */
    public SettingsController() {}
    
    /**
     * Initializes the default controller class.
     * <p>
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {}
    
    @FXML
    void selectOutputExcelFilePath(ActionEvent event) {
        if (outputExcelFilePathFeild.getText() != null || !outputExcelFilePathFeild.getText().isEmpty()) {
            File positivesCsvFile = exportExcelFile();
            if (positivesCsvFile != null) {
                String anglesPath = positivesCsvFile.getAbsolutePath();
                outputExcelFilePathFeild.setText(anglesPath);
            }
        }
    }
    
    private File exportExcelFile() {
        File csvFile = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Folder Where you want to save your Output Excel Sheet");
        FileChooser.ExtensionFilter emaiLFilter = new FileChooser.ExtensionFilter("Excel File", "*.xlsx");
        /* FileChooser.ExtensionFilter allFileFilter = new FileChooser.ExtensionFilter(
         * "All Files", "*.*"); */
        fileChooser.getExtensionFilters().add(emaiLFilter);
        // fileChooser.getExtensionFilters().add(allFileFilter);
        fileChooser.setSelectedExtensionFilter(emaiLFilter);
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            csvFile = selectedFile;
        } else {
            csvFile = null;
        }
        return csvFile;
    }
    
    @FXML
    void startWebScrapping(ActionEvent event) {
        if (!outputExcelFilePathFeild.getText().isEmpty()) {
            // scrapeWebData();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Select Output Excel File Before Start Scraping !");
            alert.show();
        }
    }
    
    /** Initially start with general side button active. */
    void setGeneralInitiallyActive() {
        if (activeSideBtn != null && activeSideBtn != generalBtn) {
            // if another sid button is active then set it to inactive
            changeSideButtonState(activeSideBtn, SIDE_BTN_INACTIVE_STATE);
        }
        activeSideBtn = generalBtn;
        
        if (activeSideVbox != null && activeSideVbox != generalVbox) {
            // if another side vbox is active then set it to inactive
            activeSideVbox.setVisible(false);
        }
        activeSideVbox = generalVbox;
    }
    
    @FXML
    void onSideButtonAction(ActionEvent event) {
        Button sideBtn = (Button) event.getSource();
        
        // don't do anything if the button is already active
        if (activeSideBtn == sideBtn) { return; }
        
        // set any active side button to inactive and set side button to selected
        setActiveSideBtn(sideBtn);
        
        // set the active side vbox associated with the side button
        setActiveSideVbox(sideBtn.getId());
    }
    
    /* helper to set side vbox active */
    private void setActiveSideVbox(String sideBtnId) {
        if (activeSideVbox != null) {
            activeSideVbox.setVisible(false);
        }
        
        switch (sideBtnId) {
            case GENERAL_BTN_ID:
                activeSideVbox = generalVbox;
                break;
            case SCRAPER_BTN_ID:
                activeSideVbox = scraperVbox;
                break;
            case EXCEL_BTN_ID:
                activeSideVbox = excelVbox;
                break;
            case GSHEETS_BTN_ID:
                activeSideVbox = gsheetsVbox;
                break;
            case LOGGING_BTN_ID:
                activeSideVbox = loggingVbox;
                break;
            default:
                return;
        }
        
        activeSideVbox.setVisible(true);
    }
    
    /* helper to set side buttons active */
    private void setActiveSideBtn(Button button) {
        Button btn = Objects.requireNonNull(button);
        
        if (activeSideBtn == button) { return; }
        
        // if another button is active then set it to inactive
        if (activeSideBtn != null) {
            changeSideButtonState(activeSideBtn, SIDE_BTN_INACTIVE_STATE);
        }
        
        // set the new active side button and change its state to selected
        changeSideButtonState(btn, SIDE_BTN_SELECTED_STATE);
        activeSideBtn = btn;
    }
    
    /**
     * Changes the visible state of a specified side button.
     * <p>
     * The supported values for the parameter {@code state} follow:
     * <ul>
     * <li>{@linkplain #SIDE_BTN_INACTIVE_STATE}</li>
     * <li>{@linkplain #SIDE_BTN_SELECTED_STATE}</li>
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
    void changeSideButtonState(Button button, int state) {
        Button btn = Objects.requireNonNull(button);
        
        // get the state nodes' parent container
        ObservableList<Node> states = ((Parent) btn.getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable();
        
        Node inactive = states.get(SIDE_BTN_INACTIVE_STATE);
        Node selected = states.get(SIDE_BTN_SELECTED_STATE);
        
        inactive.setVisible(state == SIDE_BTN_INACTIVE_STATE);
        selected.setVisible(state == SIDE_BTN_SELECTED_STATE);
    }
}
