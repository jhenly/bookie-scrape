package com.sportsbookscraper.app.scrape;

import java.util.ArrayList;
import java.util.List;

public class DateGroup {
	private String date;
	private List<Match> matches;
	
	public DateGroup(String date) {
		this.date = date;
		matches = new ArrayList<Match>();
	}
	
	public String getDate() {
		return date;
	}
	
	public List<Match> getMatches() {
		return matches;
	}
	
	public void addMatch(Match match) {
		matches.add(match);
	}
	
	public int size() {
		return matches.size();
	}
}
