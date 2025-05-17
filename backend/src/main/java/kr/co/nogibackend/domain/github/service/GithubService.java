package kr.co.nogibackend.domain.github.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kr.co.nogibackend.domain.github.command.GithubAddCollaboratorsCommand;
import kr.co.nogibackend.domain.github.command.GithubCommitCommand;
import kr.co.nogibackend.domain.github.command.GithubCommitCommand.NogiBot;
import kr.co.nogibackend.domain.github.command.GithubCreateBlobCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateCommitCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateIssueCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateOrUpdateContentCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateOrUpdateContentCommand.GithubCommitter;
import kr.co.nogibackend.domain.github.command.GithubCreateTreeCommand;
import kr.co.nogibackend.domain.github.command.GithubGetRepositoryCommand;
import kr.co.nogibackend.domain.github.command.GithubNotifyCommand;
import kr.co.nogibackend.domain.github.command.GithubOAuthAccessTokenCommand;
import kr.co.nogibackend.domain.github.command.GithubRepoCommand;
import kr.co.nogibackend.domain.github.command.GithubUpdateReferenceCommand;
import kr.co.nogibackend.domain.github.port.GithubClientPort;
import kr.co.nogibackend.domain.github.result.GithubBlobResult;
import kr.co.nogibackend.domain.github.result.GithubCommitResult;
import kr.co.nogibackend.domain.github.result.GithubCreateCommitResult;
import kr.co.nogibackend.domain.github.result.GithubCreateTreeResult;
import kr.co.nogibackend.domain.github.result.GithubOauthAccessTokenResult;
import kr.co.nogibackend.domain.github.result.GithubRepoResult;
import kr.co.nogibackend.domain.github.result.GithubUserDetailResult;
import kr.co.nogibackend.domain.github.result.GithubUserEmailResult;
import kr.co.nogibackend.global.config.context.ExecutionResultContext;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.global.response.code.GitResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubService {

	@Value("${github.resources-base-path}")
	private String resourcesBasePath;

	private static final Set<String> BINARY_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg", ".gif");
	private final GithubClientPort githubClientPort;

	public List<GithubCommitResult> commitToGithub(List<GithubCommitCommand> commands) {
		return commands.stream()
				.map(this::commitToGithub)
				.flatMap(Optional::stream)
				.toList();
	}

	/**
	 * <h2>🚀 GitHub에 파일을 커밋하는 메서드</h2>
	 *
	 * <ul>
	 *   <li>1️⃣ 현재 브랜치의 최신 HEAD 커밋 SHA 조회</li>
	 *   <li>2️⃣ 파일을 Blob으로 변환 후 TreeEntry 생성</li>
	 *   <li>3️⃣ 새로운 Git Tree 생성</li>
	 *   <li>4️⃣ 새로운 Commit 생성 (커밋 날짜 포함)</li>
	 *   <li>5️⃣ 브랜치를 최신 커밋으로 업데이트 (HEAD 이동)</li>
	 *   <li>6️⃣ 성공 여부에 따라 GithubCommitResult 반환</li>
	 * </ul>
	 */
	public Optional<GithubCommitResult> commitToGithub(GithubCommitCommand command) {
		try {
			// ✅ markdown 파일 업로드할 때 필요한 정보
			String owner = command.githubOwner();
			String repo = command.githubRepository();
			String branch = command.githubBranch();
			String email = command.githubEmail();
			String token = command.githubToken();
			String message = command.getCommitMessage();
			String date = command.commitDate();
			Map<String, String> markdownFiles = command.prepareFiles();

			// ✅ 이미지 업로드할 때 필요한 정보
			NogiBot nogiBot = command.nogiBot();
			Map<String, String> imageFiles = command.prepareImageFiles(resourcesBasePath);

			// 1️⃣ 현재 브랜치의 HEAD 커밋 SHA 조회 🔄
			String latestSha = getLatestCommitSha(owner, repo, branch, token);

			// 2️⃣ 파일들을 Blob으로 변환하여 TreeEntry 목록 생성 📂
			List<GithubCreateTreeCommand.TreeEntry> treeEntries = createTreeEntries(markdownFiles, owner,
					repo, token);

			// 3️⃣ 새로운 Git Tree 생성 🌳
			GithubCreateTreeResult tree = createNewTree(owner, repo, latestSha, treeEntries, token);

			// 4️⃣ 새로운 Commit 생성 (커밋 날짜 포함) 📝
			String newCommitSha = createNewCommit(owner, repo, email, message, date, latestSha,
					tree.sha(), token);

			// 5️⃣ 브랜치 업데이트 (HEAD 이동) 🔄
			updateBranch(owner, repo, branch, newCommitSha, token);

			// 6️⃣ 이미지 저장
			uploadImageFiles(imageFiles, nogiBot);

			// 7️⃣ 성공 결과 반환 ✅
			return Optional.of(
					new GithubCommitResult(
							command.userId(),
							command.notionPageId(),
							command.notionBotToken(),
							command.newCategory(),
							command.newTitle(),
							command.content(),
							true // 커밋 성공
					)
			);
		} catch (Exception e) {
			// ❌ 예외 발생 시 로그 기록 및 에러 처리
			log.error("Github commit error", e);

			// ExecutionResultContext에 오류 기록 📌
			ExecutionResultContext.addNotionPageErrorResult(
					"Github에 Commit 중 문제가 발생했어요",
					command.userId()
			);

			// 7️⃣ 실패 결과 반환 ❌
			return Optional.of(
					new GithubCommitResult(
							command.userId(),
							command.notionPageId(),
							command.notionBotToken(),
							command.newCategory(),
							command.newTitle(),
							command.content(),
							false // 커밋 실패
					)
			);
		}
	}

	private void uploadImageFiles(Map<String, String> imageFiles, NogiBot nogiBot) {
		for (Map.Entry<String, String> imageFile : imageFiles.entrySet()) {
			githubClientPort.uploadFile(
					nogiBot.githubOwner(),
					nogiBot.githubRepository(),
					imageFile.getKey(),
					new GithubCreateOrUpdateContentCommand(
							"이미지 파일 업로드",
							imageFile.getValue(),
							new GithubCommitter(
									nogiBot.githubOwner(),
									nogiBot.githubEmail()
							)
					),
					nogiBot.githubToken()
			);
		}
	}


	private List<GithubCreateTreeCommand.TreeEntry> createTreeEntries(
			Map<String, String> files,
			String owner,
			String repo,
			String githubToken
	) {
		List<GithubCreateTreeCommand.TreeEntry> treeEntries = new ArrayList<>();
		for (Map.Entry<String, String> entry : files.entrySet()) {
			String path = entry.getKey();
			String content = entry.getValue();
			boolean isBinary = BINARY_EXTENSIONS.stream()
					.anyMatch(ext -> path.toLowerCase().endsWith(ext));

			String sha = null;
			if (content != null) {
				GithubBlobResult blobResponse = githubClientPort.createBlob(
						owner,
						repo,
						new GithubCreateBlobCommand(content, isBinary ? "base64" : "utf-8"),
						githubToken
				);
				sha = blobResponse.sha();
			}

			treeEntries.add(new GithubCreateTreeCommand.TreeEntry(path, "100644", "blob", sha));
		}
		return treeEntries;
	}

	private GithubCreateTreeResult createNewTree(
			String owner,
			String repo,
			String baseTreeSha,
			List<GithubCreateTreeCommand.TreeEntry> treeEntries,
			String githubToken
	) {
		GithubCreateTreeCommand treeRequest = new GithubCreateTreeCommand(owner, baseTreeSha,
				treeEntries);
		return githubClientPort.createTree(owner, repo, treeRequest, githubToken);
	}

	private String createNewCommit(
			String owner,
			String repo,
			String email,
			String commitMessage,
			String commitDate,
			String baseCommitSha,
			String newTreeSha,
			String githubToken
	) {
		GithubCreateCommitCommand.AuthorCommitter authorCommitter =
				new GithubCreateCommitCommand.AuthorCommitter(owner, email, commitDate);
		GithubCreateCommitCommand commitRequest = new GithubCreateCommitCommand(
				commitMessage,
				newTreeSha,
				List.of(baseCommitSha),
				authorCommitter,  // author 정보
				authorCommitter   // committer 정보
		);
		GithubCreateCommitResult commitResponse = githubClientPort.createCommit(
				owner,
				repo,
				commitRequest,
				githubToken
		);
		return commitResponse.sha();
	}

	private void updateBranch(
			String owner,
			String repo,
			String branch,
			String commitSha,
			String githubToken
	) {
		GithubUpdateReferenceCommand updateRequest = new GithubUpdateReferenceCommand(commitSha, true);
		githubClientPort.updateBranch(owner, repo, branch, updateRequest, githubToken);
	}

	// 현재 브랜치의 최신 SHA 가져오기
	private String getLatestCommitSha(String owner, String repo, String branch, String token) {
		return githubClientPort.getBranch(owner, repo, branch, token).commit().sha();
	}

	public void notify(GithubNotifyCommand command) {
		Map<Long, GithubNotifyCommand.GithubUser> userMap = command.userMap();

		Map<Long, List<ExecutionResultContext.ProcessingResult>> executionResultMap =
				ExecutionResultContext.getResults().stream()
						.collect(Collectors.groupingBy(ExecutionResultContext.ProcessingResult::userId));

		executionResultMap.forEach((userId, results) -> {
			GithubNotifyCommand.GithubUser githubUser = userMap.get(userId);

			String title = this.generateTitle(results); // 제목 동적 생성
			String markdownMessage = this.createMarkdownMessage(results, githubUser.owner(), title);

			// GitHub Issue 생성
			githubClientPort.createIssue(
					githubUser.owner(),
					githubUser.Repo(),
					new GithubCreateIssueCommand(
							title,
							markdownMessage,
							List.of(githubUser.owner())
					),
					command.masterUser().AuthToken()
			);
		});
	}

	private String generateTitle(List<ExecutionResultContext.ProcessingResult> results) {
		if (results.size() == 1) {
			ExecutionResultContext.ProcessingResult result = results.get(0);
			return (result.success() ? "✅ " : "❌ ") + result.message(); // 단건일 때 메시지를 제목으로
		}

		long successCount = results.stream().filter(ExecutionResultContext.ProcessingResult::success)
				.count();
		long failureCount = results.size() - successCount;

		return successCount + "건 성공, " + failureCount + "건 실패"; // 여러 건일 때 개수 표시
	}

	private String createMarkdownMessage(List<ExecutionResultContext.ProcessingResult> results,
			String owner,
			String title) {
		StringBuilder sb = new StringBuilder();

		sb.append("### ").append(title).append("\n\n"); // 🔹 동적으로 제목 삽입

		if (results.size() > 1) { // 여러 건이면 상세 메시지 출력
			results.forEach(result -> {
				if (result.success()) {
					sb.append("✅ ").append(result.message()).append("\n");
				} else {
					sb.append("❌ ").append(result.message()).append("\n");
				}
			});
		}

		sb.append("\n@").append(owner);
		return sb.toString();
	}

	public GithubOauthAccessTokenResult getAccessToken(GithubOAuthAccessTokenCommand publicRepo) {
		return githubClientPort.getAccessToken(publicRepo);
	}

	public kr.co.nogibackend.domain.github.result.GithubUserResult getUserInfo(String accessToken) {

		GithubUserDetailResult userInfo = githubClientPort.getUserInfo(accessToken);
		List<GithubUserEmailResult> userEmails = githubClientPort.getUserEmails(accessToken);
		GithubUserEmailResult primaryEmail = userEmails.stream()
				.filter(GithubUserEmailResult::primary)
				.findFirst()
				.orElseThrow(() -> new GlobalException(GitResponseCode.F_PRIMARY_EMAIL_NOTFOUND));

		return kr.co.nogibackend.domain.github.result.GithubUserResult.from(
				userInfo,
				primaryEmail // primary 이메일 사용
		);
	}

	public GithubRepoResult createRepository(String repositoryName, String accessToken) {
		return githubClientPort.createUserRepository(
				new GithubRepoCommand(repositoryName, true),
				accessToken
		);
	}

	public boolean isUniqueRepositoryName(GithubGetRepositoryCommand command) {
		return githubClientPort.isUniqueRepositoryName(
				command.owner(),
				command.repoName(),
				command.token()
		);
	}

	public void addCollaborator(
			kr.co.nogibackend.domain.github.command.GithubAddCollaboratorCommand command) {
		try {
			githubClientPort.addCollaborator(
					command.owner(),
					command.repo(),
					command.username(),
					new GithubAddCollaboratorsCommand(null),
					command.accessToken()
			);
		} catch (Exception e) {
			log.error("Collaborator 추가 중 오류 발생", e);
		}

	}

	public GithubRepoResult getRepositoryInfo(String owner, String repoName, String token) {
		return githubClientPort.getOwnerRepositoryInfo(owner, repoName, token);
	}
}
