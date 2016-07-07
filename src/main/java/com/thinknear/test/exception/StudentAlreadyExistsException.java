package com.thinknear.test.exception;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class StudentAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -6184926129523711641L;

	public StudentAlreadyExistsException() {
		super();
	}

	public StudentAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public StudentAlreadyExistsException(String message) {
		super(message);
	}

	public StudentAlreadyExistsException(Throwable cause) {
		super(cause);
	}
	
}
