package com.bookiescrape.app.fx.view;

import javafx.scene.Parent;

/**
 * FXML view wrapper class with convenience methods.
 * @author Jonathan Henly
 */
public class SubView {
    
    /* private members */
    private Parent subView;
    private String subViewTitle;
    private String buttonId;
    private boolean showCloseView;
    private boolean showBottomButtons;
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Creates a {@code SubView} instance with the specified view, associated
     * root top button id and show options.
     * @param view - the parent node in the view
     * @param title - the sub view's title
     * @param topButtonId - root view's associated top button id, or
     *        {@code null}
     * @param subViewIsClosable - whether or not the root view should show the
     *        close view button when this {@code SubView} instance is showing
     * @param showCancelApply - whether or not the root view should show the
     *        cancel and apply buttons when this {@code SubView} instance is
     *        showing
     */
    public SubView(Parent view, String title, String topButtonId, boolean subViewIsClosable, boolean showCancelApply) {
        subView = view;
        subViewTitle = title;
        buttonId = topButtonId;
        showCloseView = subViewIsClosable;
        showBottomButtons = showCancelApply;
    }
    
    /**
     * Creates a {@code SubView} instance with the specified view.
     * @param view - the parent node in the view
     * @param title - the sub view's title
     */
    public SubView(Parent view, String title) { this(view, title, null, false, false); }
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets this {@code SubView} instance's parent node.
     * @return this sub view's parent node
     */
    public Parent getParentNode() { return subView; }
    
    /**
     * Gets this {@code SubView} instance's title.
     * @return this sub view's title
     */
    public String getTitle() { return subViewTitle; }
    
    /**
     * Shows this {@code SubView} instance via 
     * {@link #setVisible(boolean) setVisible(true)}.
     */
    public void show() { setVisible(true); }
    
    /**
     * Hides this {@code SubView} instance via 
     * {@link #setVisible(boolean) setVisible(false)}.
     */
    public void hide() { setVisible(false); }
    
    /**
     * Sets this {@code SubView} instance's visible state to the specified
     * boolean via {@link Parent#setVisible(boolean)}.
     * @param visible - {@code true} if this sub view should be visible,
     *        otherwise {@code false}
     */
    public void setVisible(boolean visible) { subView.setVisible(visible); }
    
    /**
     * Gets whether or not this {@code SubView} instance is showing or not.
     * @return {@code true} if this sub view is showing, otherwise {@code false}
     */
    public boolean isShowing() { return subView.isVisible(); }
    
    /**
     * Gets whether or not this {@code SubView} instance is associated with one
     * of the root view's top buttons.
     * @return {@code true} if this sub view is associated with one of the root
     *         view's top buttons, otherwise {@code false}
     */
    public boolean hasTopButtonId() { return buttonId != null; }
    
    /**
     * Gets this {@code SubView} instance's associated top button id, or
     * {@code null} if it's not associated with one of the root view's top
     * buttons.
     * @return this sub view's associated top button id, or {@code null}
     */
    public String getTopButtonId() { return buttonId; }
    
    /**
     * Gets whether or not the root view should show the close view button when
     * this {@code SubView} instance is showing.
     * @return {@code true} if the close view button should be shown, otherwise
     *         {@code false}
     */
    public boolean isClosable() { return showCloseView; }
    
    /**
     * Gets whether or not the root view should show the cancel and apply
     * buttons when this {@code SubView} instance is showing.
     * @return {@code true} if the cancel and apply buttons should be shown,
     *         otherwise {@code false}
     */
    public boolean showBottomButtons() { return showBottomButtons; }
    
    
} // class SubView
