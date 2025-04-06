package kr.co.nogibackend.domain.notion.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionAnnotationsContent {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean bold;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean italic;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean strikethrough;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean underline;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean code;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String color;

}
