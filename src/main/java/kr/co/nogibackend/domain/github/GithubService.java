package kr.co.nogibackend.domain.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
		Map<String, String> files) {

		// 1️⃣ 현재 브랜치의 HEAD 커밋 가져오기
		String latestCommitSha = getLatestCommitSha(owner, repo, branch, token);

		// 2️⃣ 각 파일을 Blob으로 변환하여 GitHub에 업로드
		List<GithubTreeRequest.TreeEntry> treeEntries = new ArrayList<>();
		for (Map.Entry<String, String> entry : files.entrySet()) {
			String path = entry.getKey();
			String content = entry.getValue();

			// 파일을 Blob으로 업로드하여 SHA 값 받기
			GithubBlobResponse blobResponse = githubFeignClient.createBlob(owner, repo, GithubBlobRequest.of(content),
				token);
			treeEntries.add(new GithubTreeRequest.TreeEntry(path, "100644", "blob", blobResponse.sha()));
		}

		// 3️⃣ 새로운 Git Tree 생성
		GithubTreeResponse treeResponse = githubFeignClient.createTree(owner, repo,
			new GithubTreeRequest(latestCommitSha, treeEntries), token);

		// 4️⃣ 새로운 Commit 생성
		GithubCommitResponse commitResponse = githubFeignClient.createCommit(owner, repo,
			new GithubCommitRequest("Batch commit multiple files", treeResponse.sha(), List.of(latestCommitSha)),
			token);

		// 5️⃣ 브랜치 업데이트 (HEAD 이동)
		githubFeignClient.updateBranch(owner, repo, branch, new GithubUpdateReferenceRequest(commitResponse.sha()),
			token);

		System.out.println("✅ 여러 파일 커밋 완료! SHA: " + commitResponse.sha());
	}

	// 현재 브랜치의 최신 SHA 가져오기 (API 호출 필요)
	public String getLatestCommitSha(String owner, String repo, String branch, String token) {
		return githubFeignClient.getBranch(owner, repo, branch, token).commit().sha();
	}

}
