package kr.co.nogibackend.domain.user.dto.info;

import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.dto.result.UserSinUpOrUpdateResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginByGithubInfo {

  private Long userId;
  private User.Role role;
  private String notionPageId;
  private String accessToken;

  public static UserLoginByGithubInfo from(UserSinUpOrUpdateResult userResult, String accessToken) {
    return
        UserLoginByGithubInfo
            .builder()
            .userId(userResult.id())
            .role(userResult.role())
            .notionPageId(userResult.notionPageId())
            .accessToken(accessToken)
            .build();
  }

  /*
  사용자가 입력해야할 정보가 있는지 확인하기 위한 메서드
   */
  public boolean isRequireNotionInfo() {
    return notionPageId == null;
  }
}
