package com.sportsbookscraper.app.excel;

import java.util.Iterator;


/**
 * @author Jonathan Henly
 */
public abstract class AbstractWorkbook {

    /*   */
    interface SheetIterator<E> extends Iterator<E> {


        @Override
        boolean hasNext();

        @Override
        E next();

    }
}
