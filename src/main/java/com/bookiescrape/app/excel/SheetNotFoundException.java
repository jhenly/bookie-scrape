package com.bookiescrape.app.excel;

/**
 * Signals that an attempt to open a sheet inside of an Excel workbook failed.
 * 
 * @author Jonathan Henly
 */
public class SheetNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 273684906142676174L;
    
    /**
     * Constructs an {@code SheetNotFoundException} with {@code null} as its
     * error detail message.
     */
    public SheetNotFoundException() { super(); }
    
    /**
     * Constructs an {@code SheetNotFoundException} with the specified detail
     * message.
     *
     * @param message
     *                The detail message (which is saved for later retrieval by
     *                the {@link #getMessage()} method)
     */
    public SheetNotFoundException(String sheetName) {
        super(String.format("%s '%s'", SHEET_NOT_FOUND_MSG, sheetName));
    }
    
    private static final String SHEET_NOT_FOUND_MSG = "the Excel workbook does"
        + "not contain a sheet named";
    
    /**
     * Constructs an {@code SheetNotFoundException} with the specified detail
     * message and cause.
     *
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated into this exception's detail message.
     *
     * @param message
     *                The detail message (which is saved for later retrieval by
     *                the {@link #getMessage()} method)
     *
     * @param cause
     *                The cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public SheetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs an {@code SheetNotFoundException} with the specified cause and
     * a detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for IO exceptions that are little more than
     * wrappers for other throwables.
     *
     * @param cause
     *              The cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A null value is permitted, and
     *              indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public SheetNotFoundException(Throwable cause) { super(cause); }
}
