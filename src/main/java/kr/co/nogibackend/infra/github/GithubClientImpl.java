package kr.co.nogibackend.infra.github;

import org.springframework.stereotype.Component;

import kr.co.nogibackend.domain.github.GithubClient;
import kr.co.nogibackend.domain.github.dto.info.GithubBlobInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubBranchInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateTreeInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUpdateReferenceInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateBlobRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateCommitRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;
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
}
