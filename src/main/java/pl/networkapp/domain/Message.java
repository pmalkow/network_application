package pl.networkapp.domain;

import java.util.Date;

public class Message {

	private String message;
	private Date creationDate;

	public Message(String message) {
		this.message = message;
		this.creationDate = new Date();
	}

	public String getMessage() {
		return message;
	}

	public Date getCreationDate() {
		return creationDate;
	}
}
