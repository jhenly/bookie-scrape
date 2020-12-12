package com.bookiescrape.app.fx.control;

import java.io.File;

import com.bookiescrape.app.sample.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


/**
 * The controller class for {@code SettingsLayout.fxml}.
 *
 * @author Jonathan Henly
 */
public class SettingsController {

    // reference to main application
    private Main main;

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
    private void initialize() {
        
    }
    
    /**
     * Called by the main application to give a reference to itself.
     *
     * @param mainRef - reference to Main's controller
     */
    public void setMain(Main mainRef) {
        main = mainRef;

        // add observable list data to the table
        // personTable.setItems(mainApp.getPersonData());
    }
    
    @FXML
    void selectOutputExcelFilePath(ActionEvent event) {
        if (outputExcelFilePathFeild.getText() != null
            || !outputExcelFilePathFeild.getText().isEmpty())
        {
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
        fileChooser.setTitle(
            "Select Folder Where you want to save your Output Excel Sheet");
        FileChooser.ExtensionFilter emaiLFilter =
            new FileChooser.ExtensionFilter("Excel File", "*.xlsx");
        /* FileChooser.ExtensionFilter allFileFilter = new
         * FileChooser.ExtensionFilter( "All Files", "*.*"); */
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
            alert.setContentText(
                "Please Select Output Excel File Before Start Scraping !");
            alert.show();
        }
    }

}
