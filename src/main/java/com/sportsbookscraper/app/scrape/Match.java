package com.sportsbookscraper.app.scrape;

/**
 * Class that represents
 * 
 * @author Jonathan Henly
 */
public class Match {
    
    /**
     * 
     * @author Jonathan Henly
     */
    public static class MatchBuilder {
        private String date;
        private String home;
        private String away;
        private String url;
        private Odds opener;
        private Odds[] odds;
        
        MatchBuilder(int numBookies) { odds = new Odds[numBookies]; }
        
        public MatchBuilder date(String date) {
            this.date = date;
            return this;
        }
        
        public MatchBuilder home(String home) {
            this.home = home;
            return this;
        }
        
        public MatchBuilder away(String away) {
            this.away = away;
            return this;
        }
        
        public MatchBuilder url(String url) {
            this.url = url;
            return this;
        }
        
        public MatchBuilder opener(int over, int under) {
            this.opener = new Odds(over, under);
            return this;
        }
        
        public Match build() { return new Match(this); }
    }
    
    /**
     * Class that represents the over under odds.
     * 
     * @author Jonathan Henly
     */
    public static class Odds {
        private double over, under;
        
        public Odds(double over, double under) {
            this.over = over;
            this.under = under;
        }
        
        public double getOver() { return over; }
        
        public double getUnder() { return under; }
    }
    
    
    // private members
    private String date;
    private String home;
    private String away;
    private String url;
    private Odds opener;
    private Odds[] odds;
    
    
    /* */
    Match(MatchBuilder builder) {
        date = builder.date;
        home = builder.home;
        away = builder.away;
        url = builder.url;
        opener = builder.opener;
        odds = builder.odds;
    }
    
    /**
     * 
     * @param numBookies
     * @return
     */
    public MatchBuilder createMatch(int numBookies) {
        return new MatchBuilder(numBookies);
    }
    
    /**
     * 
     * @param bookieIndex
     *                    the index of this bookie
     * @param top
     * @param bottom
     */
    public void setBookieOdds(int bookieIndex, double over, double under) {
        odds[bookieIndex] = new Odds(over, under);
    }
    
    public Odds getBookieOdds(int bookieIndex) { return odds[bookieIndex]; }
    
    public Odds getOpener() { return opener; }
    
    public String getHome() { return home; }
    
    public String getAway() { return away; }
    
    public String getDate() { return date; }
    
    public String getUrl() { return url; }
    
}
