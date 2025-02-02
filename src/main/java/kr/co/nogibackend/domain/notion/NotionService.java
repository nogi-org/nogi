package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.domain.notion.dto.command.NotionEndTilCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTilCommand;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTilResult;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

	private final NotionClient notionClient;

	/*
	Notion 에 대기 상태인 TIL 을 조회 후 Markdown 형식으로 가공 작업
	 */
	public List<NotionStartTilResult> startTIL(List<NotionStartTilCommand> commands) {
		return null;
	}

	public NotionStartTilResult startTIL(NotionStartTilCommand command) {
		// 대기 상태 TIL 페이지 조회
		List<NotionPageInfo> pages = this.getPendingPages(
			command.getNotionAuthToken(),
			command.getNotionDatabaseId()
		);
		System.out.println("pages ::: > " + pages.size());

		// 페이지의 블럭 조회
		// todo: result 로 반환해야될듯
		List<NotionBlockInfo> blocks = this.getBlocksOfPages(command.getNotionAuthToken(), pages);
		System.out.println("조회된 블락 개수 : " + blocks.size());

		// 블럭 -> 마크다운 가공
		this.convertMarkdownBase64(blocks);

		// result 로 반환
		return null;
	}

	/*
	Github 에 commit 된 TIL 을 Notion 에 완료, 실패 여부 반영
	 */
	public void endTIL(List<NotionEndTilCommand> users) {
	}

	public void endTIL(NotionEndTilCommand user) {
	}

	private List<NotionBlockInfo> getBlocksOfPages(String notionAuthToken, List<NotionPageInfo> pages) {
		List<NotionBlockInfo> responses = new ArrayList<>();

		for (NotionPageInfo page : pages) {
			NotionInfo<NotionBlockInfo> blocks =
				this.getBlocks(notionAuthToken, page.getId(), null);
			responses.addAll(blocks.getResults());
			System.out.println("처음 블락 가져온후 ------->>>>>> : " + responses.size());

			// hasMore 이 true 면 next_cursor 로 다음 블럭을 가져온다.
			boolean hasMore = blocks.isHas_more();
			String nextCursor = blocks.getNext_cursor();
			System.out.println("has more : " + hasMore);

			while (hasMore) {
				NotionInfo<NotionBlockInfo> nextBlocks =
					this.getBlocks(notionAuthToken, page.getId(), nextCursor);
				responses.addAll(nextBlocks.getResults());
				hasMore = nextBlocks.isHas_more();
				nextCursor = nextBlocks.getNext_cursor();
			}
		}
		return responses;
	}

	private NotionInfo<NotionBlockInfo> getBlocks(String authToken, String pageId, String startCursor) {
		return
			notionClient.getBlocksFromPage(authToken, pageId, startCursor);
	}

	private List<NotionPageInfo> getPendingPages(
		String authToken,
		String databaseId
	) {
		// todo: 페이지 가져올떄 한번에 100개 만 가져옴. 100개 이상이면 더 가져오는 로직 추가 필요
		Map<String, Object> filter = NotionRequestMaker.filterStatusEq(STATUS_PENDING);
		return
			notionClient
				.getPagesFromDatabase(authToken, databaseId, filter)
				.getResults();
	}

	private void convertMarkdownBase64(List<NotionBlockInfo> blocks) {
		StringBuilder markDown = new StringBuilder();
		// List<ImageOfNotionBlockDto> image = new ArrayList<>();

		for (int i = 0; i < blocks.size(); i++) {

			// System.out.println("Block ::: " + blocks.get(i));

			switch (blocks.get(i).getType()) {
				// todo: reich text 가 배열이라 순회하면서 만들어주기
				case "heading_1":
					System.out.println("blocks.get(i).getHeading_1() : " + blocks.get(i).getHeading_1());

					markDown
						.append("# ")
						.append(blocks.get(i).getHeading_1().getRich_text().get(0).getPlain_text())
						.append("\n");
					break;

				case "heading_2":
					markDown
						.append("## ")
						.append(blocks.get(i).getHeading_2().getRich_text().get(0).getPlain_text())
						.append("\n");
					break;

				case "heading_3":
					markDown
						.append("### ")
						.append(blocks.get(i).getHeading_3().getRich_text().get(0).getPlain_text())
						.append("\n");
					break;

				case "paragraph":
					if (blocks.get(i).getParagraph().getRich_text().size() == 0) {
						markDown
							.append("\n");
					} else {
						markDown
							.append(blocks.get(i).getParagraph().getRich_text().get(0).getPlain_text())
							.append("<br>");
					}
					break;

				case "bulleted_list_item":
					markDown
						.append("* ")
						.append(blocks.get(i).getBulleted_list_item().getRich_text().get(0).getPlain_text())
						.append("\n");
					break;

				case "numbered_list_item":
					markDown
						.append("1. ")
						.append(blocks.get(i).getNumbered_list_item().getRich_text().get(0).getPlain_text()
							+ "\n");
					break;

				case "code":
					markDown
						.append("```" + blocks.get(i).getCode().getLanguage() + "\n")
						.append(blocks.get(i).getCode().getRich_text().get(0).getPlain_text() + "\n")
						.append("```" + "\n");
					break;

				// todo: 이미지를 byte 로 받아와야함
				// case "image":
				// 	// 이미지 캡션
				// 	String caption =
				// 		blocks.get(i).getImage().getCaption().size() == 0
				// 			? "TIL_IMAGE"
				// 			: blocks.get(i).getImage().getCaption().get(0).getPlain_text();
				//
				// 	// String Url 을 Url 형태로
				// 	UriComponentsBuilder uriComponentsBuilder =
				// 		UriComponentsBuilder.fromUriString(blocks.get(i).getImage().getFile().getUrl());
				//
				// 	// 이미지 파일 이름 생성
				// 	String[] split = uriComponentsBuilder.build().getPath().split("/");
				// 	String fileName = page.convertTitle() + "_" + i + "_" + split[3];
				//
				// 	// 이후 카테고리 하위 image 디렉토리에 이미지파일 생성한다고 가정하고 경로 설정
				// 	String path = page.getTilImagePath();
				// 	markDown
				// 		.append("![" + caption + "](" + path + "image/" + fileName + ")")
				// 		.append("\n");
				//
				// 	image.add(
				// 		ImageOfNotionBlockDto
				// 			.builder()
				// 			.fileName(fileName)
				// 			.uriBuilder(uriComponentsBuilder)
				// 			.build()
				// 	);
				// 	break;

				case "divider":
					markDown
						.append("---")
						.append("\n");
					break;

				case "to_do":
					String checkBox = blocks.get(i).getTo_do().isChecked() ? "- [x]" : "- [ ]";
					markDown
						.append(checkBox + " ")
						.append(blocks.get(i).getTo_do().getRich_text().get(0).getPlain_text())
						.append("   " + "\n");
					break;

				default:
					markDown.append("\n");
			}
		}

		// return
		// 	NotionPageBlockConvertDto
		// 		.builder()
		// 		.images(image)
		// 		.content(Base64.getEncoder().encodeToString(markDown.toString().getBytes()))
		// 		.page(page)
		// 		.build();
	}

}
