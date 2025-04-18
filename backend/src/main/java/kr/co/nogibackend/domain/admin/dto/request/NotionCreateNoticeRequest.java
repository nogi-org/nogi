package kr.co.nogibackend.domain.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotionCreateNoticeRequest(
    NotionParentProperty parent,
    NotionEmojiProperty icon,
    NotionNogiProperties properties,
    List<NotionBlockInfo> children
) {

}
