package kr.co.nogibackend.domain.github;

import java.util.List;
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
import kr.co.nogibackend.domain.github.dto.request.GithubCreateOrUpdateContentRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateTreeRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubRepoRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateReferenceRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubUpdateRepoRequest;

public interface GithubClient {

  boolean isUniqueRepositoryName(
      String owner,
      String repoName,
      String token
  );

  GithubRepoInfo createUserRepository(
      GithubRepoRequest request,
      String token
  );

  GithubRepoInfo updateRepository(
      String owner,
      String repo,
      GithubUpdateRepoRequest request,
      String token
  );

  GithubUserInfo getUserInfo(String token);

  GithubOauthAccessTokenInfo getAccessToken(
      GithubOAuthAccessTokenRequest request
  );

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

  GithubIssueInfo createIssue(
      String owner,
      String repo,
      GithubCreateIssueRequest request,
      String token
  );

  List<GithubUserEmailInfo> getUserEmails(String token);

  void addCollaborator(
      String owner,
      String repo,
      String username,
      GithubAddCollaboratorRequest request,
      String token
  );

  void uploadFile(
      String owner,
      String repo,
      String path,
      GithubCreateOrUpdateContentRequest request,
      String token
  );
}
