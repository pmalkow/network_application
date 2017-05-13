package pl.networkapp.interceptors;

import javax.servlet.http.HttpServletRequest;

import static pl.networkapp.AppConfig.USER_ID_HEADER;

class RequestValidator {

	static boolean isInvalidRequest(HttpServletRequest request) {
		String userId = request.getHeader(USER_ID_HEADER);
		return userId == null || userId.isEmpty();
	}
}
