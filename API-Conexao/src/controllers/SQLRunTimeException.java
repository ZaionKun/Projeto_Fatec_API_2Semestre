package controllers;

public class SQLRunTimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SQLRunTimeException() {

	}

	public SQLRunTimeException(String message) {
		super(message);

	}

	public SQLRunTimeException(Throwable cause) {
		super(cause);

	}

	public SQLRunTimeException(String message, Throwable cause) {
		super(message, cause);

	}

	public SQLRunTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}
}