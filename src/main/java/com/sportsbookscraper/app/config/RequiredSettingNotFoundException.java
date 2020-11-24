package com.sportsbookscraper.app.config;

import java.io.File;
import java.nio.file.Path;

/**
 * Signals that a required property was not found after loading properties from
 * a {@code .properties} file.
 * 
 * @author Jonathan Henly
 */
public class RequiredSettingNotFoundException extends Exception {
	private static final long serialVersionUID = -1207032861834691318L;

	/**
	 * Constructs a {@code RequiredPropertyNotFoundException}, combining the
	 * specified property and the specified properties file path into a detail
	 * message.
	 *
	 * @param property the property that was not found
	 * @param propsFilePath the {@code .properties} file path
	 */
	public
	RequiredSettingNotFoundException(String property, String propsFilePath)
	{
		super(combineIntoMessage(property, propsFilePath));
	}
	
	private static String combineIntoMessage(String prop, String filepath) {
		final String frmtstr = "the required property '%s' was not found in the"
				+ " properties file '%s'"; 
		return String.format(frmtstr, prop.toString(), filepath.toString());
	}

	
	/**
	 * Convenience constructor that takes in the property string that was not
	 * found, as well as the {@code .properties} file in which it was not found.
	 * <p>
	 * This constructor simply calls {@code this(property,
	 * propertiesFile.getAbsolutePath())}
	 *
	 * @param property the property that was not found
	 * @param propertiesFile the {@code .properties} file
	 * 
	 * @see File#getAbsolutePath()
	 * @see {@linkplain #RequiredPropertyNotFound(String, String)
	 *                   this(String, String)}
	 */
	public
	RequiredSettingNotFoundException(String property, File propertiesFile)
	{
		this(property, propertiesFile.getAbsolutePath());
	}

	
	/**
	 * Convenience constructor that takes in the property string that was not
	 * found, as well as a path representing the {@code .properties} file in 
	 * which it was not found.
	 * <p>
	 * This constructor simply calls {@code this(property,
	 * propertiesPath.toFile())}
	 *
	 * @param property the property that was not found
	 * @param propertiesPath the {@code .properties} file
	 * 
	 * @see Path#toFile()
	 * @see {@linkplain #RequiredPropertyNotFoundException(String, String)
	 *                   this(String, String)}
	 */
	public
	RequiredSettingNotFoundException(String property, Path propertiesPath)
	{
		this(property, propertiesPath.toFile());
	}
	

}
