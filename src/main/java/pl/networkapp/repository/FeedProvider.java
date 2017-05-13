package pl.networkapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.networkapp.domain.Message;
import pl.networkapp.domain.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedProvider {

	@Autowired private UserRepository userRepository;

	public List<String> getUsersFeed(String userId) {
		User user = userRepository.get(userId).get();
		List<String> messages = user.getMessages().stream().map(Message::getMessage).collect(Collectors.toList());
		Collections.reverse(messages);
		return messages;
	}
}
