/**
 * AirlineSystemException class
 * 		Class creating custom AirlineSystemException exception 
 * 		for simpler error handling
 */

package org.airlinesystem.exceptions;

public class AirlineSystemException extends Exception {

	private static final long serialVersionUID = -7940366909339982987L;
	
	/**
	 * Constructor used when only a message is passed
	 * 
	 * @param message_
	 * 		String of exception message encountered
	 */
	public AirlineSystemException(String message_) {
    	super(message_);
    }

	/**
	 * Constructor used when passed a message and throwable (exception)
	 * 
	 * @param message_
	 * 		String of exception message encountered
	 * @param throwable_
	 * 		Throwable object, exception that was passed
	 */
    public AirlineSystemException(String message_, Throwable throwable_) {
    	super(message_, throwable_);
    }

    /**
     * Constructor used when passed a throwable (exception)
     * 
     * @param throwable_
     * 		Throwable object, exception that was passed
     */
    public AirlineSystemException(Throwable throwable_) {
    	super(throwable_);
    }
}
