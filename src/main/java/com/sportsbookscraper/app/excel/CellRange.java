package com.sportsbookscraper.app.excel;

/**
 * Represents a range of cells in an Excel sheet.
 * <p>
 * The main characteristic of a cell range is its type, which can be one of four
 * {@linkplain RangeType} enums.
 *
 * @author Jonathan Henly
 * @see RangeType
 */
public class CellRange {
    
    /**
     * Enum used by the {@linkplain CellRange} class, among other classes, to
     * denote the characteristics of the range.
     * <p>
     * The four possible {@code RangeType} enums, and what they denote, are:<br>
     * <blockquote> {@link RangeType#CELL CELL} - a single cell, with only a row
     * and column index. <br>
     * {@link RangeType#ROW ROW} - a range of rows, with a starting and ending
     * row index and a starting column index. <br>
     * {@link RangeType#COL COL} - a range of cols, with a starting and ending
     * col index and a starting row index. <br>
     * {@link RangeType#ROW_COL ROW_COL} - a rectangular range, with both row
     * and column indexes. </blockquote>
     *
     * @author Jonathan Henly
     * @see CellRange
     */
    public enum RangeType {
        /** Denotes a single cell range. */
        CELL,
        /** Denotes a range of cells in a row. */
        ROW,
        /** Denotes a range of cells in a column. */
        COL,
        /** Denotes a rectangular range of cells in rows and columns. */
        ROW_COL;
    }
    
    
    /**
     * @return this range's starting row index
     */
    public int rowStart() { return range[RS]; }
    
    /**
     * @return this range's ending row index
     */
    public int rowEnd() { return range[RE]; }
    
    /**
     * @return this range's starting column index
     */
    public int colStart() { return range[CS]; }
    
    /**
     * @return this range's ending column index
     */
    public int colEnd() { return range[CE]; }
    
    /**
     * @return this range's {@link RangeType}
     */
    public RangeType type() { return type; }
    
    /**
     * @return the number of cells in this range
     */
    public int numCells() { return numCells; }
    
    /**
     * @return the number of rows in this range
     */
    public int numRows() { return numRows; }
    
    /**
     * @return the number of columns in this range
     */
    public int numCols() { return numCols; }
    
    
    /**
     * Creates a single cell range from a row and column index.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param row
     *            - the row index of the cell
     * @param col
     *            - the column index of the cell
     * 
     * @return a cell range representing only a single cell
     */
    public static CellRange singleCell(int row, int col) {
        return range(row, row, col, col);
    }
    
    /**
     * Creates a cell range from a row index and the starting and ending column
     * indexes, representing a range of cells in a row.
     * <p>
     * Note: if {@code start} equals {@code end}, then this method will return
     * the same {@code CellRange} as {@link CellRange#singleCell(row, start)}.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param row
     *              - the row index
     * @param start
     *              - the starting column index
     * @param end
     *              - the ending column index
     * 
     * @return a range of cells in a specified row
     */
    public static CellRange rowRange(int row, int start, int end) {
        return range(row, row, start, end);
    }
    
    /**
     * Creates a cell range from a column index and the starting and ending row
     * indexes, representing a range of cells in a column.
     * <p>
     * Note: if {@code start} equals {@code end}, then this method will return
     * the same {@code CellRange} as {@link CellRange#singleCell(col, start)}.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param col
     *              - the column index
     * @param start
     *              - the starting row index
     * @param end
     *              - the ending row index
     * 
     * @return a range of cells in a specified column
     */
    public static CellRange colRange(int col, int start, int end) {
        return range(start, end, col, col);
    }
    
    /**
     * Creates a cell range from a starting and ending column index, and a row
     * index.
     * <p>
     * Note: if {@code rs} equals {@code re} and {@code cs} equals {@code ce},
     * then this method will return the same {@code CellRange} as
     * {@link CellRange#singleCell(rs, cs)}.
     * <p>
     * Note: if {@code rs} equals {@code re} and {@code cs} does not equal
     * {@code ce}, then this method will return the same {@code CellRange} as
     * {@link CellRange#rowRange(rs, cs, ce)}.
     * <p>
     * Note: if {@code cs} equals {@code ce} and {@code rs} does not equal
     * {@code re}, then this method will return the same {@code CellRange} as
     * {@link CellRange#colRange(cs, rs, re)}.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param rs
     *           - the starting row index of the range
     * @param re
     *           - the ending row index of the range
     * @param cs
     *           - the starting column index of the range
     * @param ce
     *           - the ending column index of the range
     * 
     * @return a rectangular range of cells
     */
    public static CellRange range(int rs, int re, int cs, int ce) {
        try {
            throwIfNegativeRange(rs, re, cs, ce);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(String
                .format("range indexes are not allowed to be negative, received"
                    + " ( %d, %d, %d, %d)", rs, re, cs, ce));
        }
        
        return new CellRange(rs, re, cs, ce);
    }
    
    
    // private members
    private RangeType type;
    private int[] range;
    private int numCells;
    private int numRows;
    private int numCols;
    
    // for nested subclasses
    private CellRange() {}
    
    // used by static creators
    private CellRange(int rs, int re, int cs, int ce) {
        range = new int[] { rs, re, cs, ce };
        if ((rs == re) && (cs == ce)) {
            type = RangeType.CELL;
        } else if (rs == re) {
            type = RangeType.ROW;
        } else if (cs == ce) {
            type = RangeType.COL;
        } else {
            type = RangeType.ROW_COL;
        }
        
        numRows = rs > re ? rs - re + 1 : re - rs + 1;
        numCols = cs > ce ? cs - ce + 1 : ce - cs + 1;
        numCells = numRows * numCols;
    }
    
    // used by static creators
    private CellRange(CellRange copy) {
        type = copy.type;
        numCells = copy.numCells;
        numRows = copy.numRows;
        numCols = copy.numCols;
        range = copy.range;
    }
    
    // ROW_COL type indexes..START...END.....START...END
    private static final int RS = 0, RE = 1, CS = 2, CE = 3;
    
    
    /* throws IAE if negative */
    private static void throwIfNegativeRange(int... idxs) {
        for (int i = 0; i < idxs.length; i++) {
            // this IAE will *probably* be caught and re-thrown
            if (idxs[i] < 0) { throw new IllegalArgumentException(); }
        }
    }
    
}
