package com.thinknear.test.exception;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class StudentWasNotEnrolledException extends RuntimeException {

	private static final long serialVersionUID = 5169907266727852289L;

	public StudentWasNotEnrolledException() {
		super();
	}

	public StudentWasNotEnrolledException(String message, Throwable cause) {
		super(message, cause);
	}

	public StudentWasNotEnrolledException(String message) {
		super(message);
	}

	public StudentWasNotEnrolledException(Throwable cause) {
		super(cause);
	}
	
}
