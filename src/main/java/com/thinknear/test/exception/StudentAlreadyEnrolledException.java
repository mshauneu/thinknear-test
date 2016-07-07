package com.thinknear.test.exception;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class StudentAlreadyEnrolledException extends RuntimeException {

	private static final long serialVersionUID = 5169907266727852289L;

	public StudentAlreadyEnrolledException() {
		super();
	}

	public StudentAlreadyEnrolledException(String message, Throwable cause) {
		super(message, cause);
	}

	public StudentAlreadyEnrolledException(String message) {
		super(message);
	}

	public StudentAlreadyEnrolledException(Throwable cause) {
		super(cause);
	}
	
}
