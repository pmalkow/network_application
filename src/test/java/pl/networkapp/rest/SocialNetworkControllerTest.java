package pl.networkapp.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import pl.networkapp.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static pl.networkapp.domain.MessageValidator.MAX_LENGTH;

@RunWith(MockitoJUnitRunner.class)
public class SocialNetworkControllerTest {

	private static final String userId = "userId";

	@Mock private UserRepository userRepository;
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

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		then(userRepository).should().postMessage(userId, someMessage);
	}

	private String tooLongMessage() {
		StringBuilder sb = new StringBuilder(MAX_LENGTH + 1);
		for (int i = 0; i < MAX_LENGTH + 1; i++) {
			sb.append('a');
		}
		return sb.toString();
	}

}