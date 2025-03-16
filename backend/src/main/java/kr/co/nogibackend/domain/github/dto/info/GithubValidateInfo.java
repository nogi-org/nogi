package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;

public record GithubValidateInfo(
    UserInfo user,
    GithubUserInfo githubUserInfo,
    List<GithubUserEmailInfo> githubEmails,
    List<GithubRepoInfo> githubRepositories
) {

}
