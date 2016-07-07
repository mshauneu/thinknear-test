package com.thinknear.test.exception;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5169907266727852289L;

	public StudentNotFoundException() {
		super();
	}

	public StudentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StudentNotFoundException(String message) {
		super(message);
	}

	public StudentNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
