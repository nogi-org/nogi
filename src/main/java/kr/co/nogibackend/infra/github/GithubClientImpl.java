package kr.co.nogibackend.infra.github;

import java.util.List;

import org.springframework.stereotype.Component;

import feign.FeignException;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.github.GithubClient;
import kr.co.nogibackend.domain.github.dto.info.GithubBlobInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubBranchInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateTreeInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubIssueInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubOauthAccessTokenInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUpdateReferenceInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubAddCollaboratorRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateBlobRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateCommitRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateIssueRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubRepoRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;
import kr.co.nogibackend.response.code.GitResponseCode;
import kr.co.nogibackend.util.GithubErrorParser;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.infra.github
  File Name    : GithubClientImpl
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  :
 */
@Component
@RequiredArgsConstructor
public class GithubClientImpl implements GithubClient {

	private final GithubFeignClient githubFeignClient;
	private final GithubLoginFeignClient githubLoginFeignClient;

	@Override
	public GithubOauthAccessTokenInfo getAccessToken(GithubOAuthAccessTokenRequest request) {
		return githubLoginFeignClient.getAccessToken(request);
	}

	@Override
	public boolean validateRepositoryName(String owner, String repoName, String token) {
		try {
			githubFeignClient.getOwnerRepositoryInfo(owner, repoName, token);
			return false;
		} catch (FeignException e) {
			// TODO GithubException 처리 공통으로 처리하도록 수정
			String detailMessage = GithubErrorParser.extractErrorMessage(e);
			if (detailMessage.contains("Not Found")) {
				return true;// 존재하지 않는 레포지토리
			}
			throw new GlobalException(GitResponseCode.F_GIT_UNKNOWN, e.getMessage());
		} catch (Exception e) {
			throw new GlobalException(GitResponseCode.F_GIT_UNKNOWN, e.getMessage());
		}
	}

	@Override
	public GithubRepoInfo createUserRepository(GithubRepoRequest request, String token) {
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
	public GithubUserInfo getUserInfo(String token) {
		return githubFeignClient.getUserInfo(token);
	}

	@Override
	public GithubBranchInfo getBranch(String owner, String repo, String branch, String token) {
		return githubFeignClient.getBranch(owner, repo, branch, token);
	}

	@Override
	public GithubBlobInfo createBlob(String owner, String repo, GithubCreateBlobRequest request, String token) {
		return githubFeignClient.createBlob(owner, repo, request, token);
	}

	@Override
	public GithubCreateTreeInfo createTree(String owner, String repo, GithubCreateTreeRequest request,
		String token) {
		return githubFeignClient.createTree(owner, repo, request, token);
	}

	@Override
	public GithubCreateCommitInfo createCommit(String owner, String repo, GithubCreateCommitRequest request,
		String token) {
		return githubFeignClient.createCommit(owner, repo, request, token);
	}

	@Override
	public GithubUpdateReferenceInfo updateBranch(String owner, String repo, String branch,
		GithubUpdateReferenceRequest request, String token) {
		return githubFeignClient.updateBranch(owner, repo, branch, request, token);
	}

	@Override
	public GithubIssueInfo createIssue(String owner, String repo, GithubCreateIssueRequest request, String token) {
		return githubFeignClient.createIssue(owner, repo, request, token);
	}

	@Override
	public List<GithubUserEmailInfo> getUserEmails(String token) {
		return githubFeignClient.getUserEmailInfo(token);
	}

	@Override
	public void addCollaborator(
		String owner,
		String repo,
		String username,
		GithubAddCollaboratorRequest request,
		String token
	) {
		githubFeignClient.addCollaborator(owner, repo, username, request, token);
	}
}
