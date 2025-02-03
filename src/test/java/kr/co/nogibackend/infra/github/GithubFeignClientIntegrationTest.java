package kr.co.nogibackend.infra.github;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.github.cdimascio.dotenv.Dotenv;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.infra.github.dto.GithubRepoRequest;

@SpringBootTest
@ActiveProfiles("test-github")
class GithubFeignClientIntegrationTest {

	@Autowired
	private GithubFeignClient githubFeignClient;

	@Autowired
	private GithubService githubService;

	@Value("${github.token}")
	private String token;// 환경변수로 주입
	private String owner;// beforeEach 에서 초기화
	private final String repo = "nogi-test-repo3";
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
	void createRepo() {
		// Github 저장소 생성
		GithubRepoInfo response = githubFeignClient.createUserRepository(
			new GithubRepoRequest(repo, true), barerToken
		);
		System.out.println("Github 저장소 생성 완료: " + response);

		// 저장소 소유자 정보
		owner = response.owner().login();
	}

	@Test
	void deleteRepo() {
		// 저장소 삭제
		githubFeignClient.deleteRepository(owner, repo, barerToken);
	}

	@Test
	void testUploadMultipleFiles() throws IOException {
		// ✅ 공통 경로 설정 (GitHub 내 이미지 URL)
		String imagePath = "https://raw.githubusercontent.com/5wontaek/nogi-test-repo/main/Java/image";

		// 📝 Markdown 파일 (이미지 포함)
		String mdContent = String.format("""
			# Hello GitHub
			This is a test markdown file with an image.

			![Red Image](%s/이미지1.jpeg)
			![Blue Image](%s/이미지2.jpeg)
			""", imagePath, imagePath);

		// 🔹 resources/image 폴더에서 이미지 읽기 & Base64 변환
		String redImageBase64 = encodeImageToBase64("image/이미지1.jpeg");
		String blueImageBase64 = encodeImageToBase64("image/이미지2.jpg");

		// ✅ 업로드할 파일 목록 생성
		Map<String, String> files = new HashMap<>();
		files.put("Java/노기이름1.md", mdContent); // 마크다운 파일 (평문)
		files.put("Java/image/이미지1.jpeg", redImageBase64); // 이미지 파일 (Base64)
		files.put("Java/image/이미지2.jpeg", blueImageBase64); // 이미지 파일 (Base64)

		// ✅ When: GitHub API를 사용하여 파일 업로드
		githubService.uploadMultipleFiles(
			owner,
			repo,
			"main",
			barerToken,
			files,
			"2025-01-31T16:13:30+12:00"
		);
	}

	/**
	 * 📌 `resources/image` 폴더의 이미지를 읽고 Base64로 변환하는 유틸 메서드
	 */
	private String encodeImageToBase64(String imagePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(imagePath);
		byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
		return Base64.getEncoder().encodeToString(imageBytes);
	}

	@Test
	void testUploadMarkdownFilesWithDateRange() {
		// ✅ 시작 날짜 설정 (2024-01-01 ~ 2024-06-30)
		LocalDate startDate = LocalDate.of(2024, 12, 31);
		LocalDate endDate = LocalDate.of(2025, 2, 1);

		// ✅ 날짜를 하루씩 증가시키면서 반복
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			// 📌 날짜를 12:00 PM (정오)로 설정
			String commitDate = date.atTime(12, 0).atOffset(ZoneOffset.of("+09:00"))
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

			// 📝 Markdown 파일 (이미지 없이)
			String mdContent = String.format("""
				# Hello GitHub
				This is a test markdown file for date %s.

				This commit was created at exactly 12:00 PM on %s.
				""", date, date);

			// ✅ 업로드할 파일 목록 생성 (이미지 제외)
			Map<String, String> files = new HashMap<>();
			files.put("Java/daily_commit_" + date + ".md", mdContent);

			// ✅ GitHub API를 사용하여 파일 업로드
			githubService.uploadMultipleFiles(
				owner,
				repo,
				"main",
				barerToken,
				files,
				commitDate // 날짜별 커밋 적용
			);

			System.out.println("✅ 커밋 완료: " + date + " 12:00 PM");
		}
	}
}