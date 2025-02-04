package kr.co.nogibackend.infra.github;

import java.io.IOException;
import java.nio.file.Files;
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

	private static Dotenv dotenv;// .env 파일 로드
	private final String repo = "nogi-test-repo";
	@Autowired
	private GithubFeignClient githubFeignClient;
	@Autowired
	private GithubService githubService;
	@Value("${github.token}")
	private String token;// 환경변수로 주입
	private String owner;// beforeEach 에서 초기화
	private String barerToken;// beforeEach 에서 초기화

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
			files
		);
	}

	@Test
	void testUploadMultipleFilesSuperpil() throws IOException {
		// 📝 Markdown 파일 (이미지 포함)
		String mdContent = """
			![불쑈이미지!](./Java/image/불쑈.jpeg)
			
			
			이건 일반 텍스트1111<br>1. 순서111
			1. 순서222
			
			* 리스트1
			* 리스트2
			
			# 헤더 1 첫줄 헤더 1 둘째줄zzzz
			## 헤더 2
			### 헤더 3
			
			```java
			public class Java { }
			```
			
			---
			
			
			끝!<br>
			
			""";

		// ✅ 업로드할 파일 목록 생성
		Map<String, String> files = new HashMap<>();
		files.put("Java/nogi슈퍼필2.md", mdContent); // 마크다운 파일 (평문)
		files.put("Java/image/불쑈.jpeg",
			"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HBGlYAEiTzshY0Ca/wBaQCAJmamVw3jy/vQjnBjNRbRoDFAIkmig4jdUVKjFbm+Vibj0DMOnBUwSQWOizMoAByTROlIafYqaiX9s3qrdFiLWbGh0W6HjxirDno5jkqWO/SfQw/KNEuT/AAO+PPRzIqekOk/BgDORomDTiQTH8zLRykLlEB3I7pRHZl+K6IaKSDnIcBmIR2vDKALmAuI4BIPLFS2ASJBOJ6b1TCfGh+Uf4/7lkDJeNgoIxYwcieo8uHk6Hd9o+aWkxhNB04Sw+0A8xQGJ0OWeicUGBlOFiMSQgxDxgbjMQu0Q+zfQ/trdAgHIhBcxAMRgIkq3xlwp3PLJb4hTgLD6aHbLDoEgVwILwwfk0M18OIhlXFkQOQBWC2V+loeLz/n0DEnCPE7lEH33YAQAHJgFcoT5ytAzJyuIRUbhfvQ78EwZ8zB5rBHWHBikj4/nM21gm72CfZEU/p9aHEMOBiFUetyMxkg0Y0o5X+YCaiO/32QDBhlfwUAQATKnDoFQRxg7aIYhH4t8w3MVTRDJCTk1yXVZzzlJYOZJ7IsI6XUkm5/cv5okRwuQYhGERifiyRBPqZQmFI4yicSHsEQQJ5cZhUN0VzU6KDhAyNQbo3BChIMIn3jZmE0dGA3OLWFwo4l2rUUYQHJQGa1KoiYTQX0YbPzwts2DV7FFTcjnIRgECxcTUKDxsdU5wMnMQtym0KgiNRRYzESr9aOfzC7hRARbKhs2RBExgATRXUR4TsIpAKuXZAaBgjpadV/lAEEAgAKaNJYEmQijVc4pABRJcI2I6xOsQu8EDugAJBkYIKJf0x7H0excwx8dIgAElIBP48r+kxWNTU8nSAv7EOgBEmg5TULZR/hQgCs4l30lv/8AgpTnlcmNpDuhBnnpNcGmT0rCCLsjHGTmBAfcrh3KAgTIDaXZ7H4dMBmP8QFymlOU6emQKWLgqREfgChIVZ7UabPQaJ/DwoOs34f5O//aAAwDAQACAAMAAAAQ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++pF+++++++++++++++++++++++++++++++++++++++++++8/e+++++++++++++++++++++++++++++++++++++++++++r81+++++++++++++++++++++++++++++++++++++++++++u8q8+++++++++++++++++++++++++++++++++++++++++++88q+++++++++++++++++++++++++++++++++++++++++++988P8APvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvtPv8Azzxz7777777777777777777777777777777777777hP77hTzy/f777777777777777777777777777777777777Xj77vzzzzn777777777777777777777777777777777771/vrujzzzzvb7777777777777777777777777777777776nzyqPXzzzzxX7777777777777777777777777777777776/wA88J26G882I+++++++++++++++++++++++++++++++++++888mJpX88A9++++++++++++++++++++++++++++++++++68887cMW8422+++++++++++++++++++++++++++++++++++R84C+d88Un+++++++++++++++++++++++++++++++++++++zASZ3yojV+++++++++++++++++++++++++++++++++++++2fjEKcsu++++++++++++++++++++++++++++++++++++++2888ws8+++++++++++++++++++++++++++++++++++++++/8AN7PPvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvuY/Pvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv/vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv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"); // 이미지 파일 (Base64)

		// ✅ When: GitHub API를 사용하여 파일 업로드
		githubService.uploadMultipleFiles(
			owner,
			repo,
			"main",
			barerToken,
			files
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
}