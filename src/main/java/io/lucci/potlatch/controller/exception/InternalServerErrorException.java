package io.lucci.potlatch.controller.exception;


public class InternalServerErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorException() {
		super();
	}

	public InternalServerErrorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InternalServerErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalServerErrorException(String message) {
		super(message);
	}

	public InternalServerErrorException(Throwable cause) {
		super(cause);
	}

	
}
