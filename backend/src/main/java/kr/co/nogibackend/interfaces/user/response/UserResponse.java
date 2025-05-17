package kr.co.nogibackend.interfaces.user.response;

import kr.co.nogibackend.domain.user.result.UserReslut;
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
	private String selfIntroduction;

	public static UserResponse from(UserReslut userReslut) {
		return UserResponse.builder()
				.id(userReslut.id())
				.githubRepository(userReslut.githubRepository())
				.githubEmail(userReslut.githubEmail())
				.githubOwner(userReslut.githubOwner())
				.isConnectToNotion(StringUtils.hasText(userReslut.notionPageId()))
				.isNotificationAllowed(userReslut.isNotificationAllowed())
				.selfIntroduction(userReslut.selfIntroduction())
				.build();
	}

}
