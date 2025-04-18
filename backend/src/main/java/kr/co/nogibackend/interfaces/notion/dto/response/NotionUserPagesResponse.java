package kr.co.nogibackend.interfaces.notion.dto.response;

import static kr.co.nogibackend.domain.notion.content.NotionRichTextContent.sumRichTexts;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.notion.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.info.NotionBaseInfo;
import kr.co.nogibackend.domain.notion.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.property.NotionNogiCategoryProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiCommitDateProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiCommitMessageProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionNogiStatusProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiTitleProperty;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;

public record NotionUserPagesResponse(
    List<NotionUserPagePropertyResponse> pages,
    boolean isMore,
    String nextCursor
) {

  public static NotionUserPagesResponse of(NotionBaseInfo<NotionPageInfo> notionBaseInfo) {

    return new NotionUserPagesResponse(
        NotionUserPagePropertyResponse.of(notionBaseInfo.getResults()),
        notionBaseInfo.isHas_more(),
        notionBaseInfo.getNext_cursor()
    );
  }

  public record NotionUserPagePropertyResponse(
      String object,
      String id,
      LocalDateTime createdTime,
      LocalDateTime lastEditedTime,
      boolean inTrash,
      List<String> categories,
      String commitDate,
      String status,
      String title,
      String commitMessage,
      String databaseId
  ) {

    public static List<NotionUserPagePropertyResponse> of(List<NotionPageInfo> notionPageInfos) {
      return
          notionPageInfos
              .stream()
              .map(notionPageInfo -> {

                Optional<NotionNogiProperties> propertiesOp =
                    Optional.ofNullable(notionPageInfo).map(NotionPageInfo::getProperties);

                List<String> categories =
                    propertiesOp
                        .map(NotionNogiProperties::getNogiCategory)
                        .map(NotionNogiCategoryProperty::getCategoryNames)
                        .orElse(Collections.emptyList());

                String commitDate =
                    propertiesOp
                        .map(NotionNogiProperties::getNogiCommitDate)
                        .map(NotionNogiCommitDateProperty::getStart)
                        .orElse(null);

                String status =
                    propertiesOp
                        .map(NotionNogiProperties::getNogiStatus)
                        .map(NotionNogiStatusProperty::getName)
                        .orElse(null);

                List<NotionRichTextContent> titles =
                    propertiesOp
                        .map(NotionNogiProperties::getNogiTitle)
                        .map(NotionNogiTitleProperty::getTitle)
                        .orElse(Collections.emptyList());

                List<NotionRichTextContent> commitMessages =
                    propertiesOp
                        .map(NotionNogiProperties::getNogiCommitMessage)
                        .map(NotionNogiCommitMessageProperty::getRich_text)
                        .orElse(Collections.emptyList());

                String databaseId =
                    Optional
                        .ofNullable(notionPageInfo)
                        .map(NotionPageInfo::getParent)
                        .map(NotionParentProperty::getDatabase_id)
                        .orElse(null);

                return
                    new NotionUserPagePropertyResponse(
                        notionPageInfo.getObject()
                        , notionPageInfo.getId()
                        , notionPageInfo.getCreated_time()
                        , notionPageInfo.getLast_edited_time()
                        , notionPageInfo.isIn_trash()
                        , categories
                        , commitDate
                        , status
                        , sumRichTexts(titles)
                        , sumRichTexts(commitMessages)
                        , databaseId
                    );
              })
              .toList();
    }
  }

}
