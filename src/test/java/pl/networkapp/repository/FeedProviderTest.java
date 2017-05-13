package pl.networkapp.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.networkapp.domain.Message;
import pl.networkapp.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class FeedProviderTest {

	private static final String USER_ID = "someUser";

	@Mock private UserRepository userRepository;
	@InjectMocks private FeedProvider feedProvider;

	@Test
	public void shouldReturnListOfMessagesInReversedOrder() {
		User user = someUserWithMessages();
		given(userRepository.get(USER_ID)).willReturn(Optional.of(user));

		List<String> usersFeed = feedProvider.getUsersFeed(USER_ID);

		assertThat(usersFeed.size()).isEqualTo(3);
		assertThat(usersFeed.get(0)).isEqualTo("message3");
		assertThat(usersFeed.get(1)).isEqualTo("message2");
		assertThat(usersFeed.get(2)).isEqualTo("message1");
	}

	@Test
	public void shouldReturnListOfMessagesFromFollowingUsers() {
		User user1 = someUserThatFollowsUsers();
		given(userRepository.get(USER_ID)).willReturn(Optional.of(user1));

		List<String> followingFeed = feedProvider.getFollowingFeed(USER_ID);

		assertThat(followingFeed.size()).isEqualTo(4);
		assertThat(followingFeed.get(0)).isEqualTo("message4");
		assertThat(followingFeed.get(1)).isEqualTo("message3");
		assertThat(followingFeed.get(2)).isEqualTo("message2");
		assertThat(followingFeed.get(3)).isEqualTo("message1");
	}

	private User someUserThatFollowsUsers() {
		User user1 = new User(USER_ID);
		User user2 = new User("userId2");
		User user3 = new User("userId3");
		Message message1 = new Message("message1");
		user2.getMessages().add(message1);
		Message message2 = new Message("message2");
		user2.getMessages().add(message2);
		user1.getFollowingUsers().add(user2);
		Message message3 = new Message("message3");
		user3.getMessages().add(message3);
		Message message4 = new Message("message4");
		user3.getMessages().add(message4);
		user1.getFollowingUsers().add(user3);
		return user1;
	}

	private User someUserWithMessages() {
		User user = new User(USER_ID);
		Message message1 = new Message("message1");
		Message message2 = new Message("message2");
		Message message3 = new Message("message3");
		user.getMessages().add(message1);
		user.getMessages().add(message2);
		user.getMessages().add(message3);
		return user;
	}

}
