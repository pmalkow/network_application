package pl.networkapp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkapp.repository.FeedProvider;
import pl.networkapp.repository.UserRepository;

import java.util.List;

import static pl.networkapp.AppConfig.USER_ID_HEADER;
import static pl.networkapp.domain.MessageValidator.isInvalid;

@RestController
class SocialNetworkController {

	@Autowired private UserRepository userRepository;
	@Autowired private FeedProvider feedProvider;

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	ResponseEntity<Void> postMessage(@RequestHeader(value = USER_ID_HEADER) String userId,
	                                        @RequestBody String message) {
		if (isInvalid(message)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		userRepository.postMessage(userId, message);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/wall", method = RequestMethod.GET)
	ResponseEntity<List<String>> getMyMessages(@RequestHeader(value = USER_ID_HEADER) String userId) {
		return new ResponseEntity<>(feedProvider.getUsersFeed(userId), HttpStatus.OK);
	}

}
