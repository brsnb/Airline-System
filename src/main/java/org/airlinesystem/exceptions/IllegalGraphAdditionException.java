/**
 * IllegalGraphAdditionException class
 * 		Class creating custom IllegalGraphAdditionException exception 
 * 		for simpler error handling
 */

package org.airlinesystem.exceptions;

public class IllegalGraphAdditionException extends Exception {

	private static final long serialVersionUID = 6234124611862832035L;

	/**
	 * Constructor used when only a message is passed
	 * 
	 * @param message_
	 * 		String of exception message encountered
	 */
	public IllegalGraphAdditionException(String message) {
    	super(message);
    }

	/**
	 * Constructor used when passed a message and throwable (exception)
	 * 
	 * @param message_
	 * 		String of exception message encountered
	 * @param throwable_
	 * 		Throwable object, exception that was passed
	 */
    public IllegalGraphAdditionException (String message, Throwable throwable) {
    	super(message, throwable);
    }

}
