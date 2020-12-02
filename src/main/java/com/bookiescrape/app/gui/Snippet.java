package com.bookiescrape.app.gui;

import java.util.Random;

import javafx.application.Application;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Snippet extends Application {
    
    // maintain a strong reference to the service
    private UpdateCheckService service;
    
    @Override
    public void start(Stage primaryStage) {
        service = new UpdateCheckService();
        service.setPeriod(Duration.seconds(5));
        
        Label resultLabel = new Label();
        
        // service.setOnRunning(e -> resultLabel.setText(null));
        service.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                resultLabel.setText(null);
            }
        });
        
        /* service.setOnSucceeded(e -> { if (service.getValue()) {
         * resultLabel.setText("UPDATES AVAILABLE"); } else {
         * resultLabel.setText("UP-TO-DATE"); } }); */
        
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (service.getValue()) {
                    resultLabel.setText("UPDATES AVAILABLE");
                } else {
                    resultLabel.setText("UP-TO-DATE");
                }
            }
        });
        
        
        Label msgLabel = new Label();
        msgLabel.textProperty().bind(service.messageProperty());
        
        ProgressBar progBar = new ProgressBar();
        progBar.setMaxWidth(Double.MAX_VALUE);
        progBar.progressProperty().bind(service.progressProperty());
        progBar.visibleProperty()
            .bind(service.stateProperty().isEqualTo(State.RUNNING));
        
        VBox box = new VBox(3, msgLabel, progBar);
        box.setMaxHeight(Region.USE_PREF_SIZE);
        box.setPadding(new Insets(3));
        
        StackPane root = new StackPane(resultLabel, box);
        StackPane.setAlignment(box, Pos.BOTTOM_LEFT);
        
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
        
        service.start();
    }
    
    private static class UpdateCheckService extends ScheduledService<Boolean> {
        
        @Override
        protected Task<Boolean> createTask() {
            double prob = Math.random();
            double ratio = 3.0;
            
            if (prob > 2 / ratio) {
                return createRegularTask();
            } else if (prob > 1 / ratio) {
                return createLinearTask();
            } else {
                return createBumpyTask();
            }
            
        }
        
        private Task<Boolean> createRegularTask() {
            return new Task<>() {
                
                @Override
                protected Boolean call() throws Exception {
                    
                    updateMessage("Regular Task - Checking for updates...");
                    for (int i = 0; i < 1000; i++) {
                        
                        updateProgress(i + 1, 1000);
                        Thread.sleep(1L); // fake time-consuming work
                        updateMessage(
                            "Regular Task - Checking for updates... Checked "
                                + i + "/1000 packages");
                    }
                    // 50-50 chance updates are "available"
                    return Math.random() < 0.5;
                }
            };
        }
        
        
        private Task<Boolean> createLinearTask() {
            return new Task<>() {
                
                @Override
                protected Boolean call() throws Exception {
                    
                    updateMessage("Linear Task - Checking for updates...");
                    for (int i = 0; i < 1000; i++) {
                        updateProgress(i + 1, 1000);
                        if (i % 100 == 0) {
                            Thread.sleep(100L); // fake time-consuming work
                            updateMessage(
                                "Linear Task - Checking for updates... Checked "
                                    + i + "/1000 packages");
                        }
                    }
                    // 50-50 chance updates are "available"
                    return Math.random() < 0.5;
                }
            };
            
        }
        
        static Random rand;
        
        static int tenToFifty() {
            if (rand == null) {
                rand = new Random();
            }
            
            return rand.nextInt(40) + 10;
        }
        
        private Task<Boolean> createBumpyTask() {
            return new Task<>() {
                
                @Override
                protected Boolean call() throws Exception {
                    int max = 1000;
                    int amount = 0;
                    long sleep = 0L;
                    int total = 0;
                    
                    updateMessage("Bumpy Task - Checking for updates...");
                    while (total < max) {
                        amount = (max - total > 50) ? tenToFifty()
                            : max - total;
                        
                        total += amount;
                        
                        updateProgress(total, max);
                        
                        Thread.sleep(amount * 10); // fake time-consuming work
                        updateMessage(
                            "Bumpy Task - Checking for updates... Checked "
                                + total + "/1000 packages");
                    }
                    
                    // 50-50 chance updates are "available"
                    return Math.random() < 0.5;
                }
            };
            
        }
        
        
    }
    
}

