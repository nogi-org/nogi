package kr.co.nogibackend.domain.notion.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotionCreateNoticeCommand(
		NotionParentProperty parent,
		NotionEmojiProperty icon,
		NotionNogiProperties properties,
		List<NotionBlockResult> children
) {

}
