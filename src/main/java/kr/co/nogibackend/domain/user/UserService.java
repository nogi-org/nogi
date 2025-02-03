package kr.co.nogibackend.domain.user;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.user.dto.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	/*
	Notion 에서 가져온 페이지 정보가 아래 3가지 중 어떤 케이스인지 체크
	1. 생성 : nogiHistory 에 notionPageId 가 없는 경우
	2. 파일내용 : nogiHsitory 에 notionPageId 가 있으나 category 와 title 은 그대로인 경우
	3. 파일제목 또는 카테고리 수정 : nogiHistory 에 notionPageId 가 있으나 category 또는 title 이 변경된 경우

	notionPageId 가 고유한 값이라고 가정하고 작업함
	 */
	public void checkTIL(List<UserCheckTILCommand> commands) {

	}

	public UserCheckTILResult checkTIL(UserCheckTILCommand command) {
		return userRepository.findByUserIdAndNotionPageId(command.userId(), command.notionPageId())
			.map(nogiHistory -> {
				NogiHistoryType historyType =
					nogiHistory.getCategory().equals(command.category()) &&
						nogiHistory.getTitle().equals(command.title())
						? NogiHistoryType.UPDATE_CONTENT
						: NogiHistoryType.UPDATE_TITLE_OR_CATEGORY;

				return UserCheckTILResult.of(
					command.userId(),
					command.notionPageId(),
					historyType,
					command.category(),
					command.title()
				);
			})
			.orElseGet(() -> UserCheckTILResult.of(
					command.userId(),
					command.notionPageId(),
					NogiHistoryType.CREATE,
					command.category(),
					command.title()
				)
			);

	}
}
