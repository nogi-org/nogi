package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.domain.notion.dto.command.NotionEndTILCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTILCommand;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.dto.result.NotionEndTILResult;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

	private final NotionClient notionClient;

	/*
	Notion 에 대기 상태인 TIL 을 조회 후 Markdown 형식으로 가공 작업
	 */
	public List<NotionStartTILResult> startTIL(List<NotionStartTILCommand> commands) {
		return
			commands
				.stream()
				.map(this::startTIL)
				.flatMap(List::stream)
				.toList();
	}

	public List<NotionStartTILResult> startTIL(NotionStartTILCommand command) {
		// todo: command validation check 추가 (실패 시 무시 및 로그) 고민해보자. null 값이면 어떻게 처리해줄지.

		// 대기 상태 TIL 페이지 조회
		List<NotionPageInfo> pages =
			this.getCompletedPages(command.getNotionAuthToken(), command.getNotionDatabaseId());

		List<NotionStartTILResult> results = new ArrayList<>();
		for (NotionPageInfo page : pages) {
			// 블럭 조회
			NotionInfo<NotionBlockInfo> blocks = this.getBlocksOfPage(command.getNotionAuthToken(), page);

			// 블럭 인코딩
			NotionBlockConversionInfo encodingOfBlock = this.convertMarkdown(page, blocks.getResults());

			// result 로 빌드
			results.add(new NotionStartTILResult(command.getUserId(), page, encodingOfBlock));
		}
		return results;
	}

	/*
	Github 에 commit 된 TIL 을 Notion 에 완료, 실패 여부 반영
	 */
	public List<NotionEndTILResult> endTIL(List<NotionEndTILCommand> commands) {
		return
			commands
				.stream()
				.map(this::endTIL)
				.toList();
	}

	public NotionEndTILResult endTIL(NotionEndTILCommand command) {
		boolean isUpdateResult =
			this.updateTILResultStatus(command.isSuccess(), command.notionAuthToken(), command.notionPageId());
		return
			new NotionEndTILResult(
				command.userId()
				, command.notionAuthToken()
				, command.notionPageId()
				, command.category()
				, command.title()
				, isUpdateResult
			);
	}

	private boolean updateTILResultStatus(boolean isSuccess, String authToken, String pageId) {
		try {
			Map<String, Object> request = NotionRequestMaker.requestUpdateStatusOfPage(isSuccess);
			notionClient.updatePageStatus(authToken, pageId, request);
			return true;
		} catch (Exception error) {
			return false;
		}
	}

	private NotionInfo<NotionBlockInfo> getBlocksOfPage(String notionAuthToken, NotionPageInfo page) {
		NotionInfo<NotionBlockInfo> blocks = this.getBlocks(notionAuthToken, page.getId(), null);

		// hasMore 이 true 면 next_cursor 로 다음 블럭을 가져온다.
		boolean hasMore = blocks.isHas_more();
		String nextCursor = blocks.getNext_cursor();

		while (hasMore) {
			NotionInfo<NotionBlockInfo> nextBlocks = this.getBlocks(notionAuthToken, page.getId(), nextCursor);
			blocks.getResults().addAll(nextBlocks.getResults());
			hasMore = nextBlocks.isHas_more();
			nextCursor = nextBlocks.getNext_cursor();
		}

		return blocks;
	}

	private NotionInfo<NotionBlockInfo> getBlocks(String authToken, String pageId, String startCursor) {
		return notionClient.getBlocksFromPage(authToken, pageId, startCursor);
	}

	private List<NotionPageInfo> getCompletedPages(String authToken, String databaseId) {
		// todo: 페이지 가져올떄 한번에 100개 만 가져옴. 100개 이상이면 더 가져오는 로직 추가 필요
		Map<String, Object> filter = NotionRequestMaker.filterStatusEq(STATUS_COMPLETED);
		return
			notionClient
				.getPagesFromDatabase(authToken, databaseId, filter)
				.getResults();
	}

	private NotionBlockConversionInfo convertMarkdown(NotionPageInfo page, List<NotionBlockInfo> blocks) {
		StringBuilder markDown = new StringBuilder();
		List<NotionStartTILResult.ImageOfNotionBlock> images = new ArrayList<>();

		for (NotionBlockInfo block : blocks) {
			try {
				switch (block.getType()) {
					case "heading_1":
						markDown
							.append("# ")
							.append(NotionRichTextContent.mergePlainText(block.getHeading_1().getRich_text(), true))
							.append("\n");
						break;
					case "heading_2":
						markDown
							.append("## ")
							.append(NotionRichTextContent.mergePlainText(block.getHeading_2().getRich_text(), true))
							.append("\n");
						break;

					case "heading_3":
						markDown
							.append("### ")
							.append(NotionRichTextContent.mergePlainText(block.getHeading_3().getRich_text(), true))
							.append("\n");
						break;

					case "paragraph":
						if (block.getParagraph().getRich_text().isEmpty()) {
							markDown
								.append("\n");
						} else {
							markDown
								.append(NotionRichTextContent.mergePlainText(block.getParagraph().getRich_text(), true))
								.append("<br>");
						}
						break;

					case "bulleted_list_item":
						markDown
							.append("* ")
							.append(
								NotionRichTextContent.mergePlainText(block.getBulleted_list_item().getRich_text(),
									true))
							.append("\n");
						break;

					case "numbered_list_item":
						markDown
							.append("1. ")
							.append(
								NotionRichTextContent.mergePlainText(block.getNumbered_list_item().getRich_text(), true)
							)
							.append("\n");
						break;

					case "code":
						markDown
							.append("```")
							.append(block.getCode().getLanguage())
							.append("\n")
							.append(NotionRichTextContent.mergePlainText(block.getCode().getRich_text(), true))
							.append("\n")
							.append("```\n");
						break;

					case "divider":
						markDown
							.append("---")
							.append("\n");
						break;

					case "to_do":
						String checkBox = block.getTo_do().isChecked() ? "- [x]" : "- [ ]";
						markDown
							.append(checkBox)
							.append(" ")
							.append(NotionRichTextContent.mergePlainText(block.getTo_do().getRich_text(), true))
							.append("   \n");
						break;

					case "image":
						// 이미지 요청
						URI imageUri = block.getImage().createURI();
						String imageEnc64 = this.getImageOfBlock(imageUri);

						// 이미지명 추출
						String fileName = block.getImage().parseFileName(imageUri);

						// 이미지 경로 생성
						String imagePath =
							block.getImage().createMarkdownPath(page.getProperties().getCategory(), fileName);

						// 캡션 생성
						String caption = block.getImage().createCaption();

						markDown
							.append("![")
							.append(caption)
							.append("](")
							.append(imagePath)
							.append(")")
							.append("\n");

						images.add(new NotionStartTILResult.ImageOfNotionBlock(imageEnc64, fileName, imagePath));

						break;

					default:
						markDown.append("\n");
				}
			} catch (Exception error) {
				log.error("Notion Block Convert To Markdown Error : {}", error.getMessage());
				return
					new NotionBlockConversionInfo(markDown.toString(), images, false, "Block 변환 오류");
			}
		}

		return
			new NotionBlockConversionInfo(markDown.toString(), images, true);
	}

	private String getImageOfBlock(URI uri) {
		byte[] imageByte = notionClient.getBlockImage(uri);
		return Base64.getEncoder().encodeToString(imageByte);
	}

}
