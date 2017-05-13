package pl.networkapp.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.networkapp.domain.User;
import pl.networkapp.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static pl.networkapp.AppConfig.USER_ID_HEADER;
import static pl.networkapp.interceptors.RequestValidator.isInvalidRequest;

@Component
public class WallRequestInterceptor extends HandlerInterceptorAdapter {

	@Autowired private UserRepository userRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (isInvalidRequest(request)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
		Optional<User> user = userRepository.get(request.getHeader(USER_ID_HEADER));
		if (!user.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}
		return true;
	}

}
