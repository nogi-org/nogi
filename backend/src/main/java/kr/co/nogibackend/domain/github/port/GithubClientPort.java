package kr.co.nogibackend.domain.github.port;

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

public interface GithubClientPort {

	boolean isUniqueRepositoryName(
			String owner,
			String repoName,
			String token
	);

	List<GithubRepoResult> getUserRepositories(String token);

	GithubRepoResult createUserRepository(
			GithubRepoCommand request,
			String token
	);

	GithubUserDetailResult getUserInfo(String token);

	GithubOauthAccessTokenResult getAccessToken(
			GithubOAuthAccessTokenCommand request
	);

	GithubBranchResult getBranch(
			String owner,
			String repo,
			String branch,
			String token
	);

	GithubBlobResult createBlob(
			String owner,
			String repo,
			GithubCreateBlobCommand request,
			String token
	);

	GithubCreateTreeResult createTree(
			String owner,
			String repo,
			GithubCreateTreeCommand request,
			String token
	);

	GithubCreateCommitResult createCommit(
			String owner,
			String repo,
			GithubCreateCommitCommand request,
			String token
	);

	GithubUpdateReferenceResult updateBranch(
			String owner,
			String repo,
			String branch,
			GithubUpdateReferenceCommand request,
			String token
	);

	GithubIssueResult createIssue(
			String owner,
			String repo,
			GithubCreateIssueCommand request,
			String token
	);

	List<GithubUserEmailResult> getUserEmails(String token);

	void addCollaborator(
			String owner,
			String repo,
			String username,
			GithubAddCollaboratorsCommand request,
			String token
	);

	void uploadFile(
			String owner,
			String repo,
			String path,
			GithubCreateOrUpdateContentCommand request,
			String token
	);

	GithubRepoResult getOwnerRepositoryInfo(String s, String githubRepository, String s1);
}
