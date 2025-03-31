package kr.co.nogibackend.domain.admin.dto.request;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.property.NotionDateProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty.EMOJI_TYPE;
import kr.co.nogibackend.domain.notion.dto.property.NotionMultiSelectProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiCategoryProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiCommitDateProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiStatusProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiTitleProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty.PARENT_TYPE;
import kr.co.nogibackend.domain.notion.dto.property.NotionStatusProperty;
import kr.co.nogibackend.util.DateUtils;

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
        buildProperties(title),
        content
    );
  }

  // todo: 리팩토링 필요
  private NotionNogiProperties buildProperties(String title) {
    return
        NotionNogiProperties
            .builder()
            .nogiTitle(
                NotionNogiTitleProperty
                    .builder()
                    .title(
                        List.of(
                            NotionRichTextContent
                                .builder()
                                .text(
                                    NotionTextContent
                                        .builder()
                                        .content(title)
                                        .build()
                                )
                                .build()
                        )
                    )
                    .build()
            )
            .nogiStatus(
                NotionNogiStatusProperty
                    .builder()
                    .select(
                        NotionStatusProperty
                            .builder()
                            .name("운영")
                            .color("red")
                            .build()
                    )
                    .build()
            )
            .nogiCategory(
                NotionNogiCategoryProperty
                    .builder()
                    .multi_select(
                        List.of(
                            NotionMultiSelectProperty
                                .builder()
                                .name("공지")
                                .color("blue")
                                .build()
                        )
                    )
                    .build()
            )
            .nogiCommitDate(
                NotionNogiCommitDateProperty
                    .builder()
                    .date(new NotionDateProperty(DateUtils.getTodayDateAsYYYYMMDDString()))
                    .build()
            )
            .build();
  }

  private NotionEmojiProperty buildIcon() {
    return
        NotionEmojiProperty
            .builder()
            .type("emoji")
            .emoji("\uD83D\uDCE2")
            .build();
  }

}
