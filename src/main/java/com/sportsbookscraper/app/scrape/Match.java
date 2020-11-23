package com.sportsbookscraper.app.scrape;

/**
 * Class that holds all of data scraped from a match between two teams.
 * 
 * @author Jonathan Henly
 */
public class Match {
    
    /**
     * Builder class used to construct a {@code Match} instance while scraping.
     * 
     * @author Jonathan Henly
     */
    public static class MatchBuilder {
        private int rot;
        private String time;
        private String home;
        private String away;
        private String url;
        private Odds opener;
        private Odds[] odds;
        
        /**
         * Constructs a {@code MatchBuilder} instance which is used to build a
         * {@code Match} instance.
         * 
         * @param numBookies
         *                   - the number of bookies to be scraped
         */
        private MatchBuilder(int numBookies) { odds = new Odds[numBookies]; }
        
        /**
         * Sets the match's ROT number.
         * 
         * @param rot
         *            - the match's rot number
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder rot(int rot) {
            this.rot = rot;
            return this;
        }
        
        /**
         * Sets the match's starting time.
         * 
         * @param time
         *             - the match's starting time
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder time(String time) {
            this.time = time;
            return this;
        }
        
        /**
         * Sets the team name of the home team.
         * 
         * @param away
         *             - the home team's name
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder home(String home) {
            this.home = home;
            return this;
        }
        
        /**
         * Sets the team name of the away team.
         * 
         * @param away
         *             - the away team's name
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder away(String away) {
            this.away = away;
            return this;
        }
        
        /**
         * Sets the match's url link.
         * 
         * @param url
         *            - the match's url link
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder url(String url) {
            this.url = url;
            return this;
        }
        
        /**
         * Sets the match opener's over-under odds.
         * 
         * @param over
         *              - the opener's over odds
         * @param under
         *              - the opener's under odds
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder opener(String over, String under) {
            this.opener = new Odds(over, under);
            return this;
        }
        
        /**
         * Constructs a new match instance from the data given to this
         * {@code MatchBuilder} instance.
         * 
         * @return a new {@code Match} instance comprised of the data given to
         *         this {@code MatchBuilder} instance
         */
        public Match build() { return new Match(this); }
    }
    
    // private members
    private int rot;
    private String time;
    private String home;
    private String away;
    private String url;
    private Odds opener;
    private Odds[] odds;
    
    
    /* constructs a match from a passed in builder */
    private Match(MatchBuilder builder) {
        rot = builder.rot;
        time = builder.time;
        home = builder.home;
        away = builder.away;
        url = builder.url;
        opener = builder.opener;
        odds = builder.odds;
    }
    
    /**
     * Creates an initial match, with none of its members set.
     * <p>
     * The returned {@code MatchBuilder} instance is used to assign values to
     * the match. Once all of the desired values have been set, the
     * {@linkplain MatchBuilder#build()} method can be used to construct a
     * {@code Match} instance.
     * 
     * @param numBookies
     *                   - the number of bookies to be scraped
     * @return a match builder instance with methods to assign values and
     *         eventually build a match
     */
    public static MatchBuilder createMatch(int numBookies) {
        return new MatchBuilder(numBookies);
    }
    
    
    /**
     * Gets this match's starting time.
     * 
     * @return this match's starting time
     */
    public String getTime() { return time; }
    
    /**
     * Gets the match's ROT number.
     * 
     * @return this match's ROT number
     */
    public int getRot() { return rot; }
    
    /**
     * Gets the team name of this match's home team.
     * 
     * @return the home team's name
     */
    public String getHome() { return home; }
    
    /**
     * Gets the team name of this match's away team.
     * 
     * @return the away team's name
     */
    public String getAway() { return away; }
    
    /**
     * Gets this match's url link.
     * 
     * @return this match's url link
     */
    public String getUrl() { return url; }
    
    /**
     * Gets this match opener's over-under odds.
     * 
     * @return an {@linkplain Odds} instance containing the opener's over-under
     *         odds
     */
    public Odds getOpener() { return opener; }
    
    /**
     * Sets a specified bookie's odds for this match.
     * 
     * @param bookieIndex
     *                    the index of this bookie
     * @param over
     *                    - the bookie's over odds
     * @param under
     *                    - the bookie's under odds
     */
    public void setBookieOdds(int bookieIndex, String over, String under) {
        odds[bookieIndex] = new Odds(over, under);
    }
    
    public Odds getBookieOdds(int bookieIndex) { return odds[bookieIndex]; }
    
    
    /**
     * Mainly used for debugging.
     * 
     * @return a textual representation of the data that makes up this match
     *         instance
     */
    @Override
    public String toString() {
        String s = "";
        
        s = String.format(
            "rot: %d  home: %s  away: %s  o-over: %s  o-under: %s  time: %s  url:  %s%n",
            rot, home, away, opener.getOver(), opener.getUnder(), time, url);
        
        
        for (int i = 0; i < odds.length; i++) {
            if (odds[i] == null) { break; }
            
            s += String.format("  book[%d]: %s %s ", i, odds[i].getOver(),
                odds[i].getUnder());
        }
        
        return s;
    }
    
    
    /**
     * Class that represents the over under odds.
     * <p>
     * This class contains two methods {@linkplain Odds#getOver() getOver()} and
     * {@linkplain Odds#getOver() getUnder()}, which return the over and under
     * odds respectively.
     * 
     * @author Jonathan Henly
     */
    public static class Odds {
        private String over, under;
        
        /**
         * Constructs a new {@code Odds} instance.
         * 
         * @param over
         *              - the over odds
         * @param under
         *              - the under odds
         */
        private Odds(String over, String under) {
            this.over = over;
            this.under = under;
        }
        
        /**
         * @return the over odds
         */
        public String getOver() { return over; }
        
        /**
         * @return the under odds
         */
        public String getUnder() { return under; }
    }
    
    
}
