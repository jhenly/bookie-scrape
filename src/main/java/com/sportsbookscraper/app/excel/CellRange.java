package com.sportsbookscraper.app.excel;

import java.util.ArrayList;


/**
 * Represents a range of cells in an Excel sheet.
 *
 * @author Jonathan Henly
 */
public class CellRange {

    /**
     * Signals what kind of range this is, i.e. a ROW range, or a ROW_COL, etc.
     */
    enum RangeType { CELL, ROW, COL, ROW_COL; }
    
    // ROW_COL type inedexes.START..END....START.....END
    private static final int RS = 0, RE = 1, CS = 2, CE = 3;

    // ROW type indexes....START...END..ROW.or.COL
    private static final int S = 0, E = 1, RC = 2;

    // CELL type indexes....ROW....COL
    private static final int R = 0, C = 1;
    
    
    private RangeType type;
    private int[] range;


    /**
     * Constructs an Excel sheet cell range from the supplied row and column
     * indexes.
     * <p>
     * <b>Note:</b> in Excel row and column indexes start at 1, but this class
     * starts row and column indexes at 0 (i.e. they are zero-indexed).
     *
     * @param rs - the first row index
     * @param cs - the first column index
     * @param re - the last row index
     * @param ce - the last column index
     */
    public CellRange(int rs, int re, int cs, int ce) {
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
            // perform check and get range if valid
            range = createRangeOrThrowIfNegative(type, rs, re, cs, ce);
        } catch (IllegalArgumentException iae) {
            // catch propagated exception and add detailed message
            throw new IllegalArgumentException(String.format(
                "range indexes are not allowed to be negative, received "
                    + "( %d, %d, %d, %d )",
                rs, re, cs, ce));
        }
    }

    /* */
    private static
    int[] createRangeOrThrowIfNegative(RangeType t, int... idxs)
    {
        throwIfNegativeRange(idxs); // throws IllegalArgumentException
        
        // idxs length should never be less than four, but just in case
        throwIfLengthLessThanFour(idxs);
        
        // return allocated array based
        switch (t) {
            case CELL:// { ROW START, COL START }
                return new int[] { idxs[RS], idxs[CS] };
            case ROW: // { ROW START, ROW END, COL START }
                return new int[] { idxs[RS], idxs[RE], idxs[CS] };
            case COL: // { COL START, COL END, ROW START }
                return new int[] { idxs[CS], idxs[CE], idxs[RS] };
            default: // only type left is ROW_COL
                // { ROW START, ROW END, COL START, COL END }
                return new int[] { idxs[RS], idxs[RE], idxs[CS], idxs[CE] };
        }
        
    }
    
    /* createRangeOrThrowIfNegative helper, throws IAE if negative */
    private static void throwIfNegativeRange(int... idxs) {
        for (int i = 0; i < idxs.length; i++) {
            if (idxs[i] < 0) { throw new IllegalArgumentException(); }
            ArrayList<String> b;
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
