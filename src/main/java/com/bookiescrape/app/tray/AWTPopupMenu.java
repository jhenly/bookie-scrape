package com.bookiescrape.app.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;

/**
 * Used to show and hide an AWT PopupMenu when system tray icon is clicked.
 * 
 * @author Jonathan Henly
 */
public class AWTPopupMenu extends TrayIconPopupMenu<PopupMenu, MenuItem> {
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Shows an AWT PopupMenu when system tray icon is clicked.
     */
    public AWTPopupMenu() { super(new PopupMenu()); }
    
    /***************************************************************************
     *                                                                         *
     * Abstract Implementation                                                 *
     *                                                                         *
     **************************************************************************/
    
    /** Simple AWT MenuItem wrapper. */
    private static class AWTPopupMenuItem extends TrayIconPopupMenu.PopupMenuItem<MenuItem> {
        
        public AWTPopupMenuItem(String menuItemText) {
            super(new MenuItem(menuItemText));
        }
        
        @Override
        public void setActionListener(ActionListener listener) {
            // remove any action listeners
            for (ActionListener al : menuItem.getActionListeners()) {
                menuItem.removeActionListener(al);
            }
            if (listener != null) { menuItem.addActionListener(listener); }
        }
        
        @Override
        public void setEnabled(boolean enabled) { menuItem.setEnabled(enabled); }
        
    } // class AWTPopupMenuItem
    
    
    @Override
    protected void addMenuItemToMenu(PopupMenuItem<MenuItem> item) { getPopupMenu().add(item.getMenuItem()); }
    
    @Override
    protected void addSeparatorToMenu() { getPopupMenu().addSeparator(); }
    
    @Override
    protected PopupMenuItem<MenuItem> createAbout(String text) { return new AWTPopupMenuItem(text); }
    
    @Override
    protected PopupMenuItem<MenuItem> createRunScraper(String text) { return new AWTPopupMenuItem(text); }
    
    @Override
    protected PopupMenuItem<MenuItem> createDashboard(String text) { return new AWTPopupMenuItem(text); }
    
    @Override
    protected PopupMenuItem<MenuItem> createSettings(String text) { return new AWTPopupMenuItem(text); }
    
    @Override
    protected PopupMenuItem<MenuItem> createLogs(String text) { return new AWTPopupMenuItem(text); }
    
    @Override
    protected PopupMenuItem<MenuItem> createQuit(String text) { return new AWTPopupMenuItem(text); }
    
    
}
