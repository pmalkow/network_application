package pl.networkapp.domain;

public class Message {

	private String message;
	private long creationDate;

	public Message(String message) {
		this.message = message;
		this.creationDate = System.nanoTime();
	}

	public String getMessage() {
		return message;
	}

	public long getCreationDate() {
		return creationDate;
	}
}
