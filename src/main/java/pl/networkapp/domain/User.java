package pl.networkapp.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {

	private String userName;
	private List<Message> messages;
	private Set<User> followingUsers;

	public User(String userName) {
		this.userName = userName;
		this.messages = new ArrayList<>();
		this.followingUsers = new HashSet<>();
	}

	public String getUserName() {
		return userName;
	}

	public Set<User> getFollowingUsers() {
		return followingUsers;
	}

	public List<Message> getMessages() {
		return messages;
	}
}
