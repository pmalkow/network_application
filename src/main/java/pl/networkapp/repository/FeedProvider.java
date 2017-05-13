package pl.networkapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.networkapp.domain.Message;
import pl.networkapp.domain.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
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

	public List<String> getFollowingFeed(String userId) {
		User user = userRepository.get(userId).get();
		TreeSet<Message> set = new TreeSet<>(Comparator.comparing(Message::getCreationDate).reversed());
		for (User following : user.getFollowingUsers()) {
			set.addAll(following.getMessages());
		}
		return set.stream().map(Message::getMessage).collect(Collectors.toList());
	}

}
