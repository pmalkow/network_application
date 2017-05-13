package pl.networkapp.repository;

import pl.networkapp.domain.User;

import java.util.Optional;

public interface UserRepository {

	void create(String userName);

	void postMessage(String userName, String message);

	Optional<User> get(String userName);
}
