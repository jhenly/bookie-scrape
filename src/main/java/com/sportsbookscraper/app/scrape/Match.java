package com.sportsbookscraper.app.scrape;

/**
 * Class that represents 
 * 
 * @author Jonathan Henly
 */
public class Match {
	// package private members
	String time;
	String home;
	String away;
	Odds opener;
	Odds[] odds;
	
	/**
	 * Class that represents the over under odds.
	 * 
	 * @author Jonathan Henly
	 */
	public static class Odds {
		private int over, under;
		
		public Odds(int top, int bottom) {
			this.over = top;
			this.under = bottom;
		}
		
		public int getOver() { return over; }
		public int getUnder() { return under; }
	}
	
	/* */
	Match(String time, String home, String away, int numBookies) {
		this.home = home;
		this.away = away;
		Odds[] odds = new Odds[numBookies];
	}
	
	/**
	 * 
	 * @param bookieIndex the index of this bookie
	 * @param top 
	 * @param bottom
	 */
	public void setBookieOdds(int bookieIndex, int top, int bottom) {
		odds[bookieIndex] = new Odds(top, bottom);
	}
	
	public Odds getBookieOdds(int bookieIndex) {
		return odds[bookieIndex];
	}

	public void setOpener(int top, int bottom) {
		opener = new Odds(top, bottom);
	}
	
	public Odds getOpener() {
		return opener;
	}
	
	public String getHome() {
		return home;
	}
	
	public String getAway() {
		return away;
	}
}
