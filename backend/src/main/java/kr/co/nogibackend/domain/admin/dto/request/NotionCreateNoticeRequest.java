package kr.co.nogibackend.domain.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty.EMOJI_TYPE;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty.PARENT_TYPE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotionCreateNoticeRequest(
    NotionParentProperty parent,
    NotionEmojiProperty icon,
    NotionNogiProperties properties,
    List<NotionBlockInfo> children
) {

  public static NotionCreateNoticeRequest ofNotice(
      String databaseId,
      String title,
      List<NotionBlockInfo> content
  ) {
    return new NotionCreateNoticeRequest(
        NotionParentProperty.buildParent(PARENT_TYPE.DATABASE, databaseId),
        NotionEmojiProperty.buildEmoji(EMOJI_TYPE.EMOJI, "\uD83D\uDCE2"),
        NotionNogiProperties.buildCreateNotice(title),
        content
    );
  }

}
