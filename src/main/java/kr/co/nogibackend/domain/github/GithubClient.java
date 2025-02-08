package kr.co.nogibackend.domain.github;

import kr.co.nogibackend.domain.github.dto.info.GithubBlobInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubBranchInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateCommitInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubCreateTreeInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUpdateReferenceInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateBlobRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateCommitRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;

public interface GithubClient {

	GithubBranchInfo getBranch(
		String owner,
		String repo,
		String branch,
		String token
	);

	GithubBlobInfo createBlob(
		String owner,
		String repo,
		GithubCreateBlobRequest request,
		String token
	);

	GithubCreateTreeInfo createTree(
		String owner,
		String repo,
		GithubCreateTreeRequest request,
		String token
	);

	GithubCreateCommitInfo createCommit(
		String owner,
		String repo,
		GithubCreateCommitRequest request,
		String token
	);

	GithubUpdateReferenceInfo updateBranch(
		String owner,
		String repo,
		String branch,
		GithubUpdateReferenceRequest request,
		String token
	);

}
