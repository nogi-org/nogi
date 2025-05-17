package kr.co.nogibackend.domain.github;

import java.io.IOException;
import java.util.List;
import kr.co.nogibackend.domain.github.command.GithubCommitCommand;
import kr.co.nogibackend.domain.github.service.GithubService;
import kr.co.nogibackend.domain.sync.constant.SyncHistoryType;
import kr.co.nogibackend.environment.GithubTestEnvironment;
import kr.co.nogibackend.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class GithubServiceIntegrationTest extends GithubTestEnvironment {

	@Autowired
	private GithubService githubService;

	@Test
	@DisplayName("새롭게 TIL을 작성했을 경우 MD 파일과 이미지가 생성된다.")
	void testCommitToGithub() throws IOException {
		// 새로운 TIL 작성
		GithubCommitCommand command = getGithubCommitCommand(
				SyncHistoryType.CREATE_OR_UPDATE_CONTENT,
				"Java",
				"Java 제목",
				"Java",
				"Java 제목"
		);
		githubService.commitToGithub(command);
	}

	@Test
	@DisplayName("새롭게 TIL을 작성하고나서 제목을 수정했을 경우 이전 MD 파일은 삭제되고 새로운 MD 파일이 생성된다.")
	void testUpdateTitleCommitToGithub() throws IOException {
		// 새로운 TIL 작성
		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.CREATE_OR_UPDATE_CONTENT,
						"Java",
						"Java 제목",
						"Java",
						"Java 제목"
				)
		);

		// 제목을 수정한 TIL 작성
		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.UPDATE_TITLE,
						"Java",
						"New Java 제목",
						"Java",
						"Java 제목"
				)
		);
	}

	@Test
	@DisplayName("새롭게 TIL을 작성하고나서 카테고리를 수정했을 경우 이전 MD 파일은 삭제되고 새로운 MD 파일이 생성된다.")
	void testUpdateCategoryCommitToGithub() throws IOException {
		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.CREATE_OR_UPDATE_CONTENT,
						"Java",
						"Java 제목",
						"Java",
						"Java 제목"
				)
		);

		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.UPDATE_CATEGORY,
						"New_Java",
						"New Java 제목 Title",
						"Java",
						"Java 제목"
				)
		);
	}

	@Test
	@DisplayName("같은 카테고리의 TIL을 2개 작성하고, 그 중 하나의 카테고리를 수정했을 경우 이전 MD 파일은 삭제되고 새로운 MD 파일이 생성된다.")
	void testUpdateCategoryCommitToGithub2() throws IOException {
		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.CREATE_OR_UPDATE_CONTENT,
						"Java",
						"Java 제목1",
						"Java",
						"Java 제목"
				)
		);

		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.CREATE_OR_UPDATE_CONTENT,
						"Java",
						"Java 제목2",
						"Java",
						"Java 제목"
				)
		);

		githubService.commitToGithub(
				getGithubCommitCommand(
						SyncHistoryType.UPDATE_CATEGORY,
						"New_Java",
						"New Java 제목 Title",
						"Java",
						"Java 제목2"
				)
		);
	}

	// ============================== private 메서드 ==============================
	private GithubCommitCommand getGithubCommitCommand(
			SyncHistoryType type,
			String newCategory,
			String newTitle,
			String prevCategory,
			String prevTitle
	) throws IOException {
		String image1 = "/image/이미지1.jpeg";
		String image2 = "/image/이미지2.jpg";

		// 📝 Markdown 파일 (이미지 포함)
		String mdContent = String.format("""
				# Hello GitHub
				This is a test markdown file with an image.
				
				![Red Image](.%s)
				![Blue Image](.%s)
				""", image1, image2);

		// ✅ 이미지 정보 리스트 생성
		List<GithubCommitCommand.ImageOfGithub> images = List.of(
				new GithubCommitCommand.ImageOfGithub(
						TestUtil.encodeImageToBase64("image/이미지1.jpeg"),
						"이미지1.jpeg",
						newCategory + image1
				),
				new GithubCommitCommand.ImageOfGithub(
						TestUtil.encodeImageToBase64("image/이미지2.jpg"),
						"이미지2.jpg",
						newCategory + image2
				)
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
				TestUtil.getNowDate(),
				null,
				mdContent,
				testUserToken,
				images,
				new GithubCommitCommand.NogiBot(
						nogiBotToken,
						nogiBotOwner,
						nogiBotRepo,
						"main",
						nogiBotEmail
				)
		);
	}
}
