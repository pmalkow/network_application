package pl.networkapp.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import pl.networkapp.domain.User;
import pl.networkapp.repository.FeedProvider;
import pl.networkapp.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static pl.networkapp.domain.MessageValidator.MAX_LENGTH;

@RunWith(MockitoJUnitRunner.class)
public class SocialNetworkControllerTest {

	private static final String userId = "userId";

	@Mock private UserRepository userRepository;
	@Mock private FeedProvider feedProvider;
	@InjectMocks private SocialNetworkController controller;

	@Test
	public void shouldIndicateBadRequestWhenMessageIsTooLong() {
		ResponseEntity<Void> response = controller.postMessage(userId, tooLongMessage());

		assertThat(response.getStatusCodeValue()).isEqualTo(400);
	}

	@Test
	public void shouldIndicateBadRequestWhenMessageIsTooShort() {
		ResponseEntity<Void> response = controller.postMessage(userId, "");

		assertThat(response.getStatusCodeValue()).isEqualTo(400);
	}

	@Test
	public void shouldPostMessageAndReturnOk() {
		String someMessage = "someMessage";
		ResponseEntity<Void> response = controller.postMessage(userId, someMessage);

		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		then(userRepository).should().postMessage(userId, someMessage);
	}

	@Test
	public void shouldGetFeedsForAnUser() {
		controller.getMyMessages(userId);

		then(feedProvider).should().getUsersFeed(userId);
	}

	@Test
	public void shouldFollowUser() {
		String otherId = "otherId";
		given(userRepository.get(otherId)).willReturn(Optional.of(new User(otherId)));

		controller.follow(userId, otherId);

		then(userRepository).should().follow(userId, otherId);
	}

	@Test
	public void shouldReturnNotFoundWhenUserToFollowDoesNotExist() {
		String otherId = "otherId";
		given(userRepository.get(otherId)).willReturn(Optional.empty());

		ResponseEntity<Void> result = controller.follow(userId, otherId);

		assertThat(result.getStatusCodeValue()).isEqualTo(404);
	}

	@Test
	public void shouldGetFeedsForFollowingUser() {
		controller.getFollowingFeed(userId);

		then(feedProvider).should().getFollowingFeed(userId);
	}

	private String tooLongMessage() {
		StringBuilder sb = new StringBuilder(MAX_LENGTH + 1);
		for (int i = 0; i < MAX_LENGTH + 1; i++) {
			sb.append('a');
		}
		return sb.toString();
	}

}