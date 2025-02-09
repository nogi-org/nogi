package kr.co.nogibackend.domain.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.github.dto.command.GithubCommitCommand;
import kr.co.nogibackend.domain.github.dto.info.GithubBlobInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateTreeInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateBlobRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateCommitRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.domain.github
  File Name    : GithubService
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  : GitHub API를 호출하는 서비스
 */
@Service
@RequiredArgsConstructor
public class GithubService {

	private final GithubClient githubClient;
	private static final Set<String> BINARY_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg", ".gif");

	public List<Boolean> commitToGithub(List<GithubCommitCommand> commands) {
		return commands.stream().map(this::commitToGithub).toList();
	}

	// TODO 성공 실패여부 객체를 리턴하도록 수정
	public boolean commitToGithub(GithubCommitCommand command) {
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

			return true;
		} catch (Exception e) {
			return false;
		}
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
}