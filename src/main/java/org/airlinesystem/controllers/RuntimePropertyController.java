/**
 * RuntimePropertyController class
 *  	Manages the loading of the properties file that are specific
 *  	to this program.
 */

package org.airlinesystem.controllers;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.exceptions.AirlineSystemException;

public class RuntimePropertyController {

	private FullLogging propertyControllerLog = FullLogging.getInstance();

	/**
	 * Loads the default.properties file and throws exceptions
	 * if it is unable to.
	 * 
	 * @return the Properties object with the defaults loaded
	 */
	public Properties loadDefaultProperties() throws AirlineSystemException {
		Properties _defaultProperties = new Properties();

		try (InputStream _is = this.getClass().getResourceAsStream("/default.properties")) {
			_defaultProperties.load(_is);
			propertyControllerLog.debugDebug("defaults loaded...");
		} catch (Exception e_) {
			throw new AirlineSystemException("Error reading default.properties file", e_);
		}	
		return _defaultProperties;
	}

	/**
	 *  Attempts to load in the runtime properties for a specified properties file
	 *  
	 *  @param the string filename/path of the custom properties file
	 *  @return the loaded properties file, default or custom
	 */
	public Properties createRuntimeProperties(File file_) throws AirlineSystemException {
		Properties _properties = new Properties();

		try (InputStream _is = new FileInputStream(file_)) {
			_properties.load(_is);
		} catch(IOException e_){
			throw new AirlineSystemException("Unable to load specified properties file", e_);
		}
		
		return _properties;
	}

	/**
	 *  Decides whether to load the defaults or use the custom properties 
	 *  file, then calls the appropriate method for loading.
	 *  
	 *  @param the string filename/path of the properties file
	 *  @return the loaded Properties object
	 */
	public Properties loadRuntimeProperties(File file_) {
		Properties _returnProperties = new Properties();

		if(file_.getName().matches("default.properties")) {
			try {
				_returnProperties = loadDefaultProperties();
			} catch(AirlineSystemException _e) {
				propertyControllerLog.debugDebug(_e.getLocalizedMessage() + "Error loading default.properties, exiting...");
				System.exit(1);
			}
		} else {
			try {
				_returnProperties = createRuntimeProperties(file_);
			} catch(AirlineSystemException _e){
				propertyControllerLog.menuInfo(_e.getLocalizedMessage() + "\nReverting to default.properties\n");
				try {
					_returnProperties = loadDefaultProperties();
				} catch(AirlineSystemException _e2) {
					propertyControllerLog.debugError(_e2.getLocalizedMessage() + "Error loading default.properties, exiting...");
					System.exit(1);
				};
			}
		}
		return _returnProperties;
	}
}

