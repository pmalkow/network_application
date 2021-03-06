package pl.networkapp.interceptors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.networkapp.domain.User;
import pl.networkapp.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static pl.networkapp.AppConfig.USER_ID_HEADER;

@RunWith(MockitoJUnitRunner.class)
public class PostRequestInterceptorTest {

	private static final String USER_ID = "userId";

	@Mock private UserRepository userRepository;
	@InjectMocks private PostRequestInterceptor postRequestInterceptor;

	@Test
	public void shouldIndicateBadRequestWhenEmptyHeader() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		boolean result = postRequestInterceptor.preHandle(request, response, null);

		assertThat(result).isFalse();
		assertThat(response.getStatus()).isEqualTo(400);
	}

	@Test
	public void shouldIndicateBadRequestWhenInvalidHeader() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(USER_ID_HEADER, "");
		MockHttpServletResponse response = new MockHttpServletResponse();

		boolean result = postRequestInterceptor.preHandle(request, response, null);

		assertThat(result).isFalse();
		assertThat(response.getStatus()).isEqualTo(400);
	}

	@Test
	public void shouldCreateUserForNonExistingOneAndContinueProcessing() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(USER_ID_HEADER, USER_ID);
		MockHttpServletResponse response = new MockHttpServletResponse();
		given(userRepository.get(USER_ID)).willReturn(Optional.empty());

		boolean result = postRequestInterceptor.preHandle(request, response, null);

		assertThat(result).isTrue();
		then(userRepository).should().create(USER_ID);
	}

	@Test
	public void shouldContinueProcessingForValidHeaderAndExistingUser() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(USER_ID_HEADER, USER_ID);
		MockHttpServletResponse response = new MockHttpServletResponse();
		given(userRepository.get(USER_ID)).willReturn(Optional.of(new User(USER_ID)));

		boolean result = postRequestInterceptor.preHandle(request, response, null);

		assertThat(result).isTrue();
	}

}
