package pl.networkapp.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.networkapp.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class InMemoryRepositoryTest {

	private static final String USER_NAME = "userName";
	private static final String USER_NAME2 = "userName2";

	private InMemoryRepository repository = new InMemoryRepository();

	@Test
	public void shouldCreateUser() {
		repository.create(USER_NAME);

		assertThat(repository.get(USER_NAME)).isPresent();
	}

	@Test
	public void shouldReturnEmptyForNonExistingUser() {
		assertThat(repository.get("someOtherUser")).isNotPresent();
	}

	@Test
	public void shouldPostMessage() {
		repository.create(USER_NAME);

		repository.postMessage(USER_NAME, "someMessage1");
		repository.postMessage(USER_NAME, "someMessage2");
		repository.postMessage(USER_NAME, "someMessage3");

		Optional<User> user = repository.get(USER_NAME);
		assertThat(user).isPresent();
		assertThat(user.get().getMessages()).size().isEqualTo(3);
	}

	@Test
	public void shouldRemoveUser() {
		repository.create(USER_NAME);

		repository.delete(USER_NAME);

		Optional<User> user = repository.get(USER_NAME);
		assertThat(user).isNotPresent();
	}

	@Test
	public void shouldFollowUser() {
		repository.create(USER_NAME);
		repository.create(USER_NAME2);

		repository.follow(USER_NAME, USER_NAME2);

		Optional<User> user = repository.get(USER_NAME);
		Optional<User> user2 = repository.get(USER_NAME2);

		assertThat(user.get().getFollowingUsers()).size().isEqualTo(1);
		assertThat(user.get().getFollowingUsers()).contains(user2.get());
	}

}
