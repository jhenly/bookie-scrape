package com.bookiescrape.app.tray;

import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Used to show and hide a Swing JPopupMenu when system tray icon is clicked.
 * 
 * @author Jonathan Henly
 */
public final class SwingPopupMenu extends TrayIconPopupMenu<JPopupMenu, JMenuItem> {
    // tray icon reference needed to add listeners to
    private TrayIcon trayIcon;
    // hidden dialog used to hide JPopupMenu when focus lost
    private JDialog hiddenDialog;
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Shows a JPopupMenu when system tray icon is clicked.
     * @param systemTrayIcon - the system tray icon
     */
    public SwingPopupMenu(TrayIcon systemTrayIcon) {
        super(new JPopupMenu());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // TODO log swing popup menu look and feel exception
            System.err.println(e.getLocalizedMessage());
        }
        trayIcon = systemTrayIcon;
        
        createHiddenDialog();
        addTrayIconListeners();
        
        // ensure popup menu uses system look and feel
        SwingUtilities.updateComponentTreeUI(menu);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /** Initialize the hidden dialog as a headless, titleless dialog window. */
    private void createHiddenDialog() {
        hiddenDialog = new JDialog();
        hiddenDialog.setSize(10, 10);
        
        // add the window focus listener to the hidden dialog
        hiddenDialog.addWindowFocusListener(new WindowFocusListener()
        {
            @Override
            public void windowLostFocus(WindowEvent e) { hideDialog(); }
            @Override
            public void windowGainedFocus(WindowEvent e) {}
        });
    }
    
    /** Adds tray icon mouse click listeners. */
    private void addTrayIconListeners() {
        trayIcon.addMouseListener(new MouseListener()
        {
            private void showPopupMenu(MouseEvent me) {
//                if (me.isPopupTrigger()) {
                menu.setLocation(me.getX(), me.getY());
                // place the hidden dialog at the same location
                hiddenDialog.setLocation(me.getX(), me.getY());
                // now the popup menu's invoker is the hidden dialog
                menu.setInvoker(hiddenDialog);
                // show the hidden dialog to check for focus loss
                showDialog();
                menu.setVisible(true);
//                }
            }
            
            @Override
            public void mouseClicked(MouseEvent me) { showPopupMenu(me); }
            @Override
            public void mouseReleased(MouseEvent me) { showPopupMenu(me); }
            @Override
            public void mousePressed(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {}
            @Override
            public void mouseExited(MouseEvent me) {}
        });
        
        trayIcon.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("actionPerformed");
            }
        });
    }
    
    /** Hides the hidden dialog. */
    private void hideDialog() { hiddenDialog.setVisible(false); }
    
    /** Shows the hidden dialog. */
    private void showDialog() { hiddenDialog.setVisible(true); }
    
    
    /**************************************************************************
     *                                                                        *
     * Abstract Implementation                                                *
     *                                                                        *
     *************************************************************************/
    
    /** Simple Swing JMenuItem wrapper. */
    private static class SwingPopupMenuItem extends TrayIconPopupMenu.PopupMenuItem<JMenuItem> {
        private static final int ACTION_LISTENERS_TO_KEEP = 1;
        
        public SwingPopupMenuItem(String menuItemText, ActionListener hideDialog) {
            super(new JMenuItem(menuItemText));
            // ensure hidden dialog goes away after selection
            menuItem.addActionListener(hideDialog);
        }
        
        @Override
        public void setActionListener(ActionListener listener) {
            ActionListener[] listeners = menuItem.getActionListeners();
            // remove all action listeners except the initial one
            for (int i = listeners.length - 1; i > ACTION_LISTENERS_TO_KEEP; --i) {
                menuItem.removeActionListener(listeners[i]);
            }
            
            if (listener != null) { menuItem.addActionListener(listener); }
        }
        
        @Override
        public void setEnabled(boolean enabled) { menuItem.setEnabled(enabled); }
        
    } // class SwingPopupMenuItem
    
    
    @Override
    protected void addMenuItemToMenu(PopupMenuItem<JMenuItem> item) { getPopupMenu().add(item.getMenuItem()); }
    
    @Override
    protected void addSeparatorToMenu() { getPopupMenu().addSeparator(); }
    
    @Override
    protected PopupMenuItem<JMenuItem> createAbout(String text) {
        return new SwingPopupMenuItem(text, action -> hideDialog());
    }
    
    @Override
    protected PopupMenuItem<JMenuItem> createRunScraper(String text) {
        return new SwingPopupMenuItem(text, action -> hideDialog());
    }
    
    @Override
    protected PopupMenuItem<JMenuItem> createDashboard(String text) {
        return new SwingPopupMenuItem(text, action -> hideDialog());
    }
    
    @Override
    protected PopupMenuItem<JMenuItem> createSettings(String text) {
        return new SwingPopupMenuItem(text, action -> hideDialog());
    }
    
    @Override
    protected PopupMenuItem<JMenuItem> createLogs(String text) {
        return new SwingPopupMenuItem(text, action -> hideDialog());
    }
    
    @Override
    protected PopupMenuItem<JMenuItem> createQuit(String text) {
        return new SwingPopupMenuItem(text, action -> hideDialog());
    }
    
    
}
