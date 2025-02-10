package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.config.context.ExecutionResultContext;
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

/*
노션 용어정리
1. 데이터베이스: TIL 페이지를 담고있는 데이터베이스, 속성도 포함
2. 페이지: 데이터베이스가 담고 있는 여러개의 페이지, 페이지는 각각 TIL 로 구분됨
3. 블럭: 페이지에 작성된 내용, 한줄이 블럭 한개
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

	private final NotionClient notionClient;

	/*
	Notion 에 작성완료 상태인 TIL 을 조회 후 Markdown 형식으로 가공 작업
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
		// 작성완료 상태 TIL 페이지 조회
		List<NotionPageInfo> pages =
			this.getCompletedPages(command.getNotionAuthToken(), command.getNotionDatabaseId(), command.getUserId());

		List<NotionStartTILResult> results = new ArrayList<>();
		for (NotionPageInfo page : pages) {
			// 블럭 조회
			NotionInfo<NotionBlockInfo> blocks = this.getBlocksOfPage(command.getNotionAuthToken(), page);

			// 블럭 markdown 으로 변환
			NotionBlockConversionInfo encodingOfBlock = this.convertMarkdown(page, blocks.getResults());

			// result 로 빌드
			results.add(new NotionStartTILResult(command.getUserId(), page, encodingOfBlock));
		}

		return results;
	}

	/*
	Github 에 commit 된 결과를 notion 상태값 변경
	 */
	public List<NotionEndTILResult> endTIL(List<NotionEndTILCommand> commands) {
		return
			commands
				.stream()
				.map(this::endTIL)
				.flatMap(Optional::stream)
				.toList();
	}

	public Optional<NotionEndTILResult> endTIL(NotionEndTILCommand command) {
		boolean isUpdateResult =
			this.updateTILResultStatus(command.isSuccess(), command.notionAuthToken(), command.notionPageId());
		
		return
			isUpdateResult
				? Optional.of(new NotionEndTILResult(command.notionPageId()))
				: Optional.empty();
	}

	private boolean updateTILResultStatus(boolean isSuccess, String authToken, String pageId) {
		try {
			Map<String, Object> request = NotionRequestMaker.requestUpdateStatusOfPage(isSuccess);
			notionClient.updatePageStatus(authToken, pageId, request);
			return true;
		} catch (Exception error) {
			ExecutionResultContext.addNotionPageErrorResult("TIL 결과를 Notion에 반영 중 문제가 발생했어요.", pageId);
			return false;
		}
	}

	// 노션 페이지의 블럭을 모두 불러오기(1회 최대 100개만 가져올 수 있음)
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

	// 노션 페이지의 블럭 요청
	private NotionInfo<NotionBlockInfo> getBlocks(String authToken, String pageId, String startCursor) {
		try {
			return notionClient.getBlocksFromPage(authToken, pageId, startCursor);
		} catch (Exception error) {
			ExecutionResultContext.addNotionPageErrorResult("Notion Block을 불러오는 중 문제가 발생했어요.", pageId);
			return NotionInfo.empty();
		}
	}

	private List<NotionPageInfo> getCompletedPages(String authToken, String databaseId, Long userId) {
		// todo: 페이지 가져올떄 한번에 100개 만 가져옴. 100개 이상이면 더 가져오는 로직 추가 필요
		try {
			Map<String, Object> filter = NotionRequestMaker.filterStatusEq(STATUS_COMPLETED);
			return
				notionClient
					.getPagesFromDatabase(authToken, databaseId, filter)
					.getResults();
		} catch (Exception error) {
			ExecutionResultContext.addUserErrorResult("Notion Page를 불러오는 중 문제가 발생했어요.", userId);
			return List.of();
		}
	}

	// 블럭 조회 후 각각의 블럭을 markdown 으로 변환
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
				ExecutionResultContext.addNotionPageErrorResult("Markdown 변환 중 문제가 발생했어요.", page.getId());
				return new NotionBlockConversionInfo(markDown.toString(), images);
			}
		}
		return new NotionBlockConversionInfo(markDown.toString(), images);
	}

	private String getImageOfBlock(URI uri) {
		byte[] imageByte = notionClient.getBlockImage(uri);
		return Base64.getEncoder().encodeToString(imageByte);
	}

}
