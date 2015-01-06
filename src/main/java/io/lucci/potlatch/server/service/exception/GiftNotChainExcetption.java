package io.lucci.potlatch.server.service.exception;

public class GiftNotChainExcetption extends Exception {

	private static final long serialVersionUID = 1L;

	public GiftNotChainExcetption() {
	}

	public GiftNotChainExcetption(String message) {
		super(message);
	}

	public GiftNotChainExcetption(Throwable cause) {
		super(cause);
	}

	public GiftNotChainExcetption(String message, Throwable cause) {
		super(message, cause);
	}

	public GiftNotChainExcetption(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
