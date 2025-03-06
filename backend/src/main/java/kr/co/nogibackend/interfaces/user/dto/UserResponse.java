package kr.co.nogibackend.interfaces.user.dto;

import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private Long id;
  private String githubRepository;
  private String githubEmail;
  private String githubOwner;
  private Boolean isConnectToNotion;// notionPageId 가 있으면 연결이 되었다고 판단
  private Boolean isNotificationAllowed;

  public static UserResponse from(UserInfo userInfo) {
    return UserResponse.builder()
        .id(userInfo.id())
        .githubRepository(userInfo.githubRepository())
        .githubEmail(userInfo.githubEmail())
        .githubOwner(userInfo.githubOwner())
        .isConnectToNotion(StringUtils.hasText(userInfo.notionPageId()))
        .isNotificationAllowed(userInfo.isNotificationAllowed())
        .build();
  }

}
