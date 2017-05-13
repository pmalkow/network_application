package pl.networkapp.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.networkapp.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static pl.networkapp.AppConfig.USER_ID_HEADER;
import static pl.networkapp.interceptors.HeaderValidator.isInvalidRequest;

@Component
public class PostRequestInterceptor extends HandlerInterceptorAdapter {

	@Autowired private UserRepository userRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (isInvalidRequest(request)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
		createNewUserIfAbsent(request.getHeader(USER_ID_HEADER));
		return true;
	}

	private void createNewUserIfAbsent(String userId) {
		if (!userRepository.get(userId).isPresent()) {
			userRepository.create(userId);
		}
	}

}
