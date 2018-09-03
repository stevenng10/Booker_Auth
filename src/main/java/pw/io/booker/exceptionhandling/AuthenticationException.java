package pw.io.booker.exceptionhandling;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String userFriendlyErrorMessage;

	public AuthenticationException(RuntimeException e, String message) {
		super(e);
		this.userFriendlyErrorMessage = message;
	}

	public AuthenticationException(Throwable e, String message) {
		super(e);
		this.userFriendlyErrorMessage = message;
	}

	public AuthenticationException(String message) {
		this.userFriendlyErrorMessage = message;
	}

	public String getUserFriendlyErrorMessage() {
		return userFriendlyErrorMessage;
	}
}
