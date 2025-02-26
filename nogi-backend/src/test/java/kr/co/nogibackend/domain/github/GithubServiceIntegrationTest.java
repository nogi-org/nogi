package kr.co.nogibackend.domain.github;

import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import kr.co.nogibackend.domain.github.dto.command.GithubCommitCommand;
import kr.co.nogibackend.domain.user.NogiHistoryType;
import kr.co.nogibackend.environment.GithubTestEnvironment;

public class GithubServiceIntegrationTest extends GithubTestEnvironment {

	@Autowired
	private GithubService githubService;

	@Test
	@DisplayName("새롭게 TIL을 작성했을 경우 MD 파일과 이미지가 생성된다.")
	void testCommitToGithub() throws IOException {
		// 새로운 TIL 작성
		GithubCommitCommand command = getGithubCommitCommand(
			NogiHistoryType.CREATE_OR_UPDATE_CONTENT,
			"Java",
			"Java 제목",
			"Java",
			"Java 제목",
			"image"
		);
		githubService.commitToGithub(command);
	}

	@Test
	@DisplayName("새롭게 TIL을 작성하고나서 제목을 수정했을 경우 이전 MD 파일은 삭제되고 새로운 MD 파일이 생성된다.")
	void testUpdateTitleCommitToGithub() throws IOException {
		// 새로운 TIL 작성
		githubService.commitToGithub(
			getGithubCommitCommand(
				NogiHistoryType.CREATE_OR_UPDATE_CONTENT,
				"Java",
				"Java 제목",
				"Java",
				"Java 제목",
				"image"
			)
		);

		// 제목을 수정한 TIL 작성
		githubService.commitToGithub(
			getGithubCommitCommand(
				NogiHistoryType.UPDATE_TITLE,
				"Java",
				"New Java 제목",
				"Java",
				"Java 제목",
				"image"
			)
		);
	}

	@Test
	@DisplayName("새롭게 TIL을 작성하고나서 카테고리를 수정했을 경우 이전 카테고리 하위의 모든 파일은 삭제되고, 새로운 카테고리 하위에 파일이 생성된다.")
	void testUpdateCategoryCommitToGithub() throws IOException {
		githubService.commitToGithub(
			getGithubCommitCommand(NogiHistoryType.CREATE_OR_UPDATE_CONTENT, "Java", "Java 제목", "Java", "Java 제목",
				"image")
		);

		githubService.commitToGithub(
			getGithubCommitCommand(NogiHistoryType.UPDATE_CATEGORY, "New_Java", "New Java 제목 Title", "Java", "Java 제목",
				"image")
		);
	}

	private GithubCommitCommand getGithubCommitCommand(
		NogiHistoryType type,
		String newCategory,
		String newTitle,
		String prevCategory,
		String prevTitle,
		String imageFilePath
	) throws IOException {
		// ✅ 공통 경로 설정 (GitHub 내 이미지 URL)
		String rootImgPath =
			"https://raw.githubusercontent.com/5wontaek/nogi-test-repo/main/" + "newCategory/" + imageFilePath;

		// 📝 Markdown 파일 (이미지 포함)
		String mdContent = String.format("""
			# Hello GitHub
			This is a test markdown file with an image.4
						
			![Red Image](%s/이미지1.jpeg)
			![Blue Image](%s/이미지2.jpeg)
			""", rootImgPath, rootImgPath);

		// ✅ 이미지 정보 리스트 생성
		List<GithubCommitCommand.ImageOfGithub> images = List.of(
			new GithubCommitCommand.ImageOfGithub(encodeImageToBase64("image/이미지1.jpeg"), "이미지1.jpeg", imageFilePath),
			new GithubCommitCommand.ImageOfGithub(encodeImageToBase64("image/이미지2.jpg"), "이미지2.jpg", imageFilePath)
		);

		// ✅ GithubCommitCommand 객체 생성 (테스트용)
		return new GithubCommitCommand(
			1001L,
			testUserOwner,
			testUserRepo,
			"main",
			"onetaekoh@gmail.com",
			"notion-page-1234",
			"notion-auth-token",
			type,
			newCategory,
			newTitle,
			prevCategory,
			prevTitle,
			getNowDate(),
			null,
			mdContent,
			testUserToken,
			images
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

	private String getNowDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		return OffsetDateTime.now().format(formatter);
	}
}
