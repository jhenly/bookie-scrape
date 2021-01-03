package com.bookiescrape.app.fx;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * Contains FXML references loaded from a {@linkplain FXMLLoader}.
 * <p>
 * The contained FXML references are the loaded view and the loaded view's
 * controller.
 *
 * @author Jonathan Henly
 * @see FXMLLoader#load()
 */
public class FXMLReference {
    private URL fxmlUrl;
    private Parent view;
    private Object controller;
    
    /* loads an fxml view and controller */
    private FXMLReference(URL url, FXMLLoader loader) throws IOException {
        fxmlUrl = url;
        view = loader.load();
        controller = loader.getController();
    }
    
    /**
     * Creates an instance of this class from calling
     * {@code FXMLLoader.setLocation(fxmlUrl)} and {@code FXMLLoader.load()}.
     *
     * @param fxmlUrl - the URL of the {@code .fxml} file to load
     * @return an instance of this class containing a reference to the loaded
     *         view and its controller
     * @throws IOException if an I/O exception occurs from calling
     *         {@linkplain FXMLLoader#load()}
     * @see FXMLLoader#load()
     */
    public static FXMLReference loadFxml(URL fxmlUrl) throws IOException {
        URL url = Objects.requireNonNull(fxmlUrl);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(url);
        
        FXMLReference ref = new FXMLReference(url, fxmlLoader);
        
        return ref;
    }
    
    /**
     * Returns the controller associated with the loaded view.
     *
     * @param <T> the type of the controller
     * @return the controller associated with the loaded view
     * @see FXMLLoader#load()
     * @see FXMLLoader#getController()
     */
    @SuppressWarnings("unchecked")
    public <T> T getController() { return (T) controller; }
    
    /**
     * Returns the view loaded from {@code FXMLLoader.load()}.
     *
     * @return the loaded view
     * @see FXMLLoader#load()
     */
    public Parent getView() { return view; }
    
    /**
     * Gets the URL used to load this {@code FXMLReference} instance.
     *
     * @return the URL used to load this {@code FXMLReference} instance
     */
    public URL getUrl() { return fxmlUrl; }
    
}

