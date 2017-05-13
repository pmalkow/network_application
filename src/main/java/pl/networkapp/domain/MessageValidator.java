package pl.networkapp.domain;

public class MessageValidator {

	public static final int MAX_LENGTH = 140;

	public static boolean isInvalid(String message) {
		return message.isEmpty() || message.length() > MAX_LENGTH;
	}
}
