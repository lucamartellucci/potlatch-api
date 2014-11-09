package io.lucci.potlatch.service;

public class GiftServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public GiftServiceException() {
	}

	public GiftServiceException(String message) {
		super(message);
	}

	public GiftServiceException(Throwable cause) {
		super(cause);
	}

	public GiftServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public GiftServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}