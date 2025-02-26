package kr.co.nogibackend.environment;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import kr.co.nogibackend.domain.github.dto.request.GithubRepoRequest;
import kr.co.nogibackend.infra.github.GithubFeignClient;

@SpringBootTest
@ActiveProfiles("test")
public class GithubTestEnvironment {

	@Value("${github.nogi-bot.owner}")
	protected String nogiBotOwner;
	@Value("${github.nogi-bot.token}")
	protected String nogiBotToken;
	@Value("${github.test-user.owner}")
	protected String testUserOwner;
	@Value("${github.test-user.token}")
	protected String testUserToken;
	@Value("${github.test-user.repo}")
	protected String testUserRepo;
	@Autowired
	protected GithubFeignClient githubFeignClient;

	@BeforeEach
	public void createUsers() {
		// Github Repository 에 유저의 저장소 생성
		try {
			// 저장소 삭제
			// 전체 권한을 가진 토근을 따로 발급받아서 삭제가 가능하다.
			// 테스트 실행 후 눈으로 데이터를 보기위해 deleteRepository 를 AfterEach 에 두지 않음
			githubFeignClient.deleteRepository(testUserOwner, testUserRepo, testUserToken);
		} catch (Exception ignored) {
		} finally {
			// 저장소 생성
			githubFeignClient.createUserRepository(
				new GithubRepoRequest(testUserRepo, true), testUserToken
			);
		}
	}

}
