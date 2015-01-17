package io.lucci.potlatch.server.service.exception;

public class GiftManagerException extends Exception {

	private static final long serialVersionUID = 1L;

	public GiftManagerException() {
	}

	public GiftManagerException(String message) {
		super(message);
	}

	public GiftManagerException(Throwable cause) {
		super(cause);
	}

	public GiftManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public GiftManagerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
