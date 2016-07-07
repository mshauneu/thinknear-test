package com.thinknear.test.exception;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class CourseAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -6184926129523711641L;

	public CourseAlreadyExistsException() {
		super();
	}

	public CourseAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CourseAlreadyExistsException(String message) {
		super(message);
	}

	public CourseAlreadyExistsException(Throwable cause) {
		super(cause);
	}
	
}
