package kr.co.nogibackend.domain.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.github.dto.command.GithubCommitCommand;
import kr.co.nogibackend.domain.github.dto.command.GithubNotifyCommand;
import kr.co.nogibackend.domain.github.dto.info.GithubBlobInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateTreeInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubOauthAccessTokenInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateBlobRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateCommitRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateIssueRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubRepoRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;
import kr.co.nogibackend.domain.github.dto.result.GithubCommitResult;
import kr.co.nogibackend.domain.github.dto.result.GithubUserResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
  Package Name : kr.co.nogibackend.domain.github
  File Name    : GithubService
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  : GitHub API를 호출하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GithubService {

	private static final Set<String> BINARY_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg", ".gif");
	private final GithubClient githubClient;

	public List<GithubCommitResult> commitToGithub(List<GithubCommitCommand> commands) {
		return commands.stream()
			.map(this::commitToGithub)
			.flatMap(Optional::stream)
			.toList();
	}

	// TODO 성공 실패여부 객체를 리턴하도록 수정
	public Optional<GithubCommitResult> commitToGithub(GithubCommitCommand command) {
		try {
			String owner = command.githubOwner();
			String repo = command.githubRepository();
			String branch = command.githubBranch();
			String email = command.githubEmail();
			String token = command.githubToken();
			String message = command.getCommitMessage();
			String date = command.commitDate();
			Map<String, String> files = command.prepareFiles();

			// 1️⃣ 현재 브랜치의 HEAD 커밋 가져오기
			String latestSha = getLatestCommitSha(owner, repo, branch, token);

			// 2️⃣ 파일들을 Blob 으로 변환하여 TreeEntry 목록 생성
			List<GithubCreateTreeRequest.TreeEntry> treeEntries = createTreeEntries(files, owner, repo, token);

			// 3️⃣ 새로운 Git Tree 생성
			GithubCreateTreeInfo tree = createNewTree(owner, repo, latestSha, treeEntries, token);

			// 4️⃣ 새로운 Commit 생성 (커밋 날짜 지정)
			String newCommitSha = createNewCommit(owner, repo, email, message, date, latestSha, tree.sha(), token);

			// 5️⃣ 브랜치 업데이트 (HEAD 이동)
			updateBranch(owner, repo, branch, newCommitSha, token);

			return Optional.of(
				new GithubCommitResult(
					command.userId(),
					command.notionPageId(),
					command.notionAuthToken(),
					command.newCategory(),
					command.newTitle()
				)
			);
		} catch (Exception e) {
			log.error("Github commit error", e);
			ExecutionResultContext.addNotionPageErrorResult(
				"Github에 Commit 중 문제가 발생했어요",
				command.userId()
			);
		}
		return Optional.empty();
	}

	private List<GithubCreateTreeRequest.TreeEntry> createTreeEntries(
		Map<String, String> files,
		String owner,
		String repo,
		String githubToken
	) {
		List<GithubCreateTreeRequest.TreeEntry> treeEntries = new ArrayList<>();
		for (Map.Entry<String, String> entry : files.entrySet()) {
			String path = entry.getKey();
			String content = entry.getValue();
			boolean isBinary = BINARY_EXTENSIONS.stream()
				.anyMatch(ext -> path.toLowerCase().endsWith(ext));

			String sha = null;
			if (content != null) {
				GithubBlobInfo blobResponse = githubClient.createBlob(
					owner,
					repo,
					new GithubCreateBlobRequest(content, isBinary ? "base64" : "utf-8"),
					githubToken
				);
				sha = blobResponse.sha();
			}

			treeEntries.add(new GithubCreateTreeRequest.TreeEntry(path, "100644", "blob", sha));
		}
		return treeEntries;
	}

	private GithubCreateTreeInfo createNewTree(
		String owner,
		String repo,
		String baseTreeSha,
		List<GithubCreateTreeRequest.TreeEntry> treeEntries,
		String githubToken
	) {
		GithubCreateTreeRequest treeRequest = new GithubCreateTreeRequest(owner, baseTreeSha, treeEntries);
		return githubClient.createTree(owner, repo, treeRequest, githubToken);
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
		GithubCreateCommitRequest.AuthorCommitter authorCommitter =
			new GithubCreateCommitRequest.AuthorCommitter(owner, email, commitDate);
		GithubCreateCommitRequest commitRequest = new GithubCreateCommitRequest(
			commitMessage,
			newTreeSha,
			List.of(baseCommitSha),
			authorCommitter,  // author 정보
			authorCommitter   // committer 정보
		);
		GithubCreateCommitInfo commitResponse = githubClient.createCommit(
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
		GithubUpdateReferenceRequest updateRequest = new GithubUpdateReferenceRequest(commitSha, true);
		githubClient.updateBranch(owner, repo, branch, updateRequest, githubToken);
	}

	// 현재 브랜치의 최신 SHA 가져오기
	private String getLatestCommitSha(String owner, String repo, String branch, String token) {
		return githubClient.getBranch(owner, repo, branch, token).commit().sha();
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
			githubClient.createIssue(
				githubUser.owner(),
				githubUser.Repo(),
				new GithubCreateIssueRequest(
					title,
					markdownMessage,
					List.of(githubUser.owner())
				),
				command.masterUser().authToken()
			);
		});
	}

	private String generateTitle(List<ExecutionResultContext.ProcessingResult> results) {
		if (results.size() == 1) {
			ExecutionResultContext.ProcessingResult result = results.get(0);
			return (result.success() ? "✅ " : "❌ ") + result.message(); // 단건일 때 메시지를 제목으로
		}

		long successCount = results.stream().filter(ExecutionResultContext.ProcessingResult::success).count();
		long failureCount = results.size() - successCount;

		return successCount + "건 성공, " + failureCount + "건 실패"; // 여러 건일 때 개수 표시
	}

	private String createMarkdownMessage(List<ExecutionResultContext.ProcessingResult> results, String owner,
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

	public GithubOauthAccessTokenInfo getAccessToken(GithubOAuthAccessTokenRequest publicRepo) {
		return githubClient.getAccessToken(publicRepo);
	}

	public GithubUserResult getUserInfo(String accessToken) {
		String token = "Bearer " + accessToken;

		GithubUserInfo userInfo = githubClient.getUserInfo(token);
		GithubUserEmailInfo userEmails = githubClient.getUserEmails(token);

		return GithubUserResult.from(
			userInfo,
			userEmails
		);
	}

	public void createRepository(String repositoryName, String accessToken) {
		githubClient.createUserRepository(
			new GithubRepoRequest(repositoryName, true),
			accessToken
		);
	}
}