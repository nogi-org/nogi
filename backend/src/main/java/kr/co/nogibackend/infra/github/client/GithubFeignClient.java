package kr.co.nogibackend.infra.github.client;

import java.util.List;
import kr.co.nogibackend.domain.github.command.GithubAddCollaboratorsCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateBlobCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateCommitCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateIssueCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateOrUpdateContentCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateTreeCommand;
import kr.co.nogibackend.domain.github.command.GithubRepoCommand;
import kr.co.nogibackend.domain.github.command.GithubUpdateReferenceCommand;
import kr.co.nogibackend.domain.github.result.GithubBlobResult;
import kr.co.nogibackend.domain.github.result.GithubBranchResult;
import kr.co.nogibackend.domain.github.result.GithubCreateCommitResult;
import kr.co.nogibackend.domain.github.result.GithubCreateTreeResult;
import kr.co.nogibackend.domain.github.result.GithubIssueResult;
import kr.co.nogibackend.domain.github.result.GithubRepoResult;
import kr.co.nogibackend.domain.github.result.GithubTreeResult;
import kr.co.nogibackend.domain.github.result.GithubUpdateReferenceResult;
import kr.co.nogibackend.domain.github.result.GithubUserDetailResult;
import kr.co.nogibackend.domain.github.result.GithubUserEmailResult;
import kr.co.nogibackend.global.config.openfeign.GitHubFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "GithubClient", url = "https://api.github.com", configuration = GitHubFeignClientConfig.class)
public interface GithubFeignClient {

	/*
	➡️ 사용자 정보 가져오기
	doc: https://docs.github.com/ko/rest/users?apiVersion=2022-11-28#get-the-authenticated-user
	 */
	@GetMapping("/user")
	GithubUserDetailResult getUserInfo(@RequestHeader("Authorization") String token);

	/*
	➡️ 사용자 이메일 정보 가져오기
	doc: https://docs.github.com/ko/rest/users?apiVersion=2022-11-28#get-the-authenticated-user
	 */
	@GetMapping("/user/emails")
	List<GithubUserEmailResult> getUserEmailInfo(@RequestHeader("Authorization") String token);

	/*
	➡️ repository 사용자의 repository 정보 가져오기
	doc: https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#get-a-repository
	 */
	@GetMapping("/repos/{owner}/{repo}")
	GithubRepoResult getOwnerRepositoryInfo(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repoName,
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ 유저의 repository 목록 가져오기
	todo: 기본 레포지토리 30개만 들고옴(레포지토리 36개 있는 유저가 깃허브 연결 안된다고 문의들어옴 2025-04-18)
	 하드코딩으로 max 100개 들고 올 수 잇게 함. 수정필요 시 수정하기 (참고: https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28)
	 */
	@GetMapping("/user/repos?per_page=100")
	List<GithubRepoResult> getUserRepositories(
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ repository 생성
	doc: https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#create-a-repository-for-the-authenticated-user
	 */
	@PostMapping("/user/repos")
	GithubRepoResult createUserRepository(
			@RequestBody GithubRepoCommand request,
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
	GithubBranchResult getBranch(
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
	GithubBlobResult createBlob(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@RequestBody GithubCreateBlobCommand request,
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ github tree 조회
	docs: https://docs.github.com/ko/rest/git/trees?apiVersion=2022-11-28#get-a-tree
	 */
	@GetMapping("/repos/{owner}/{repo}/git/trees/{treeSha}")
	GithubTreeResult getTree(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@PathVariable("treeSha") String treeSha,
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ 새로운 Git Tree 생성
	docs: https://docs.github.com/ko/rest/git/trees?apiVersion=2022-11-28#create-a-tree
	stackoverflow: https://stackoverflow.com/questions/23637961/how-do-i-mark-a-file-as-deleted-in-a-tree-using-the-github-api
	 */
	@PostMapping("/repos/{owner}/{repo}/git/trees")
	GithubCreateTreeResult createTree(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@RequestBody GithubCreateTreeCommand request,
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ 새로운 커밋 생성
	docs: https://docs.github.com/ko/rest/git/commits?apiVersion=2022-11-28#create-a-commit
	 */
	@PostMapping("/repos/{owner}/{repo}/git/commits")
	GithubCreateCommitResult createCommit(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@RequestBody GithubCreateCommitCommand request,
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ 브랜치 업데이트 (HEAD 이동)
	docs: https://docs.github.com/ko/rest/git/refs?apiVersion=2022-11-28#update-a-reference
	 */
	@PostMapping("/repos/{owner}/{repo}/git/refs/heads/{branch}")
	GithubUpdateReferenceResult updateBranch(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@PathVariable("branch") String branch,
			@RequestBody GithubUpdateReferenceCommand request,
			@RequestHeader("Authorization") String token
	);

	/*
		 ➡️ 이슈 생성 (멘션 포함)
		 docs: https://docs.github.com/ko/rest/issues/issues?apiVersion=2022-11-28#create-an-issue
		 */
	@PostMapping("/repos/{owner}/{repo}/issues")
	GithubIssueResult createIssue(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@RequestBody GithubCreateIssueCommand request,
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
			@RequestBody GithubAddCollaboratorsCommand request,
			@RequestHeader("Authorization") String token
	);

	/*
	➡️ 파일 업로드 (base64)
	https://docs.github.com/ko/rest/repos/contents?apiVersion=2022-11-28#create-or-update-file-contents
	 */
	@PutMapping("/repos/{owner}/{repo}/contents/{path}")
	void uploadFile(
			@PathVariable("owner") String owner,
			@PathVariable("repo") String repo,
			@PathVariable("path") String path,
			@RequestBody GithubCreateOrUpdateContentCommand request,
			@RequestHeader("Authorization") String token
	);

}
