package kr.co.nogibackend.domain.notice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notion.dto.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class NoticePublish {

	@Autowired
	private NotionDataInjector notionDataInjector;
	private static User testUser;

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
	@DisplayName("노션에 공지사항 정상 발행")
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
						.title("공지사항 테스트" + new Date())
						.content("텟텟")
						.url("https://nogi.co.kr")
						.build();

		List<PublishNewNoticeResult> results =
				notionDataInjector.publishNewNotice(List.of(user), notice);

		results.forEach(result -> {
			System.out.println(result.isSuccess());
		});

	}


}
