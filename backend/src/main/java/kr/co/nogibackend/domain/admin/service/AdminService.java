package kr.co.nogibackend.domain.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.co.nogibackend.domain.admin.dto.response.UserInfoResponse;
import kr.co.nogibackend.domain.notion.dto.info.NotionDatabaseInfo;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import kr.co.nogibackend.infra.notion.NotionClientImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;
	private final NotionClientImpl notionClientImpl;

	// todo: 리팩토링 필요!
	@Transactional
	public Map<String, List<String>> updateNotionPageId() {

		List<User> users =
				userRepository.findAllUser();

		List<User> filterUsers =
				users
						.stream()
						.filter(user -> user.hasNotionDatabaseId() && user.hasNotionAccessToken())
						.toList();

		Map<String, List<String>> result = new HashMap<>();
		List<String> success = new ArrayList<>();
		List<String> fail = new ArrayList<>();

		for (User user : filterUsers) {
			try {
				NotionDatabaseInfo database =
						notionClientImpl.getDatabase(user.getNotionAccessToken(), user.getNotionDatabaseId());
				user.updateNotionPageId(database.getParent().getPage_id());
				success.add(user.getGithubOwner());
			} catch (Exception e) {
				fail.add(user.getGithubOwner());
			}
		}

		result.put("success", success);
		result.put("fail", fail);
		return result;
	}

	public List<UserInfoResponse> getUserInfo() {
		return UserInfoResponse.from(userRepository.findAll());
	}

}
