package kr.co.nogibackend.domain.github.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubAddCollaboratorsCommand(
		@JsonProperty("permission")
		String permission // 권한
) {

}
