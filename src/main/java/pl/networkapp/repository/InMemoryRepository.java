package pl.networkapp.repository;

import org.springframework.stereotype.Repository;
import pl.networkapp.domain.Message;
import pl.networkapp.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryRepository implements UserRepository {

	private Map<String, User> users = new HashMap<>();

	@Override
	public void create(String userName) {
		users.put(userName, new User(userName));
	}

	@Override
	public void postMessage(String userName, String message) {
		users.get(userName).getMessages().add(new Message(message));
	}

	@Override
	public Optional<User> get(String userName) {
		if (users.containsKey(userName)) {
			return Optional.of(users.get(userName));
		}
		return Optional.empty();
	}

	@Override
	public void delete(String userName) {
		users.remove(userName);
	}

}
