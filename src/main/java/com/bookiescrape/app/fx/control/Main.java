package com.bookiescrape.app.fx.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bookiescrape.app.scrape.Bookie;
import com.bookiescrape.app.scrape.Scraper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


public class Main implements Initializable {
    @FXML
    private TextField outputExcelFilePathFeild;
    
    @FXML
    private Button closeBtn;
    
    private List<String> bookiesList = new ArrayList<>();
    private CellStyle style = null;
    
    @FXML
    void onCloseButtonAction(ActionEvent event) {
        // TODO minimize application (preferably to tray) rather than exiting
        Platform.exit();
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
    
    @FXML
    void startWebScrapping(ActionEvent event) {
        try {
            if (!outputExcelFilePathFeild.getText().isEmpty()) {
                scrapeWebData();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText(
                    "Please Select Output Excel File Before Start Scrapping !!!");
                alert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
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
    
    
    private static final String MONEY_LINE =
        "https://classic.sportsbookreview.com/betting-odds/money-line/";
    
    private void scrapeWebData() throws IOException {
        Scraper scraper = new Scraper();
        scraper.scrape(MONEY_LINE);
        
        System.out.println("*** Showing all Bookies list ***\n");
        
        for (Bookie bookie : scraper.getBookies()) {
            System.out.println(bookie.name() + " " + bookie.index());
        }
        
        
        /* System.out.println("*** All Team Names ***\n\n\n"); for (Element e :
         * document.select("div#booksData.data-books").select("div.teamText")) {
         * System.out.println(e.select("a.get-grid").text()); }
         *
         * System.out.println("*** NBA Data ***\n\n\n");
         *
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().size());
         *
         * System.out.println("team names\n\n\n\n");//el-div eventLine-team
         *
         * System.out.println(document.select("div.eventLines").size() +
         * " )))"); for (int i = 0; i <
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .size(); i++) {
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().get(i).children().select(
         * "div.el-div.eventLine-team").get(0).select("span.team-name").select(
         * "a").text()); }
         *
         * //opener column System.out.println("\n\n\nOpener Column"); for (int i
         * = 0; i <
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .size(); i++) {
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().get(i).children().select(
         * "div.el-div.eventLine-opener").get(0).children().select(
         * "div.eventLine-book-value").text()); }
         *
         * //frist column System.out.println("\n\n\n5D times Column"); for (int
         * i = 0; i <
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .size(); i++) {
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().get(i).children().select(
         * "div.el-div.eventLine-book").get(0).children().text()); }
         *
         * //event-holder holder-scheduled //frist column
         * System.out.println("\n\n\n5D times Column -> : " +
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .get(5).children().get(0).children().select(
         * "div.el-div.eventLine-book").parents().get(22).children().select(
         * "div.el-div.eventLine-book").text()); for (int i = 0; i <
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .size(); i++) {
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().get(i).children().get(0).children().select(
         * "div.el-div.eventLine-book").parents().get(22).children().select(
         * "div.el-div.eventLine-book").text());
         *
         * for(int j=0 ; j<
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .get(i).children().get(0).children().select(
         * "div.event-holder.holder-scheduled").size() ; j++){
         * //System.out.println(document.select("div.eventLines").get(0).
         * children().get(3).children().get(i).children().get(0).children().
         * select("div.event-holder.holder-scheduled").get(j).children().select(
         * "div.el-div.eventLine-book").get(0).children().text());
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().get(i).children().get(0).children().select(
         * "div.el-div.eventLine-book").parents().get(22).children().select(
         * "div.el-div.eventLine-book").text()); } }
         *
         *
         * for (int i = 0; i <
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .size(); i++) {
         * System.out.println(document.select("div.eventLines").get(0).children(
         * ).get(3).children().get(i).children().select(
         * "div.el-div.eventLine-book").get(0).children().text()); }
         *
         *
         *
         * System.out.println("*** All Team Names ***\n\n\n"); for (Element e :
         * document.select("div.leagueByDate")) {
         * System.out.println(e.select("div.teamText").text());
         * //System.out.println(document.select("div.eventLines").text()); for
         * (Element es :
         * document.select("div#booksData.data-books").select("div.teamText")) {
         * System.out.println(e.select("a.get-grid").text()); } } */
        
        
        // System.out.println("Final TeamNames" +
        // document.select("div.eventLines").size() + " )))");
        Set<String> linkedHashSet = new LinkedHashSet<>();
        
        /// this code is 100% working
        /* for (int i = 0; i <
         * document.select("div.eventLines").get(0).children().get(3).children()
         * .size(); i++) {
         * linkedHashSet.add(document.select("div.eventLines").get(0).children()
         * .get(3).children().get(i).children().select(
         * "div.eventLine.odd.status-scheduled").text());
         * linkedHashSet.add(document.select("div.eventLines").get(0).children()
         * .get(3).children().get(i).children().select(
         * "div.eventLine.status-scheduled").text()); } */
        /* int count = 1; for (Element e :
         * document.select("div#booksData.data-books")) {
         * //System.out.println(e.select("div.teamText").select("a.get-grid").
         * text());
         * linkedHashSet.add(e.select("div.teamText").select("a.get-grid").text(
         * )); linkedHashSet.add("start" + count); for (int i = 0; i <
         * e.select("div.eventLines").get(0).children().get(3).children().size()
         * ; i++) {
         * linkedHashSet.add(e.select("div.eventLines").get(0).children().get(3)
         * .children().get(i).children().select(
         * "div.eventLine.odd.status-scheduled").text());
         * linkedHashSet.add(e.select("div.eventLines").get(0).children().get(3)
         * .children().get(i).children().select("div.eventLine.status-scheduled"
         * ).text());
         * //System.out.println(e.select("div.eventLines").get(0).children().get
         * (3).children().get(i).children().select(
         * "div.eventLine.odd.status-scheduled").text());
         * //System.out.println(e.select("div.eventLines").get(0).children().get
         * (3).children().get(i).children().select(
         * "div.eventLine.status-scheduled").text()); } linkedHashSet.add("end"
         * + count); count++; } */
        /* for (String i : linkedHashSet) { System.out.println(i); } */
        
        /* System.out.println("All teamNames are !!!"); for (String s :
         * retrieveTeamNames(linkedHashSet)) { System.out.println(s); } */
        
        exportDataToExcelSheet(retrieveTeamNames(linkedHashSet));
    }
    
    private List<String> retrieveTeamNames(Set<String> linkedHashSet) {
        List<String> teamNames = new LinkedList<>();
        String previousString = "";
        // ScrappedData scrappedData = new ScrappedData("","","","");
        for (String s : linkedHashSet) {
            if (s.trim().startsWith("start")) {
                teamNames.add(previousString);
                // scrappedData.setGameName(previousString);
            }
            Pattern teamNamePattern =
                Pattern.compile("(?<=[p]\\s)[\\w\\s\\w]+(?=Options)");   // the
                                                                         // pattern
                                                                         // to
                                                                         // search
                                                                         // for
                                                                         // TeamNames
            Matcher teamNameMatcher = teamNamePattern.matcher(s);
            
            Pattern openerColPatter = Pattern.compile(
                "(?<=[A-Za-z]\\+|\\s\\-|\\s)[\\+|\\-\\d\\s]+(?=\\s\\d+\\.\\d\\%)");
            Matcher openerColMatcher = openerColPatter.matcher(s);
            
            Pattern allLastColPattern =
                Pattern.compile("(?<=%\\s)[\\+|\\-\\d\\s]+");
            Matcher allLoastColMatcher = allLastColPattern.matcher(s);
            
            // if we find a match, get the group
            if (teamNameMatcher.find() && openerColMatcher.find()) {
                // we're only looking for one group, so get it
                String teamNameGroupFound = teamNameMatcher.group(0);
                String openerColGroupFound = openerColMatcher.group(0);
                String allLastColGroupFound = "";
                while (allLoastColMatcher.find()) {
                    allLastColGroupFound = allLoastColMatcher.group();
                    // System.out.println(allLastColGroupFound);
                }
                
                // print the group out for verification
                // System.out.format("'%s'\n", teamNameGroupFound);
                
                teamNames.add(teamNameGroupFound + " , " + openerColGroupFound
                    + " , " + allLastColGroupFound);
                // scrappedData.setTeamName(teamNameGroupFound);
                // scrappedData.setOpnerColumn(openerColGroupFound);
                // scrappedData.setOtherColumns(allLastColGroupFound);
                
            }
            // teamNames.add(scrappedData);
            previousString = s;
            // scrappedData = new ScrappedData("","","","");
        }
        return teamNames;
    }
    
    
    private void exportDataToExcelSheet(List<String> data) throws IOException {
        // System.out.println("Exporting Data to Excel !!!");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Scrapped Data Sheet");
        
        
        //////
        workbook.createCellStyle();
        // Setting Background color
        /* style.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
         * style.setFillPattern(FillPatternType.BIG_SPOTS);
         *
         * style.setFillForegroundColor(IndexedColors.RED.getIndex());
         * style.setFillPattern(FillPatternType.SOLID_FOREGROUND); */
        
        boolean printDataFlag = false;
        int rowNum = 0;
        // System.out.println("Creating excel");
        for (int i = 0; i < data.size(); i++) {
            int colNum = 0;
            if (!data.get(i).contains(",")) {
                if (data.get(i).equals("NBA BASKETBALL")
                    || data.get(i).trim().equals("MLB BASEBALL"))
                {
                    printDataFlag = true;
                    Row row = sheet.createRow(rowNum++);
                    Cell cell = row.createCell(colNum++);
                    // cell.setCellStyle(style);
                    cell.setCellValue(data.get(i));
                    
                    row = sheet.createRow(rowNum++);
                    row.setRowStyle(style);
                    /// columns List
                    Cell teamNameCol = row.createCell(0);
                    teamNameCol.setCellValue("Team Names");
                    
                    
                    Cell openerCOOOOl = row.createCell(colNum++);
                    openerCOOOOl.setCellValue("Opener");
                    
                    for (int s = 0; s < 10; s++) {
                        Cell bookiesCol = row.createCell(colNum++);
                        bookiesCol.setCellValue(bookiesList.get(s));
                    }
                    // System.out.println(data.get(i));
                    continue;
                }
                
                /* if(data.get(i).trim().equals("MLB BASEBALL")){ printDataFlag
                 * = true; Row row = sheet.createRow(rowNum++); Cell cell =
                 * row.createCell(colNum++); cell.setCellValue(data.get(i));
                 * System.out.println(data.get(i)); continue; } */
            }
            
            if (printDataFlag) {
                if (!data.get(i).contains(",")) {
                    // System.out.println(data.get(i) + " -going to false
                    // printDataFlag");
                    printDataFlag = false;
                } else {
                    // System.out.println(data.get(i) + " -else");
                    Row row = sheet.createRow(rowNum++);
                    String[] splitTeamNameOpnerColAndOtherCols =
                        data.get(i).split(",");
                    String[] opnerCol =
                        splitTeamNameOpnerColAndOtherCols[1].trim().split(" ");
                    String[] otherColumns =
                        splitTeamNameOpnerColAndOtherCols[2].trim().split(" ");
                    
                    Cell teamNameCell = row.createCell(colNum++);
                    teamNameCell
                        .setCellValue(splitTeamNameOpnerColAndOtherCols[0]);
                    
                    StringBuilder builder = new StringBuilder();
                    for (int j = 0; j < opnerCol.length; j++) {
                        builder.append(opnerCol[j] + "\t  ");
                    }
                    Cell openerColCell = row.createCell(colNum++);
                    openerColCell.setCellValue(builder.toString());
                    
                    builder = new StringBuilder();
                    
                    int h = 1;
                    for (int k = 0; k < otherColumns.length; k++) {
                        if (k == (opnerCol.length * h)) {
                            Cell otherCOl = row.createCell(colNum++);
                            otherCOl.setCellValue(builder.toString());
                            builder = new StringBuilder();
                            h++;
                        }
                        builder.append(otherColumns[k] + "\t  ");
                    }
                    Cell otherCOl = row.createCell(colNum++);
                    otherCOl.setCellValue(otherColumns[otherColumns.length - 2]
                        + " " + otherColumns[otherColumns.length - 1]);
                }
            }
            
            /* if(lastColFlag){ Row row = sheet.createRow(rowNum++); String[]
             * splitTeamNameOpnerColAndOtherCols = data.get(i).split(",");
             * String[] opnerCol =
             * splitTeamNameOpnerColAndOtherCols[1].trim().split(" "); String[]
             * otherColumns =
             * splitTeamNameOpnerColAndOtherCols[2].trim().split(" ");
             *
             * Cell teamNameCell = row.createCell(colNum++);
             * teamNameCell.setCellValue(splitTeamNameOpnerColAndOtherCols[0]);
             *
             * StringBuilder builder = new StringBuilder(); for(int j=0 ;
             * j<opnerCol.length ; j++){ builder.append(opnerCol[j]+"\t  "); }
             * Cell openerColCell = row.createCell(colNum++);
             * openerColCell.setCellValue(builder.toString());
             *
             * builder = new StringBuilder();
             *
             * int h=1; for(int k=0 ; k<otherColumns.length ; k++){ if(k ==
             * (opnerCol.length*h)){ Cell otherCOl = row.createCell(colNum++);
             * otherCOl.setCellValue(builder.toString()); builder = new
             * StringBuilder(); h++; } builder.append(otherColumns[k]+"\t  "); }
             * lastColFlag = false; } */
        }
        // System.out.println("Row : " + rowNum);
        try {
            FileOutputStream outputStream =
                new FileOutputStream(outputExcelFilePathFeild.getText());
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // System.out.println("Done");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success !!!");
        alert.setHeaderText("Completed !!!");
        alert.setContentText(
            "All Data is scrapped to Excel Sheet Successfully !!! Thanks");
        alert.show();
    }
}
