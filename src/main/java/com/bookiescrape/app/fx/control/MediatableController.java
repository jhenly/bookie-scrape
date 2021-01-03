package com.bookiescrape.app.fx.control;

abstract class MediatableController {
    protected ControllerMediator conMediator;
    
    /** No argument constructor used by JavaFX */
    protected MediatableController() {}
    
    /**
     * Sets the controller mediator.
     * @param mediator - the controller mediator
     */
    protected void setControllerMediator(ControllerMediator controllerMediator) { conMediator = controllerMediator; }
    
    /**
     * Gets the controller mediator.
     * @return the controller mediator
     */
    protected ControllerMediator getControllerMediator() { return conMediator; }
    
}
