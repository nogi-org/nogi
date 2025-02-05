package kr.co.nogibackend.domain.github;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.github.dto.command.GithubCommitCommand;
import kr.co.nogibackend.domain.github.dto.info.GithubTreeInfo;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import kr.co.nogibackend.domain.user.NogiHistoryType;
import kr.co.nogibackend.infra.github.GithubFeignClient;
import kr.co.nogibackend.infra.github.dto.GithubBlobRequest;
import kr.co.nogibackend.infra.github.dto.GithubBlobResponse;
import kr.co.nogibackend.infra.github.dto.GithubCommitRequest;
import kr.co.nogibackend.infra.github.dto.GithubCommitResponse;
import kr.co.nogibackend.infra.github.dto.GithubTreeRequest;
import kr.co.nogibackend.infra.github.dto.GithubTreeResponse;
import kr.co.nogibackend.infra.github.dto.GithubUpdateReferenceRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GithubService {

	private final GithubFeignClient githubFeignClient;

	// TODO owner, repository, branch 를 어떻게 관리할지 정책결정하기
	private final static String REPO = "nogi-TIL";
	private final static String BRANCH = "main";

	public void commitToGithub(List<GithubCommitCommand> commands) {

	}

	public void commitToGithub(GithubCommitCommand command) {

		String owner = command.userName();
		String repo = REPO;
		String branch = BRANCH;
		String githubToken = command.githubToken();

		// 현재 브랜치의 HEAD 커밋 가져오기
		String latestCommitSha = getLatestCommitSha(owner, repo, branch, githubToken);

		// 기존 Git Tree 가져오기
		String latestTreeSha = getLatestTreeSha(owner, repo, latestCommitSha, githubToken);

		// 🚀 기존 GitHub Tree에서 삭제할 파일을 제외한 새로운 Tree 생성
		List<GithubTreeInfo.GithubTreeNode> treeEntries = new ArrayList<>();

		// 🔹 기존 파일에서 삭제할 파일 제외
		List<String> filesToDelete = getFilesToDelete(command);
		List<GithubTreeInfo.GithubTreeNode> existingTreeEntries = getExistingTreeEntries(owner, repo, latestTreeSha,
			githubToken);

		for (GithubTreeInfo.GithubTreeNode entry : existingTreeEntries) {
			if (!filesToDelete.contains(entry.path())) {
				treeEntries.add(entry); // 유지할 파일만 새로운 Tree에 포함
			}
		}

		// 🔹 새롭게 추가할 파일 등록
		Map<String, String> filesToUpload = prepareFiles(command);
		for (Map.Entry<String, String> entry : filesToUpload.entrySet()) {
			boolean isBinary = entry.getKey().endsWith(".png") || entry.getKey().endsWith(".jpg");

			GithubBlobResponse blobResponse = githubFeignClient.createBlob(
				owner, repo,
				new GithubBlobRequest(entry.getValue(), isBinary ? "base64" : "utf-8"),
				githubToken
			);

			treeEntries.add(new GithubTreeRequest.TreeEntry(entry.getKey(), "100644", "blob", blobResponse.sha()));
		}

		// 🚀 새로운 Git Tree 생성 (삭제할 파일 제외, 새 파일 포함)
		GithubTreeResponse treeResponse = githubFeignClient.createTree(
			owner, repo,
			new GithubTreeRequest(latestCommitSha, treeEntries), githubToken
		);

		// 🚀 하나의 커밋으로 모든 변경 사항 반영 (삭제 + 추가 포함)
		GithubCommitRequest.AuthorCommitter authorCommitter = new GithubCommitRequest.AuthorCommitter(
			"5wontaek", "onetaekoh@gmail.com", command.commitDate());

		GithubCommitResponse commitResponse = githubFeignClient.createCommit(
			owner, repo,
			new GithubCommitRequest("Batch commit files", treeResponse.sha(), List.of(latestCommitSha),
				authorCommitter, authorCommitter),
			githubToken
		);

		// 🚀 브랜치 업데이트
		githubFeignClient.updateBranch(owner, repo, branch,
			new GithubUpdateReferenceRequest(commitResponse.sha()), githubToken);

		System.out.println("✅ GitHub 커밋 완료! SHA: " + commitResponse.sha());
	}

	// 현재 브랜치의 최신 SHA 가져오기
	private String getLatestCommitSha(String owner, String repo, String branch, String token) {
		return githubFeignClient.getBranch(owner, repo, branch, token).commit().sha();
	}

	// 최신 Tree SHA 가져오기
	private String getLatestTreeSha(String owner, String repo, String commitSha, String token) {
		return githubFeignClient.getCommit(owner, repo, commitSha, token).tree().sha();
	}

	// 기존 Git Tree 정보 가져오기
	private List<GithubTreeInfo.GithubTreeNode> getExistingTreeEntries(String owner, String repo, String treeSha,
		String token) {
		return githubFeignClient.getTree(owner, repo, treeSha, token).tree();
	}

	// 삭제할 파일 목록 가져오기
	private List<String> getFilesToDelete(GithubCommitCommand command) {
		List<String> filesToDelete = new ArrayList<>();

		if (command.type() == NogiHistoryType.UPDATE_TITLE_OR_CATEGORY) {
			filesToDelete.add(command.prevCategory() + "/" + command.prevTitle() + ".md");
		}

		for (NotionStartTILResult.ImageOfNotionBlock image : command.images()) {
			filesToDelete.add(image.filePath());
		}
		return filesToDelete;
	}

	// 업로드할 파일 준비 (마크다운 + 이미지)
	private Map<String, String> prepareFiles(GithubCommitCommand command) {
		Map<String, String> files = new HashMap<>();
		files.put(command.newCategory() + "/" + command.newTitle() + ".md", command.content());

		for (NotionStartTILResult.ImageOfNotionBlock image : command.images()) {
			files.put(image.filePath(), image.fileEnc64());
		}
		return files;
	}

	/**
	 * Github에 커밋을 할 때 2가지 case가 있다.
	 * 1. 이미 존재하는 파일을 수정하는 경우
	 * 2. 새로운 파일을 추가하는 경우
	 */
	public void uploadMultipleFiles(
		String owner,
		String repo,
		String branch,
		String token,
		Map<String, String> files,
		String commitDate // 추가된 매개변수 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ssZ)
	) {

		// 1️⃣ 현재 브랜치의 HEAD 커밋 가져오기
		String latestCommitSha = getLatestCommitSha(owner, repo, branch, token);

		// 2️⃣ 각 파일을 Blob으로 변환하여 GitHub에 업로드
		List<GithubTreeRequest.TreeEntry> treeEntries = new ArrayList<>();
		for (Map.Entry<String, String> entry : files.entrySet()) {
			String path = entry.getKey();
			String content = entry.getValue();

			// 파일 확장자로 Base64 인코딩 여부 결정
			boolean isBinary =
				path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif");

			// 파일을 Blob으로 업로드하여 SHA 값 받기
			GithubBlobResponse blobResponse = githubFeignClient.createBlob(owner, repo,
				new GithubBlobRequest(content, isBinary ? "base64" : "utf-8"), token);

			treeEntries.add(new GithubTreeRequest.TreeEntry(path, "100644", "blob", blobResponse.sha()));
		}

		// 3️⃣ 새로운 Git Tree 생성
		GithubTreeResponse treeResponse = githubFeignClient.createTree(owner, repo,
			new GithubTreeRequest(latestCommitSha, treeEntries), token);

		// 4️⃣ 새로운 Commit 생성 (커밋 날짜 지정)
		GithubCommitRequest.AuthorCommitter authorCommitter = new GithubCommitRequest.AuthorCommitter(
			"5wontaek", // GitHub에서 설정된 사용자 이름
			"onetaekoh@gmail.com", // GitHub 이메일
			commitDate // 커밋 날짜 (ISO-8601 형식)
		);

		GithubCommitResponse commitResponse = githubFeignClient.createCommit(owner, repo,
			new GithubCommitRequest(
				"Batch commit multiple files",
				treeResponse.sha(),
				List.of(latestCommitSha),
				authorCommitter, // author 정보 추가
				authorCommitter  // committer 정보 추가
			),
			token
		);

		// 5️⃣ 브랜치 업데이트 (HEAD 이동)
		githubFeignClient.updateBranch(owner, repo, branch, new GithubUpdateReferenceRequest(commitResponse.sha()),
			token);

		System.out.println("✅ 여러 파일 커밋 완료! SHA: " + commitResponse.sha());
	}

}
