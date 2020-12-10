@ECHO OFF

cd ..

E:\Programming\Java\jdk-15.0.1\bin\java --module-path "lib\javafx\15.0.1\winx64\lib" --add-modules="javafx.controls,javafx.fxml" -jar "target\bookie-scrape-1.0-SNAPSHOT-jar-with-dependencies.jar"

PAUSE
