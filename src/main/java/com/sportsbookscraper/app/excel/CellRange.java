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
        /** Denotes a range of rows. */
        ROW,
        /** Denotes a range of columns. */
        COL,
        /** Denotes a rectangular range of rows and columns. */
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
        
        if ((rs == re) && (cs == ce))
            return new SingleCellRange(rs, cs);
        else if (rs == re)
            return new RowRange(rs, cs, ce);
        else if (cs == ce)
            return new ColRange(cs, rs, re);
        
        return new RowColRange(rs, re, cs, ce);
    }
    
    /* single cell range implementation */
    private static class SingleCellRange extends CellRange {
        SingleCellRange(int row, int col) { super(row, col); }
        
        @Override
        public int rowStart() { return super.rowStart(); }
        
        @Override
        public int rowEnd() { return super.rowStart(); }
        
        @Override
        public int colStart() { return super.rowEnd(); }
        
        @Override
        public int colEnd() { return super.rowEnd(); }
    }
    
    /* row range implementation */
    private static class RowRange extends CellRange {
        RowRange(int row, int start, int end) { super(row, start, end, true); }
        
        @Override
        public int rowStart() { return super.rowStart(); }
        
        @Override
        public int rowEnd() { return super.rowStart(); }
        
        @Override
        public int colStart() { return super.rowEnd(); }
        
        @Override
        public int colEnd() { return super.colStart(); }
    }
    
    /* col range implementation */
    private static class ColRange extends CellRange {
        ColRange(int col, int start, int end) { super(col, start, end, false); }
        
        @Override
        public int rowStart() { return super.rowEnd(); }
        
        @Override
        public int rowEnd() { return super.colStart(); }
        
        @Override
        public int colStart() { return super.rowStart(); }
        
        @Override
        public int colEnd() { return super.rowStart(); }
    }
    
    /* row_col range implementation */
    private static class RowColRange extends CellRange {
        RowColRange(int rs, int re, int cs, int ce) { super(rs, re, cs, ce); }
    }
    
    public static class DirectionalCellRange extends CellRange {
        /**
         * Enum specifying the order in which rows and columns should be read.
         */
        public enum Order {
            /** Read the columns in each row. */
            ROW_THEN_COL,
            /** Read the rows in each column. */
            COL_THEN_ROW;
        }
        
        /**
         * Enum specifying the direction in which rows and columns should be
         * read.
         */
        public enum Direction {
            /** Read cells in a column from top to bottom. */
            TOP_TO_BOTTOM,
            /** Read cells in a column from bottom to top. */
            BOTTOM_TO_TOP,
            /** Read cells in a column from left to right. */
            RIGHT_TO_LEFT,
            /** Read cells in a column from right to left. */
            LEFT_TO_RIGHT;
        }
        
        /**
         * @return whether rows then columns, or columns then rows.
         */
        public Order readOrder() { return readOrder; }
        
        
        public Direction rowDirection() { return rowDirection; }
        
        public Direction colDirection() { return colDirection; }
        
        private Order readOrder;
        private Direction rowDirection;
        private Direction colDirection;
    }
    
    
    // private members
    private RangeType type;
    private int[] range;
    private int numCells;
    private int numRows;
    private int numCols;
    
    // for nested subclasses
    private CellRange() {}
    
    private CellRange(int row, int col) {
        range = new int[] { row, col };
        type = RangeType.CELL;
        numCells = numRows = numCols = 1;
    }
    
    private CellRange(int rc, int start, int end, boolean isRow) {
        range = new int[] { rc, start, end };
        type = isRow ? RangeType.ROW : RangeType.COL;
        
        numCells = start > end ? start - end + 1 : end - start + 1;
        numRows = isRow ? 1 : numCells;
        numCols = isRow ? numCells : 1;
    }
    
    private CellRange(int rs, int re, int cs, int ce) {
        range = new int[] { rs, re, cs, ce };
        type = RangeType.ROW_COL;
        
        numRows = rs > re ? rs - re + 1 : re - rs + 1;
        numCols = cs > ce ? cs - ce + 1 : ce - cs + 1;
        numCells = numRows * numCols;
    }
    
    // ROW_COL type inedexes.START..END....START.....END
    private static final int RS = 0, RE = 1, CS = 2, CE = 3;
    
    
    /* throws IAE if negative */
    private static void throwIfNegativeRange(int... idxs) {
        for (int i = 0; i < idxs.length; i++) {
            // this IAE will *probably* be caught and re-thrown
            if (idxs[i] < 0) { throw new IllegalArgumentException(); }
        }
    }
    
}
