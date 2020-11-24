package com.sportsbookscraper.app.scrape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Class to contain the matches scraped from a date group.
 * <p>
 * This class has a {@linkplain #listIterator()} and an {@linkplain #iterator()}
 * method to allow for iterating over the list of scraped matches.
 * 
 * @author Jonathan Henly
 */
public class DateGroup implements Iterable<Match> {
    private String date;
    private List<Match> matches;
    
    /**
     * Constructs a new {@code DateGroup} instance associated with the passed in
     * date.
     * 
     * @param date
     *             - the date of this date group
     */
    public DateGroup(String date) {
        this.date = date;
        matches = new ArrayList<Match>();
    }
    
    /**
     * @return the date associated with this {@code DateGroup} instance
     */
    public String getDate() { return date; }
    
    /**
     * @return a list of the matches associated with this {@code DateGroup}
     *         instance
     */
    public List<Match> getMatches() { return matches; }
    
    /**
     * @return a list of the matches associated with this {@code DateGroup}
     *         instance
     */
    public Match getMatch(int index) { return matches.get(index); }
    
    /**
     * Adds a {@code Match} instance to the end of the matches list in this
     * {@code DateGroup} instance.
     * 
     * @param match
     *              the match to add
     */
    public void addMatch(Match match) { matches.add(match); }
    
    /**
     * Swaps all match odds between two different bookies.
     * 
     * @param one
     *            - the bookie to swap indexes with the other
     * @param two
     *            - the other bookie to swap indexes with
     */
    public void swapBookieIndexes(Bookie one, Bookie two) {
        for (Match m : this) {
            m.swapBookieOdds(one.index(), two.index());
        }
    }
    
    /**
     * @return the size of the list of matches in this {@code DateGroup}
     *         instance
     */
    public int size() { return matches.size(); }
    
    /**
     * Returns a {@code ListIterator<Match>} over the matches in this
     * {@code DateGroup} instance.
     * 
     * @return a list iterator over the matches in this date group
     * @see ListIterator
     */
    public ListIterator<Match> listIterator() {
        return matches.listIterator();
    }
    
    /**
     * Returns an {@code Iterator<Match>} over the matches in this
     * {@code DateGroup} instance.
     * 
     * @return an iterator over the matches in this date group
     * @see Iterator
     */
    @Override
    public Iterator<Match> iterator() { return matches.iterator(); }
    
    /**
     * A geniric {@code toString()} method.
     * 
     * @return a generic {@code toString()} of this object
     */
    @Override
    public String toString() {
        String s = "";
        for (Match m : this) {
            s += m.toString() + "\n";
        }
        return s;
    }
}
