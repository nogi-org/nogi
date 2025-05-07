package kr.co.nogibackend.environment;

import kr.co.nogibackend.domain.github.command.GithubRepoCommand;
import kr.co.nogibackend.infra.github.client.GithubFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GithubTestEnvironment {

	@Value("${github.nogi-bot.owner}")
	protected String nogiBotOwner;
	@Value("${github.nogi-bot.token}")
	protected String nogiBotToken;
	@Value("${github.nogi-bot.email}")
	protected String nogiBotEmail;
	@Value("${github.nogi-bot.repo}")
	protected String nogiBotRepo;
	@Value("${github.test-user.owner}")
	protected String testUserOwner;
	@Value("${github.test-user.token}")
	protected String testUserToken;
	@Value("${github.test-user.repo}")
	protected String testUserRepo;
	@Autowired
	protected GithubFeignClient githubClient;

	@BeforeEach
	public void createUsers() {

		try {
      /*
      저장소 삭제
      전체 권한을 가진 토큰을 따로 발급받아서 사용하고 있고, 유저들에게 받는 토큰은 read, write 권한만 가지고 있다.
      테스트 실행 후 눈으로 데이터를 보기위해 deleteRepository 를 AfterEach 에 두지 않음
       */
			githubClient.deleteRepository(testUserOwner, testUserRepo, testUserToken);
		} catch (Exception ignored) {
		} finally {
			// 저장소 생성
			githubClient.createUserRepository(
					new GithubRepoCommand(testUserRepo, true), testUserToken
			);
		}
	}

}
