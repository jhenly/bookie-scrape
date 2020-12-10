#!/usr/bin/env bash


cd ..

java --module-path "lib/javafx/15.0.1/linux" --add-modules="javafx.controls,javafx.fxml" -jar "target/bookie-scrape-1.0-SNAPSHOT-jar-with-dependencies.jar"

