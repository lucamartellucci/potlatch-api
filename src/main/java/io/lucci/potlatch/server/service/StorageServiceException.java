package io.lucci.potlatch.server.service;

public class StorageServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public StorageServiceException() {
	}

	public StorageServiceException(String message) {
		super(message);
	}

	public StorageServiceException(Throwable cause) {
		super(cause);
	}

	public StorageServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
