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
     * @return this range's starting row index.
     */
    public int rowStart() {
        return range[RS];
    }
    
    /**
     * @return this range's ending row index.
     */
    public int rowEnd() {
        return range[RE];
    }

    /**
     * @return this range's starting column index.
     */
    public int colStart() {
        return range[CS];
    }
    
    /**
     * @return this range's ending column index.
     */
    public int colEnd() {
        return range[CE];
    }
    
    
    /**
     * Class method that creates a single cell range from a row and column
     * index.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param row - the row index of the cell
     * @param col - the column index of the cell
     * @return
     */
    public static CellRange singleCell(int row, int col) {
        try {
            throwIfNegativeRange(row, col);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(String
                .format("range indexes are not allowed to be negative, received"
                    + " ( %d, %d)", row, col));
        }
        return new SingleCellRange(row, col);
    }
    
    
    /**
     * Constructs an Excel sheet cell range from the supplied row and column
     * indexes.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param rs - the first row index
     * @param re - the first column index
     * @param cs - the last row index
     * @param ce - the last column index
     */
    protected CellRange(int rs, int re, int cs, int ce) {
        if ((rs == re) && (cs == ce)) {
            type = RangeType.CELL;
        } else if (rs == re) {
            type = RangeType.ROW;
        } else if (cs == ce) {
            type = RangeType.COL;
        } else {
            type = RangeType.ROW_COL;
        }
        
        try {
            range = createRangeOrThrowIfNegative(type, rs, re, cs, ce);
        } catch (IllegalArgumentException iae) {
            // catch propagated exception and add detailed message
            throw new IllegalArgumentException(String.format(
                "range indexes are not allowed to be negative, received "
                    + "( %d, %d, %d, %d )",
                rs, re, cs, ce));
        }
    }
    
    /* single cell range implementation */
    private static class SingleCellRange extends CellRange {
        SingleCellRange(int row, int col) {
            range = new int[] { row, col };
            type = RangeType.CELL;
        }
        
        @Override
        public int rowStart() { return range[R]; }

        @Override
        public int rowEnd() { return range[R]; }

        @Override
        public int colStart() { return range[C]; }

        @Override
        public int colEnd() { return range[C]; }
    }
    
    // private members
    protected RangeType type;
    protected int[] range;
    
    
    // for nested subclasses
    private CellRange() {}
    
    
    // ROW_COL type inedexes.START..END....START.....END
    private static final int RS = 0, RE = 1, CS = 2, CE = 3;

    // ROW | COL indexes..START...END..ROW.or.COL
    private static final int S = 0, E = 1, RC = 2;

    // CELL type indexes....ROW....COL
    private static final int R = 0, C = 1;
    
    
    /* helper function used by constructors work */
    private static
    int[] createRangeOrThrowIfNegative(RangeType type, int... idxs)
    {
        throwIfNegativeRange(idxs); // throws IllegalArgumentException
        // idxs length should never be less than four, but just in case
        throwIfLengthLessThanFour(idxs);

        // return allocated array based on type
        switch (type) {
            case CELL:// { ROW START, COL START }
                return new int[] { idxs[RS], idxs[CS] };
            case ROW: // { ROW START, ROW END, COL START }
                return new int[] { idxs[RS], idxs[RE], idxs[CS] };
            case COL: // { COL START, COL END, CS START }
                return new int[] { idxs[CS], idxs[CE], idxs[RS] };
            default: // only type left is ROW_COL
                // { ROW START, ROW END, COL START, COL END }
                return new int[] { idxs[RS], idxs[RE], idxs[CS], idxs[CE] };
        }

    }


    /* createRangeOrThrowIfNegative helper, throws IAE if negative */
    private static void throwIfNegativeRange(int... idxs) {
        for (int i = 0; i < idxs.length; i++) {
            // this IAE will *probably* be caught and rethrown
            if (idxs[i] < 0) { throw new IllegalArgumentException(); }
        }
    }


    /* createRangeOrThrowIfNegative helper, throws IAE if idxs.length < 4 */
    private static void throwIfLengthLessThanFour(int... idxs) {
        if (idxs.length < 4) {
            throw new IllegalArgumentException(
                "expected 4 indexes, received " + idxs.length);
        }
    }

}
