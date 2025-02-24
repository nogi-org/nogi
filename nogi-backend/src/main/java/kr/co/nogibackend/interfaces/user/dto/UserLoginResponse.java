package kr.co.nogibackend.interfaces.user.dto;

import kr.co.nogibackend.domain.user.dto.info.UserLoginByGithubInfo;
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
public class UserLoginResponse {

	private UserResponse user;
	private String accessToken;

	public static UserLoginResponse from(UserLoginByGithubInfo userInfo) {
		return new UserLoginResponse(
			UserResponse.from(userInfo.getUserInfo()),
			userInfo.getAccessToken()
		);
	}
}
