package kr.co.nogibackend.domain.notion.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionLinkContent {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String url;

}
