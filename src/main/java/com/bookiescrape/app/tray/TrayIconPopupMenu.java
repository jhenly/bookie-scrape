package com.bookiescrape.app.tray;

import java.awt.event.ActionListener;

/**
 * Simple abstract popup menu wrapper class used by
 * {@link SystemTrayController}.
 * 
 * @author Jonathan Henly
 *
 * @param <T> - the popup menu type
 * @param <E> - the popup menu item type
 */
public abstract class TrayIconPopupMenu<T, E> {
    private static final String ABOUT_TEXT = "About";
    private static final String RUN_SCRAPER_TEXT = "Run Scraper";
    private static final String DASHBOARD_TEXT = "Dashboard";
    private static final String SETTINGS_TEXT = "Settings";
    private static final String LOGS_TEXT = "View Logs";
    private static final String QUIT_TEXT = "Quit Bookie Scrape";
    
    static abstract class PopupMenuItem<E> {
        protected E menuItem;
        
        protected PopupMenuItem(E item) { menuItem = item; }
        
        public E getMenuItem() { return menuItem; }
        
        /**
         * Sets this menu item's action listener to a specified action
         * listener.
         * <p>
         * If the specified listener is {@code null} then this menu item
         * will not have an action listener.
         * @param listener - the action listener to set
         */
        public abstract void setActionListener(ActionListener listener);
        
        public abstract void setEnabled(boolean enabled);
        
    }
    
    protected T menu;
    protected PopupMenuItem<E> about;
    protected PopupMenuItem<E> runScraper;
    protected PopupMenuItem<E> dashboard;
    protected PopupMenuItem<E> settings;
    protected PopupMenuItem<E> logs;
    protected PopupMenuItem<E> quit;
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Creates a {@code TrayIconPopupMenu} instance with a specified popup menu.
     * <p>
     * Implementing classes must call this constructor via {@code super(...)}.
     * @param popupMenu - 
     */
    protected TrayIconPopupMenu(T popupMenu) {
        menu = popupMenu;
        
        createPopupMenuItems();
        
        addMenuItemToMenu(about);
        addMenuItemToMenu(runScraper);
        addSeparatorToMenu();
        addMenuItemToMenu(dashboard);
        addMenuItemToMenu(settings);
        addMenuItemToMenu(logs);
        addSeparatorToMenu();
        addMenuItemToMenu(quit);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Protected Abstract API                                                 *
     *                                                                        *
     *************************************************************************/
    
    protected abstract PopupMenuItem<E> createAbout(String text);
    protected abstract PopupMenuItem<E> createRunScraper(String text);
    protected abstract PopupMenuItem<E> createDashboard(String text);
    protected abstract PopupMenuItem<E> createSettings(String text);
    protected abstract PopupMenuItem<E> createLogs(String text);
    protected abstract PopupMenuItem<E> createQuit(String text);
    
    protected abstract void addMenuItemToMenu(PopupMenuItem<E> item);
    protected abstract void addSeparatorToMenu();
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets this {@code TrayIconPopupMenu} instance's popup menu.
     * @return this {@code TrayIconPopupMenu} instance's popup menu
     */
    public T getPopupMenu() { return menu; }
    
    /**
     * Specifies the enabled state of the 'Run Scraper' menu item.
     * @param enabled - the enabled state of the 'Run Scraper' menu item
     */
    public void setRunScraperEnabled(boolean enabled) { runScraper.setEnabled(enabled); };
    
    /**
     * Sets the about tray icon menu item selected action listener.
     * @param listener - the about selected listener
     */
    public void setOnAboutSelected(ActionListener listener) { about.setActionListener(listener); }
    /**
     * Sets the run scraper tray icon menu item selected action listener.
     * @param listener - the run scraper selected listener
     */
    public void setOnRunScraperSelected(ActionListener listener) { runScraper.setActionListener(listener); }
    /**
     * Sets the dashboard tray icon menu item selected action listener.
     * @param listener - the dashboard selected listener
     */
    public void setOnDashboardSelected(ActionListener listener) { dashboard.setActionListener(listener); }
    /**
     * Sets the settings tray icon menu item selected action listener.
     * @param listener - the settings selected listener
     */
    public void setOnSettingsSelected(ActionListener listener) { settings.setActionListener(listener); }
    /**
     * Sets the view logs tray icon menu item selected action listener.
     * @param listener - the view logs selected listener
     */
    public void setOnViewLogsSelected(ActionListener listener) { logs.setActionListener(listener); }
    /**
     * Sets the quit tray icon menu item selected action listener.
     * <p>
     * The quit tray icon menu item has a preset action listener that handles
     * exiting of the application, therefore changing the action listener
     * should rarely, if ever, be needed.
     * @param listener - the quit selected listener
     */
    public void setOnQuitSelected(ActionListener listener) { quit.setActionListener(listener); }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /** Creates the popup menu items. */
    private void createPopupMenuItems() {
        about = createAbout(ABOUT_TEXT);
        runScraper = createRunScraper(RUN_SCRAPER_TEXT);
        dashboard = createDashboard(DASHBOARD_TEXT);
        settings = createSettings(SETTINGS_TEXT);
        logs = createLogs(LOGS_TEXT);
        quit = createQuit(QUIT_TEXT);
    }
    
    
} // class TrayIconPopupMenu
