package com.bookiescrape.app.fx.ui;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Class that greatly simplifies putting JavaFX applications into the system
 * tray and presents a mechanism for displaying periodic notifications.
 * <p>
 * The original class, that this class is based on, got rid of the huge headache
 * that came with putting a JavaFX application into the system tray. It involves
 * using {@code javafx}, {@code javax.swing} and {@code java.awt}.
 * <p>
 * Thankfully the GitHub user, jewelsea, uploaded their all-in-one
 * implementation as a gist to GitHub.
 * <p>
 * jewelsea's original implementation can be found here:
 * https://gist.github.com/jewelsea/e231e89e8d36ef4e5d8a
 *
 * @author jewelsea ( Original Implementation Author )
 * @author Jonathan Henly ( This Implementation Author)
 * @implNote This implementation, which supports Java 7, is a modified version
 *           of jewelsea's original GitHub gist, which supports Java 8.
 */
public class JavaFXTrayIconSample extends Application {
    
    // one icon location is shared between the application tray icon and task
    // bar icon.
    // you could also use multiple icons to allow for clean display of tray
    // icons on hi-dpi devices.
    private static final String iconImageLoc = "http://icons.iconarchive.com/icons/scafer31000/bubble-circle-3/16/GameCenter-icon.png";
    
    // application stage is stored so that it can be shown and hidden based on
    // system tray icon operations.
    private Stage stage;
    
    // a timer allowing the tray icon to provide a periodic notification event.
    private Timer notificationTimer = new Timer();
    
    // format used to display the current time in a tray icon notification.
    private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
    
    // sets up the javafx application.
    // a tray icon is setup for the icon, but the main stage remains invisible
    // until the user
    // interacts with the tray icon.
    @Override
    public void start(final Stage stage) {
        // stores a reference to the stage.
        this.stage = stage;
        
        // instructs the javafx system not to exit implicitly when the last
        // application window is shut.
        Platform.setImplicitExit(false);
        
        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    addAppToTray();
                } catch (java.awt.AWTException | IOException e) {
                    System.out.println("Unable to init system tray");
                    e.printStackTrace();
                }
            }
        });
        
        // out stage will be translucent, so give it a transparent style.
        stage.initStyle(StageStyle.TRANSPARENT);
        
        // create the layout for the javafx stage.
        StackPane layout = new StackPane(createContent());
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        layout.setPrefSize(300, 200);
        
        // a real app might have some interactive UI and a separate icon which
        // this dummy app just hides itself when the app screen is clicked.
        // hides the app window.
        layout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { stage.hide(); }
        });
        
        // a scene with a transparent fill is necessary to implement the
        // translucent app window.
        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        
        stage.setScene(scene);
    }
    
    /**
     * For this dummy app, the (JavaFX scenegraph) content, just says "hello,
     * world". A real app, might load an FXML or something like that.
     *
     * @return the main window application content.
     */
    private Node createContent() {
        Label hello = new Label("hello, world");
        hello.setStyle("-fx-font-size: 40px; -fx-text-fill: forestgreen;");
        Label instructions = new Label("(click to hide)");
        instructions.setStyle("-fx-font-size: 12px; -fx-text-fill: orange;");
        
        VBox content = new VBox(10, hello, instructions);
        content.setAlignment(Pos.CENTER);
        
        return content;
    }
    
    /**
     * Sets up a system tray icon for the application.
     *
     * @throws IOException
     * @throws AWTException
     */
    private void addAppToTray() throws IOException, AWTException {
        
        // ensure awt toolkit is initialized.
        java.awt.Toolkit.getDefaultToolkit();
        
        // app requires system tray support, just exit if there is no
        // support.
        if (!java.awt.SystemTray.isSupported()) {
            System.out.println("No system tray support, application exiting.");
            Platform.exit();
        }
        
        // set up a system tray icon.
        java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
        URL imageLoc = new URL(iconImageLoc);
        java.awt.Image image = ImageIO.read(imageLoc);
        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);
        
        // if the user double-clicks on the tray icon, show the main app
        // stage.
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(showStage());
            }
        });
        
        // if the user selects the default menu item (which includes the app
        // name), show the main app stage.
        java.awt.MenuItem openItem = new java.awt.MenuItem("hello, world");
        
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(showStage());
            }
        });
        
        // the convention for tray icons seems to be to set the default icon
        // for opening
        // the application stage in a bold font.
        java.awt.Font defaultFont = java.awt.Font.decode(null);
        java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
        openItem.setFont(boldFont);
        
        // to really exit the application, the user must go to the system
        // tray icon and select the exit option, this will shutdown JavaFX
        // and remove the tray icon (removing the tray icon will also shut
        // down AWT).
        java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationTimer.cancel();
                Platform.exit();
                tray.remove(trayIcon);
            }
            
        });
        
        // setup the popup menu for the application.
        final java.awt.PopupMenu popup = new java.awt.PopupMenu();
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        
        
        // create a timer to periodically display a notification
        TimerTask ttask = new TimerTask() {
            
            @Override
            public void run() {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        trayIcon.displayMessage("hello",
                            "The time is now " + timeFormat.format(new Date()),
                            java.awt.TrayIcon.MessageType.INFO);
                    }
                    
                });
            }
        };
        
        // schedule the timer task
        notificationTimer.schedule(ttask, 5_000, 60_000);
        
        // add the application tray icon to the system tray.
        tray.add(trayIcon);
    }
    
    /**
     * Shows the application stage and ensures that it is brought to the front
     * of all stages.
     */
    private Runnable showStage() {
        return new Runnable() {
            @Override
            public void run() {
                if (stage != null) {
                    stage.show();
                    stage.toFront();
                }
            }
        };
    }
    
    public static void main(String[] args)
        throws IOException, java.awt.AWTException {
        // Just launches the JavaFX application.
        // Due to way the application is coded, the application will remain
        // running
        // until the user selects the Exit menu option from the tray icon.
        launch(args);
    }
}
