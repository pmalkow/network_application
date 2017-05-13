package pl.networkapp.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String userName;
	private List<Message> messages;

	public User(String userName) {
		this.userName = userName;
		this.messages = new ArrayList<>();
	}

	public String getUserName() {
		return userName;
	}

	public List<Message> getMessages() {
		return messages;
	}
}
