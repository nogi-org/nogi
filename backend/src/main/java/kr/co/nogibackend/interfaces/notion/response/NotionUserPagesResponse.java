package kr.co.nogibackend.interfaces.notion.response;

import static kr.co.nogibackend.domain.notion.content.NotionRichTextContent.sumRichTexts;
import static kr.co.nogibackend.global.util.DateUtils.convertKoreaDateTimeFromUTC;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.notion.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.property.NotionNogiCategoryProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiCommitDateProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiCommitMessageProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionNogiStatusProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiTitleProperty;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;
import kr.co.nogibackend.domain.notion.result.NotionBaseResult;
import kr.co.nogibackend.domain.notion.result.NotionPageResult;

public record NotionUserPagesResponse(
		List<NotionUserPagePropertyResponse> pages,
		boolean isMore,
		String nextCursor
) {

	public static NotionUserPagesResponse of(NotionBaseResult<NotionPageResult> notionBaseResult) {

		return new NotionUserPagesResponse(
				NotionUserPagePropertyResponse.of(notionBaseResult.getResults()),
				notionBaseResult.isHas_more(),
				notionBaseResult.getNext_cursor()
		);
	}

	public record NotionUserPagePropertyResponse(
			String object,
			String id,
			String createdTime,
			String lastEditedTime,
			boolean inTrash,
			List<String> categories,
			String commitDate,
			String status,
			String title,
			String commitMessage,
			String databaseId
	) {

		public static List<NotionUserPagePropertyResponse> of(
				List<NotionPageResult> notionPageResults) {
			return
					notionPageResults
							.stream()
							.map(notionPageInfo -> {

								Optional<NotionNogiProperties> propertiesOp =
										Optional.ofNullable(notionPageInfo).map(NotionPageResult::getProperties);

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
												.map(NotionPageResult::getParent)
												.map(NotionParentProperty::getDatabase_id)
												.orElse(null);

								return
										new NotionUserPagePropertyResponse(
												notionPageInfo.getObject()
												, notionPageInfo.getId()
												, convertKoreaDateTimeFromUTC(notionPageInfo.getCreated_time())
												, convertKoreaDateTimeFromUTC(notionPageInfo.getLast_edited_time())
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
