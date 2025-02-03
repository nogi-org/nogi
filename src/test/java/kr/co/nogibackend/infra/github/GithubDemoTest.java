package kr.co.nogibackend.infra.github;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.github.cdimascio.dotenv.Dotenv;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.info.GithubCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubFileCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubFileShaInfo;
import kr.co.nogibackend.infra.github.dto.GithubFileDeleteRequest;
import kr.co.nogibackend.infra.github.dto.GithubFileRequest;

@SpringBootTest
@ActiveProfiles("test-github")
public class GithubDemoTest {

	@Autowired
	private GithubFeignClient githubFeignClient;

	@Autowired
	private GithubService githubService;

	@Value("${github.token}")
	private String token;// 환경변수로 주입
	private String owner;// beforeEach 에서 초기화
	private final String repo = "nogi-test-repo";
	private String barerToken;// beforeEach 에서 초기화
	private static Dotenv dotenv;// .env 파일 로드

	@BeforeAll
	static void setup() {
		dotenv = Dotenv.load(); // .env 파일 로드
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("github.token", () -> dotenv.get("TEST_GITHUB_TOKEN"));
	}

	@BeforeEach
	public void setUp() {
		// Bearer Token
		barerToken = "Bearer " + token;

		// // Github 저장소 생성
		// GithubRepoInfo response = githubFeignClient.createUserRepository(
		// 	new GithubRepoRequest(repo, true), barerToken
		// );
		// System.out.println("Github 저장소 생성 완료: " + response);
		//
		// // 저장소 소유자 정보
		// owner = response.owner().login();
		owner = "5wontaek";
	}

	@AfterEach
	public void tearDown() {
		// 저장소 삭제
		//githubFeignClient.deleteRepository("5wontaek", "nogi-test-repo", barerToken);
	}

	@Test
	void testCreateAndDeleteCommit() {
		// Given: GitHub 저장소 및 파일 정보
		String path = "test/hello.txt"; // 저장할 파일 경로

		// 파일 내용을 Base64 인코딩
		String content = "Hello, GitHub!";
		String base64Content = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));

		// 요청 데이터 생성
		GithubFileRequest request = new GithubFileRequest(
			"Test commit: Adding hello.txt",
			base64Content,
			null,  // 신규 파일이므로 SHA 없음
			"main"
		);

		// When: GitHub API 호출
		GithubFileCommitInfo response = githubFeignClient.createOrUpdateFile(owner, repo, path, request, barerToken);

		// Then: 응답 검증
		assertNotNull(response);
		assertNotNull(response.content());
		assertNotNull(response.commit());
		assertNotNull(response.commit().sha());
		System.out.println("response = " + response);

		GithubFileShaInfo fileInfo = githubFeignClient.getFileInfo(owner, repo, path, barerToken);
		String sha = fileInfo.sha();
		System.out.println("sha = " + sha);

		// When: GitHub API 호출
		githubFeignClient.deleteFile(owner, repo, path,
			new GithubFileDeleteRequest("Test commit: delete hello.txt", sha,
				new GithubFileDeleteRequest.GithubCommitter("5wontaek", "onetaekoh@gmail.com"),
				"main"),
			barerToken
		);

		// Then: 응답 검증
		assertNotNull(response);

		System.out.println("✅ 파일 삭제 성공! 커밋 SHA: " + response.commit().sha());
	}

	@Test
	void testGetCommitsFromGitHub() {
		// When: GitHub API 호출
		List<GithubCommitInfo> commits = githubFeignClient.getCommits(owner, repo, 5, 1, barerToken);

		// Then: 응답 검증
		assertNotNull(commits);
	}

	@Test
	void testCreateOrUpdateFileOnGitHub() {
		// Given: GitHub 저장소 및 파일 정보
		String path = "test/hello.txt"; // 저장할 파일 경로

		// 파일 내용을 Base64 인코딩
		String content = "Hello, GitHub!";
		String base64Content = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));

		// 요청 데이터 생성
		GithubFileRequest request = new GithubFileRequest(
			"Test commit: Adding hello.txt",
			base64Content,
			null,  // 신규 파일이므로 SHA 없음
			"main"
		);

		// When: GitHub API 호출
		GithubFileCommitInfo response = githubFeignClient.createOrUpdateFile(owner, repo, path, request, barerToken);

		// Then: 응답 검증
		assertNotNull(response);
		assertNotNull(response.content());
		assertNotNull(response.commit());
		assertNotNull(response.commit().sha());

		System.out.println("✅ 파일 생성 성공! 커밋 SHA: " + response.commit().sha());
	}

	@Test
	void testCreateOrUpdateFileOnGitHub2() {
		// Given: GitHub 저장소 및 파일 정보
		String path = "test-folder/hello.txt"; // 저장할 파일 경로

		// 파일 내용을 Base64 인코딩
		String content = "Hello, GitHub! Updated!33333333";
		String base64Content = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));

		GithubFileShaInfo fileInfo = githubFeignClient.getFileInfo(owner, repo, path, barerToken);
		String sha = fileInfo.sha();
		System.out.println("sha = " + sha);

		// 요청 데이터 생성
		GithubFileRequest request = new GithubFileRequest(
			"Test commit: update hello.txt",
			base64Content,
			sha,  // 신규 파일이므로 SHA 없음
			"main"
		);

		// When: GitHub API 호출
		GithubFileCommitInfo response = githubFeignClient.createOrUpdateFile(owner, repo, path, request, barerToken);

		// Then: 응답 검증
		assertNotNull(response);
		assertNotNull(response.content());
		assertNotNull(response.commit());
		assertNotNull(response.commit().sha());

		System.out.println("✅ 파일 생성 성공! 커밋 SHA: " + response.commit().sha());
	}

	@Test
	void testSha() {
		// Given: GitHub 저장소 및 파일 정보
		String path1 = "test-folder/hello.txt"; // 저장할 파일 경로
		String path2 = "test-folder2/hello2.txt"; // 저장할 파일 경로

		GithubFileShaInfo fileInfo1 = githubFeignClient.getFileInfo(owner, repo, path1, barerToken);
		System.out.println("fileInfo1 = " + fileInfo1);
		GithubFileShaInfo fileInfo2 = githubFeignClient.getFileInfo(owner, repo, path2, barerToken);
		System.out.println("fileInfo2 = " + fileInfo2);
	}

}
