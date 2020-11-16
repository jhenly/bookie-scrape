package com.sportsbookscraper.app.scrape;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan Henly
 *
 */
public class Bookie {
	private String name;
	private int index;
	
	/**
	 * Creates a new {@code Bookie} instance.
	 * 
	 * @param name - the name of this bookie
	 * @param index - the Excel sheet column index of this bookie
	 */
	public Bookie(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * @return the name of this bookie as a {@code String}
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return the Excel sheet column index of this bookie
	 */
	public int index() {
		return index;
	}
	
}
