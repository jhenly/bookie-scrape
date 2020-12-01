package com.bookiescrape.app.scrape;

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
        // private MatchBuilder members
        private String time;
        private int homeRot;
        private String home;
        private int awayRot;
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
        private MatchBuilder(int numBookies) {
            odds = new Odds[numBookies];
            homeRot = -1;
            awayRot = -1;
        }
        
        /**
         * Sets the home team's ROT number.
         * 
         * @param rot
         *            - the home team's rot number
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder homeRot(int rot) {
            this.homeRot = rot;
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
         * Sets the away team's ROT number.
         * 
         * @param rot
         *            - the away team's rot number
         * @return {@code this}, to allow for method chaining
         */
        public MatchBuilder awayRot(int rot) {
            this.homeRot = rot;
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
    
    // private Match members
    private String time;
    private Team home;
    private Team away;
    private String url;
    private Odds opener;
    private Odds[] odds;
    
    
    /* constructs a match from a passed in builder */
    private Match(MatchBuilder builder) {
        time = builder.time;
        home = new Team(builder.homeRot, builder.home);
        away = new Team(builder.awayRot, builder.away);
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
    public String time() { return time; }
    
    /**
     * Gets the match's ROT number.
     * 
     * @return this match's ROT number
     */
    public int homeRot() { return home.rot(); }
    
    /**
     * Gets the team name of this match's home team.
     * 
     * @return the home team's name
     */
    public String home() { return home.name(); }
    
    /**
     * Gets the match's ROT number.
     * 
     * @return this match's ROT number
     */
    public int awayRot() { return away.rot(); }
    
    /**
     * Gets the {@code Team} instance associated with the away team.
     * 
     * @return the away team's name
     * @see Match.Team
     */
    public String away() { return away.name(); }
    
    /**
     * Gets this match's url link.
     * 
     * @return this match's url link
     */
    public String url() { return url; }
    
    /**
     * Gets this match opener's over-under odds.
     * 
     * @return an {@linkplain Odds} instance containing the opener's over-under
     *         odds
     */
    public Odds opener() { return opener; }
    
    /**
     * Swaps one bookie's odds with another, this method is useful when
     * switching the ordering of bookies.
     * 
     * @param one
     * @param two
     */
    void swapBookieOdds(int one, int two) {
        Odds tmp = odds[one];
        odds[one] = odds[two];
        odds[two] = tmp;
    }
    
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
    
    /**
     * Gets the odds associated with a passed in bookie index.
     * 
     * @param bookieIndex
     *                    - which bookie's odds to get
     * @return the odds associated with the specified bookie index
     */
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
            "home: [%d] %s  away: [%d] %s  o-over: %s  o-under: %s  time: %s  url:  %s%n",
            home.rot(), home.name(), away.rot(), away.name(), opener.over(),
            opener.under(), time, url);
        
        
        for (int i = 0; i < odds.length; i++) {
            if (odds[i] == null) { break; }
            
            s += String.format("  book[%d]: %s %s ", i, odds[i].over(),
                odds[i].under());
        }
        
        return s;
    }
    
    /**
     * Class that represents a team.
     * <p>
     * This class contains two methods {@linkplain Team#rot() rot()} and
     * {@linkplain Team#name() name()}, which return the team's ROT number and
     * the team's name respectively.
     * 
     * @author Jonathan Henly
     */
    private static class Team {
        private String name;
        private int rot;
        
        /**
         * Constructs a new {@code Team} instance.
         * 
         * @param rot
         *             - the team's ROT number
         * @param name
         *             - the name of the team
         */
        private Team(int rot, String name) {
            this.rot = rot;
            this.name = name;
        }
        
        /**
         * Gets this {@code Team} instance's ROT number.
         * 
         * @return this team's ROT number
         */
        private int rot() { return rot; }
        
        /**
         * Gets this {@code Team} instance's name.
         * 
         * @return this team's name
         */
        private String name() { return name; }
    }
    
    /**
     * Class that represents the over under odds.
     * <p>
     * This class contains two methods {@linkplain Odds#over() over()} and
     * {@linkplain Odds#under() under()}, which return the over and under odds
     * respectively.
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
         * Gets this {@code Odds} instance's over.
         * 
         * @return this {@code Odds} instance's over
         */
        public String over() { return over; }
        
        /**
         * Gets this {@code Odds} instance's under.
         * 
         * @return this {@code Odds} instance's under
         */
        public String under() { return under; }
    }
    
    
}
