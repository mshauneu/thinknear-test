package com.thinknear.test.exception;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5169907266727852289L;

	public CourseNotFoundException() {
		super();
	}

	public CourseNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CourseNotFoundException(String message) {
		super(message);
	}

	public CourseNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
