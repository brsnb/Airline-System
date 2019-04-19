/**
 * FullLogging class
 *  	Manages all logging for the program
 */

package org.airlinesystem.controllers.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullLogging {

	private static FullLogging instance;
	
	private Logger resultsLogger = LoggerFactory.getLogger("resultsLogger");
	private Logger consoleLogger = LoggerFactory.getLogger("consoleLogger");
	private Logger debugLogger = LoggerFactory.getLogger("debugLogger");
	private Logger menuLogger = LoggerFactory.getLogger("menuLogger");
	
	/**
	 * Default constructor
	 */
	private FullLogging() {
	}
	
	/**
	 * Initialize loggers
	 */
	private void init() {
		resultsLogger = LoggerFactory.getLogger("resultsLogger");
		consoleLogger = LoggerFactory.getLogger("consoleLogger");
		debugLogger = LoggerFactory.getLogger("debugLogger");
		menuLogger = LoggerFactory.getLogger("menuLogger");
	}
	
	/**
	 * Get current FullLogging object instance
	 * if it doesn't exist yet initialize it
	 * 
	 * @return
	 * 		FullLogging object
	 */
	public static FullLogging getInstance() {
		if (instance == null) {
			instance = new FullLogging();
			instance.init();
		}
		return instance;
	}
	
	/**
	 * Log menu info message
	 *
	 * @param message_
	 * 		String of message to be logged
	 * @return
	 * 		N/A
	 */
	public void menuInfo(String message_) {
		menuLogger.info(message_);
	}
	
	/**
	 * Log menu error message
	 *
	 * @param message_
	 * 		String of message to be logged
	 * @return
	 * 		N/A
	 */
	public void menuError(String message_) {
		menuLogger.info(message_);
	}
	
	/**
	 * Log console info message
	 *
	 * @param message_
	 * 		String of message to be logged
	 * @return
	 * 		N/A
	 */
	public void consoleInfo(String message_) {
		consoleLogger.info(message_);
	}
	
	/**
	 * Log debugger debug message
	 *
	 * @param message_
	 * 		String of message to be logged
	 * @return
	 * 		N/A
	 */
	public void debugDebug(String message_) {
		debugLogger.debug(message_);
	}
	
	/**
	 * Log debugger error message
	 *
	 * @param message_
	 * 		String of message to be logged
	 * @return
	 * 		N/A
	 */
	public void debugError(String message_) {
		debugLogger.error(message_);
	}
	
	/**
	 * Log results info message
	 *
	 * @param message_
	 * 		String of message to be logged
	 * @return
	 * 		N/A
	 */
	public void resultsInfo(String message_) {
		resultsLogger.info(message_);
	}
}
