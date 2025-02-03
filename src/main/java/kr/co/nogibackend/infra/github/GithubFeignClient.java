package kr.co.nogibackend.infra.github;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.nogibackend.domain.github.dto.info.GithubCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubFileCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubFileDeleteInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubFileShaInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.infra.github.dto.GithubBlobRequest;
import kr.co.nogibackend.infra.github.dto.GithubBlobResponse;
import kr.co.nogibackend.infra.github.dto.GithubBranchInfo;
import kr.co.nogibackend.infra.github.dto.GithubCommitRequest;
import kr.co.nogibackend.infra.github.dto.GithubCommitResponse;
import kr.co.nogibackend.infra.github.dto.GithubFileDeleteRequest;
import kr.co.nogibackend.infra.github.dto.GithubFileRequest;
import kr.co.nogibackend.infra.github.dto.GithubRepoRequest;
import kr.co.nogibackend.infra.github.dto.GithubTreeRequest;
import kr.co.nogibackend.infra.github.dto.GithubTreeResponse;
import kr.co.nogibackend.infra.github.dto.GithubUpdateReferenceRequest;

@FeignClient(name = "GithubClient", url = "https://api.github.com")
public interface GithubFeignClient {

	/*
	커밋 정보를 조회하는 메서드
	 */
	@GetMapping("/repos/{owner}/{repo}/commits")
	List<GithubCommitInfo> getCommits(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestParam(value = "per_page", defaultValue = "10") int perPage,
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestHeader("Authorization") String token
	);

	/*
	파일정보를 조회하는 메서드(SHA 값 포함)
	https://docs.github.com/ko/rest/repos/contents?apiVersion=2022-11-28#get-repository-content
	 */
	@GetMapping("/repos/{owner}/{repo}/contents/{path}")
	GithubFileShaInfo getFileInfo(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("path") String path,
		@RequestHeader("Authorization") String token
	);

	/*
	파일을 생성하거나 업데이트
	https://docs.github.com/ko/rest/repos/contents?apiVersion=2022-11-28#create-or-update-file-contents
	 */
	@PutMapping("/repos/{owner}/{repo}/contents/{path}")
	GithubFileCommitInfo createOrUpdateFile(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("path") String path,
		@RequestBody GithubFileRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	파일을 삭제하는 메서드
	https://docs.github.com/ko/rest/repos/contents?apiVersion=2022-11-28#delete-a-file
	 */
	@DeleteMapping("/repos/{owner}/{repo}/contents/{path}")
	GithubFileDeleteInfo deleteFile(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("path") String path,
		@RequestBody GithubFileDeleteRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	repository 를 생성하는 메서드
	https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#create-a-repository-for-the-authenticated-user
	 */
	@PostMapping("/user/repos")
	GithubRepoInfo createUserRepository(
		@RequestBody GithubRepoRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	repository 를 삭제하는 메서드
	https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#delete-a-repository
	 */
	@DeleteMapping("/repos/{owner}/{repo}")
	void deleteRepository(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestHeader("Authorization") String token
	);

	// 1️⃣ 파일을 Blob으로 업로드 (Create a Blob)
	@PostMapping("/repos/{owner}/{repo}/git/blobs")
	GithubBlobResponse createBlob(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubBlobRequest request,
		@RequestHeader("Authorization") String token
	);

	// 2️⃣ 여러 개의 Blob을 묶어 Git Tree 생성 (Create a Tree)
	@PostMapping("/repos/{owner}/{repo}/git/trees")
	GithubTreeResponse createTree(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubTreeRequest request,
		@RequestHeader("Authorization") String token
	);

	// 3️⃣ 새로운 Tree로 커밋 생성 (Create a Commit)
	@PostMapping("/repos/{owner}/{repo}/git/commits")
	GithubCommitResponse createCommit(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubCommitRequest request,
		@RequestHeader("Authorization") String token
	);

	// 4️⃣ 브랜치 업데이트 (HEAD 이동) (Update a Reference)
	@PostMapping("/repos/{owner}/{repo}/git/refs/heads/{branch}")
	void updateBranch(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("branch") String branch,
		@RequestBody GithubUpdateReferenceRequest request,
		@RequestHeader("Authorization") String token
	);

	@GetMapping("/repos/{owner}/{repo}/branches/{branch}")
	GithubBranchInfo getBranch(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("branch") String branch,
		@RequestHeader("Authorization") String token
	);
}
