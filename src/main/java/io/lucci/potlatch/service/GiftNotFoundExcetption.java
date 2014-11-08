package io.lucci.potlatch.service;

public class GiftNotFoundExcetption extends Exception {

	private static final long serialVersionUID = 1L;

	public GiftNotFoundExcetption() {
	}

	public GiftNotFoundExcetption(String message) {
		super(message);
	}

	public GiftNotFoundExcetption(Throwable cause) {
		super(cause);
	}

	public GiftNotFoundExcetption(String message, Throwable cause) {
		super(message, cause);
	}

	public GiftNotFoundExcetption(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
