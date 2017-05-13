package pl.networkapp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkapp.AppConfig;
import pl.networkapp.repository.UserRepository;

import static pl.networkapp.domain.MessageValidator.isInvalid;

@RestController
class SocialNetworkController {

	@Autowired private UserRepository userRepository;

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	ResponseEntity<Void> postMessage(@RequestHeader(value = AppConfig.USER_ID_HEADER) String userId,
	                                        @RequestBody String message) {
		if (isInvalid(message)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userRepository.postMessage(userId, message);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
