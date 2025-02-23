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

import kr.co.nogibackend.config.openfeign.GitHubFeignClientConfig;
import kr.co.nogibackend.domain.github.dto.info.GithubBlobInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubBranchInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateTreeInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubIssueInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUpdateReferenceInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubAddCollaboratorRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateBlobRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateCommitRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateIssueRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubRepoRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;

/*
  Package Name : kr.co.nogibackend.infra.github
  File Name    : GithubFeignClient
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  : GIT API를 호출하기 위한 Feign Client
 */
@FeignClient(name = "GithubClient", url = "https://api.github.com", configuration = GitHubFeignClientConfig.class)
public interface GithubFeignClient {

	/*
	➡️ 사용자 정보 가져오기
	doc: https://docs.github.com/ko/rest/users?apiVersion=2022-11-28#get-the-authenticated-user
	 */
	@GetMapping("/user")
	GithubUserInfo getUserInfo(@RequestHeader("Authorization") String token);

	/*
	➡️ 사용자 이메일 정보 가져오기
	doc: https://docs.github.com/ko/rest/users?apiVersion=2022-11-28#get-the-authenticated-user
	 */
	@GetMapping("/user/emails")
	List<GithubUserEmailInfo> getUserEmailInfo(@RequestHeader("Authorization") String token);

	/*
	➡️ repository 사용자의 repository 정보 가져오기
	doc: https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#get-a-repository
	 */
	@GetMapping("/repos/{owner}/{repo}")
	GithubRepoInfo getOwnerRepositoryInfo(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repoName,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ repository 생성
	doc: https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#create-a-repository-for-the-authenticated-user
	 */
	@PostMapping("/user/repos")
	GithubRepoInfo createUserRepository(
		@RequestBody GithubRepoRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ repository 삭제
	doc: https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#delete-a-repository
	 */
	@DeleteMapping("/repos/{owner}/{repo}")
	void deleteRepository(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ 최신 브랜치의 Tree SHA 가져오기
	docs: https://docs.github.com/ko/rest/branches/branches?apiVersion=2022-11-28#get-a-branch
	 */
	@GetMapping("/repos/{owner}/{repo}/branches/{branch}")
	GithubBranchInfo getBranch(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("branch") String branch,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ 새로운 파일을 Blob으로 변환하여 업로드
	docs: https://docs.github.com/ko/rest/git/blobs?apiVersion=2022-11-28#create-a-blob
	 */
	@PostMapping("/repos/{owner}/{repo}/git/blobs")
	GithubBlobInfo createBlob(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubCreateBlobRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ 새로운 Git Tree 생성
	docs: https://docs.github.com/ko/rest/git/trees?apiVersion=2022-11-28#create-a-tree
	stackoverflow: https://stackoverflow.com/questions/23637961/how-do-i-mark-a-file-as-deleted-in-a-tree-using-the-github-api
	 */
	@PostMapping("/repos/{owner}/{repo}/git/trees")
	GithubCreateTreeInfo createTree(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubCreateTreeRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ 새로운 커밋 생성
	docs: https://docs.github.com/ko/rest/git/commits?apiVersion=2022-11-28#create-a-commit
	 */
	@PostMapping("/repos/{owner}/{repo}/git/commits")
	GithubCreateCommitInfo createCommit(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubCreateCommitRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ 브랜치 업데이트 (HEAD 이동)
	docs: https://docs.github.com/ko/rest/git/refs?apiVersion=2022-11-28#update-a-reference
	 */
	@PostMapping("/repos/{owner}/{repo}/git/refs/heads/{branch}")
	GithubUpdateReferenceInfo updateBranch(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("branch") String branch,
		@RequestBody GithubUpdateReferenceRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
     ➡️ 이슈 생성 (멘션 포함)
     docs: https://docs.github.com/ko/rest/issues/issues?apiVersion=2022-11-28#create-an-issue
     */
	@PostMapping("/repos/{owner}/{repo}/issues")
	GithubIssueInfo createIssue(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@RequestBody GithubCreateIssueRequest request,
		@RequestHeader("Authorization") String token
	);

	/*
	➡️ 협력자 추가
	docs: https://docs.github.com/ko/rest/repos/collaborators?apiVersion=2022-11-28#add-a-repository-collaborator
	 */
	@PutMapping("/repos/{owner}/{repo}/collaborators/{username}")
	void addCollaborator(
		@PathVariable("owner") String owner,
		@PathVariable("repo") String repo,
		@PathVariable("username") String username,
		@RequestBody GithubAddCollaboratorRequest request,
		@RequestHeader("Authorization") String token
	);

}
