package kr.co.nogibackend.domain.notion.service;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_NOT_FOUND_USER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kr.co.nogibackend.domain.notion.port.NotionClientPort;
import kr.co.nogibackend.domain.notion.result.NotionDatabaseResult;
import kr.co.nogibackend.domain.user.entity.User;
import kr.co.nogibackend.domain.user.port.UserRepositoryPort;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.interfaces.notion.response.NotionDatabasePropertyCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionDatabaseService {

	private final NotionClientPort notionClientPort;
	private final UserRepositoryPort userRepositoryPort;
	private final ObjectMapper objectMapper;

	// todo: objectMapper 예외 처리 다시 하기
	public String getDatabase(Long userId) {
		User user =
				userRepositoryPort
						.findById(userId)
						.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
		NotionDatabaseResult database =
				notionClientPort.getDatabase(user.getNotionAccessToken(), user.getNotionDatabaseId());
		try {
			return objectMapper.writeValueAsString(database);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public NotionDatabasePropertyCreateResponse createProperty(Map<String, Object> properties) {
		// todo: 휴면 계정을 제외한 유저들 조회
		List<User> users =
				userRepositoryPort
						.findAll()
						.stream()
						.filter(user ->
								user.hasNotionDatabaseId()
										&& user.hasNotionPageId()
										&& user.hasNotionAccessToken())
						.toList();

		List<Long> successIds = new ArrayList<>();
		List<Long> failIds = new ArrayList<>();
		for (User user : users) {
			try {
				notionClientPort
						.patchDatabase(user.getNotionAccessToken(), user.getNotionDatabaseId(), properties);
				successIds.add(user.getId());
			} catch (Exception e) {
				failIds.add(user.getId());
			}
		}

		return new NotionDatabasePropertyCreateResponse(successIds, failIds);
	}

}
