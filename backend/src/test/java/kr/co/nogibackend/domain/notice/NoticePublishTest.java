package kr.co.nogibackend.domain.notice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import kr.co.nogibackend.application.notice.command.NoticePublishCommand;
import kr.co.nogibackend.domain.notice.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.service.NoticeCreateService;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class NoticePublishTest {

	private static User testUser;
	@Autowired
	private NotionDataInjector notionDataInjector;
	@Autowired
	private NoticeCreateService noticeCreateService;

	// todo: 공통으로 빼기
	@BeforeAll
	static void setUp() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

		InputStream is =
				User.class
						.getClassLoader()
						.getResourceAsStream("env/UserInfo.json");

		testUser =
				mapper.readValue(is, new TypeReference<>() {
				});

	}

	@Test
	@DisplayName("노션에 공지사항 발행")
	void test1() {
		User user =
				User
						.builder()
						.notionDatabaseId(testUser.getNotionDatabaseId())
						.notionAccessToken(testUser.getNotionAccessToken())
						.build();

		Notice notice =
				Notice
						.builder()
						.title("공지사항 테스트 리팩토링 중..." + new Date())
						.content("텟텟")
						.url("https://nogi.co.kr")
						.build();

		List<PublishNewNoticeResult> results =
				notionDataInjector.publishNewNotice(List.of(user), notice);

		results.forEach(result -> {
			assertTrue(result.isSuccess());
		});

	}

	@Test
	@DisplayName("공지사항 저장")
	void test2() {
		NoticePublishCommand command =
				new NoticePublishCommand("테스트1", "https://nogi.co.kr", "내용1");
		Notice notice = noticeCreateService.create(command);
		assertThat(notice.getTitle()).isEqualTo("테스트1");
		assertThat(notice.getTitle()).isNotNull();
	}

}
