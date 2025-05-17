package kr.co.nogibackend.infra.github.adapter;

import feign.FeignException;
import java.util.List;
import kr.co.nogibackend.domain.github.command.GithubAddCollaboratorsCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateBlobCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateCommitCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateIssueCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateOrUpdateContentCommand;
import kr.co.nogibackend.domain.github.command.GithubCreateTreeCommand;
import kr.co.nogibackend.domain.github.command.GithubOAuthAccessTokenCommand;
import kr.co.nogibackend.domain.github.command.GithubRepoCommand;
import kr.co.nogibackend.domain.github.command.GithubUpdateReferenceCommand;
import kr.co.nogibackend.domain.github.port.GithubClientPort;
import kr.co.nogibackend.domain.github.result.GithubBlobResult;
import kr.co.nogibackend.domain.github.result.GithubBranchResult;
import kr.co.nogibackend.domain.github.result.GithubCreateCommitResult;
import kr.co.nogibackend.domain.github.result.GithubCreateTreeResult;
import kr.co.nogibackend.domain.github.result.GithubIssueResult;
import kr.co.nogibackend.domain.github.result.GithubOauthAccessTokenResult;
import kr.co.nogibackend.domain.github.result.GithubRepoResult;
import kr.co.nogibackend.domain.github.result.GithubUpdateReferenceResult;
import kr.co.nogibackend.domain.github.result.GithubUserDetailResult;
import kr.co.nogibackend.domain.github.result.GithubUserEmailResult;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.global.response.code.GitResponseCode;
import kr.co.nogibackend.global.util.GithubErrorParser;
import kr.co.nogibackend.infra.github.client.GithubFeignClient;
import kr.co.nogibackend.infra.github.client.GithubLoginFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubClientPortFeignAdapter implements GithubClientPort {

	private final GithubFeignClient githubFeignClient;
	private final GithubLoginFeignClient githubLoginFeignClient;

	@Override
	public GithubOauthAccessTokenResult getAccessToken(GithubOAuthAccessTokenCommand request) {
		return githubLoginFeignClient.getAccessToken(request);
	}

	@Override
	public boolean isUniqueRepositoryName(String owner, String repoName, String token) {
		try {
			githubFeignClient.getOwnerRepositoryInfo(owner, repoName, token);
			return false;
		} catch (FeignException e) {
			// TODO GithubException 처리 공통으로 처리하도록 수정
			String detailMessage = GithubErrorParser.extractErrorMessage(e);
			if (detailMessage.contains("Not Found")) {
				return true;// 존재하지 않는 레포지토리 -> 고유한 값
			}
			throw new GlobalException(GitResponseCode.F_GIT_UNKNOWN, e.getMessage());
		} catch (Exception e) {
			throw new GlobalException(GitResponseCode.F_GIT_UNKNOWN, e.getMessage());
		}
	}

	@Override
	public List<GithubRepoResult> getUserRepositories(String token) {
		return githubFeignClient.getUserRepositories(token);
	}

	@Override
	public GithubRepoResult createUserRepository(GithubRepoCommand request, String token) {
		try {
			return githubFeignClient.createUserRepository(request, token);
		} catch (FeignException e) {
			// TODO GithubException 처리 공통으로 처리하도록 수정
			String detailMessage = GithubErrorParser.extractErrorMessage(e);
			if (detailMessage.contains("name already exists on this account")) {
				throw new GlobalException(GitResponseCode.F_DUPLICATION_REPO_NAME_GIT);
			}
			throw new GlobalException(GitResponseCode.F_GIT_UNKNOWN, e.getMessage());
		} catch (Exception e) {
			throw new GlobalException(GitResponseCode.F_GIT_UNKNOWN, e.getMessage());
		}
	}

	@Override
	public GithubUserDetailResult getUserInfo(String token) {
		return githubFeignClient.getUserInfo(token);
	}

	@Override
	public GithubBranchResult getBranch(String owner, String repo, String branch, String token) {
		return githubFeignClient.getBranch(owner, repo, branch, token);
	}

	@Override
	public GithubBlobResult createBlob(String owner, String repo, GithubCreateBlobCommand request,
			String token) {
		return githubFeignClient.createBlob(owner, repo, request, token);
	}

	@Override
	public GithubCreateTreeResult createTree(String owner, String repo,
			GithubCreateTreeCommand request,
			String token) {
		return githubFeignClient.createTree(owner, repo, request, token);
	}

	@Override
	public GithubCreateCommitResult createCommit(String owner, String repo,
			GithubCreateCommitCommand request,
			String token) {
		return githubFeignClient.createCommit(owner, repo, request, token);
	}

	@Override
	public GithubUpdateReferenceResult updateBranch(String owner, String repo, String branch,
			GithubUpdateReferenceCommand request, String token) {
		return githubFeignClient.updateBranch(owner, repo, branch, request, token);
	}

	@Override
	public GithubIssueResult createIssue(String owner, String repo, GithubCreateIssueCommand request,
			String token) {
		return githubFeignClient.createIssue(owner, repo, request, token);
	}

	@Override
	public List<GithubUserEmailResult> getUserEmails(String token) {
		return githubFeignClient.getUserEmailInfo(token);
	}

	@Override
	public void addCollaborator(
			String owner,
			String repo,
			String username,
			GithubAddCollaboratorsCommand request,
			String token
	) {
		githubFeignClient.addCollaborator(owner, repo, username, request, token);
	}

	@Override
	public void uploadFile(
			String owner,
			String repo,
			String path,
			GithubCreateOrUpdateContentCommand request,
			String token
	) {
		githubFeignClient.uploadFile(owner, repo, path, request, token);
	}

	@Override
	public GithubRepoResult getOwnerRepositoryInfo(String owner, String repoName, String token) {
		return githubFeignClient.getOwnerRepositoryInfo(owner, repoName, token);
	}
}
