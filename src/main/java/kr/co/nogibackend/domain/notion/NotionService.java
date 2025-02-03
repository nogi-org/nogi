package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.domain.notion.dto.command.NotionEndTilCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTilCommand;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

	private final NotionClient notionClient;

	/*
	Notion 에 대기 상태인 TIL 을 조회 후 Markdown 형식으로 가공 작업
	 */
	public List<NotionStartTILResult> startTIL(List<NotionStartTilCommand> commands) {
		return
			commands
				.stream()
				.map(this::startTIL)
				.flatMap(List::stream)
				.toList();
	}

	public List<NotionStartTILResult> startTIL(NotionStartTilCommand command) {
		// todo: command validation check 추가 (실패 시 무시 및 로그)

		// 대기 상태 TIL 페이지 조회
		List<NotionPageInfo> pages =
			this.getPendingPages(command.getNotionAuthToken(), command.getNotionDatabaseId());

		List<NotionStartTILResult> results = new ArrayList<>();
		for (NotionPageInfo page : pages) {
			// 블럭 조회
			NotionInfo<NotionBlockInfo> blocks = this.getBlocksOfPage(command.getNotionAuthToken(), page);

			// 블럭 인코딩
			NotionBlockConversionInfo encoding = this.convertMarkdown(page, blocks.getResults());

			// result 로 빌드
			NotionStartTILResult result = NotionStartTILResult.of(command.getUserId(), page, encoding);
			results.add(result);
		}
		return results;
	}

	/*
	Github 에 commit 된 TIL 을 Notion 에 완료, 실패 여부 반영
	 */
	public void endTIL(List<NotionEndTilCommand> users) {
	}

	public void endTIL(NotionEndTilCommand user) {
	}

	private NotionInfo<NotionBlockInfo> getBlocksOfPage(String notionAuthToken, NotionPageInfo page) {
		NotionInfo<NotionBlockInfo> blocks = this.getBlocks(notionAuthToken, page.getId(), null);

		// hasMore 이 true 면 next_cursor 로 다음 블럭을 가져온다.
		boolean hasMore = blocks.isHas_more();
		String nextCursor = blocks.getNext_cursor();
		System.out.println("has more : " + hasMore);

		while (hasMore) {
			NotionInfo<NotionBlockInfo> nextBlocks = this.getBlocks(notionAuthToken, page.getId(), nextCursor);
			blocks.getResults().addAll(nextBlocks.getResults());
			hasMore = nextBlocks.isHas_more();
			nextCursor = nextBlocks.getNext_cursor();
		}

		return blocks;
	}

	private NotionInfo<NotionBlockInfo> getBlocks(String authToken, String pageId, String startCursor) {
		return
			notionClient.getBlocksFromPage(authToken, pageId, startCursor);
	}

	private List<NotionPageInfo> getPendingPages(String authToken, String databaseId) {
		// todo: 페이지 가져올떄 한번에 100개 만 가져옴. 100개 이상이면 더 가져오는 로직 추가 필요
		Map<String, Object> filter = NotionRequestMaker.filterStatusEq(STATUS_PENDING);
		return
			notionClient
				.getPagesFromDatabase(authToken, databaseId, filter)
				.getResults();
	}

	/*
	todo:
	2. 이미지 처리
	 */
	private NotionBlockConversionInfo convertMarkdown(NotionPageInfo page, List<NotionBlockInfo> blocks) {
		StringBuilder markDown = new StringBuilder();
		List<NotionStartTILResult.ImageOfNotionBlock> images = new ArrayList<>();
		boolean isSuccess = true;

		for (NotionBlockInfo block : blocks) {
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
							NotionRichTextContent.mergePlainText(block.getBulleted_list_item().getRich_text(), true))
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
						.append("```" + block.getCode().getLanguage() + "\n")
						.append(NotionRichTextContent.mergePlainText(block.getCode().getRich_text(), true))
						.append("\n")
						.append("```" + "\n");
					break;

				case "divider":
					markDown
						.append("---")
						.append("\n");
					break;

				case "to_do":
					String checkBox = block.getTo_do().isChecked() ? "- [x]" : "- [ ]";
					markDown
						.append(checkBox + " ")
						.append(NotionRichTextContent.mergePlainText(block.getTo_do().getRich_text(), true))
						.append("   " + "\n");
					break;

				case "image":
					try {
						System.out.println("==================== Markdown Image Convert Start ====================");
						// 이미지 캡션
						String caption =
							block.getImage().getCaption().isEmpty()
								? "TIL_IMAGE"
								: NotionRichTextContent.mergePlainText(block.getImage().getCaption(), true);

						// 공백과 특수 문자 인코딩
						String encodedUrl =
							URLEncoder.encode(block.getImage().getFile().getUrl(), StandardCharsets.UTF_8);
						/*
						import feign.Feign;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

public interface DynamicFeign {
    @RequestLine("GET /endpoint")
    String getData();
}

@Service
public class DynamicFeignService {

    public DynamicFeign createClient(String baseUrl) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DynamicFeign.class, baseUrl);
    }

    public String fetchDataFromDynamicUrl(String dynamicUrl) {
        DynamicFeign client = createClient(dynamicUrl);
        return client.getData();
    }
}





						 */
						// 공백이나 특수 문자가 처리된 후 URI로 변환
						URI uri = new URI(encodedUrl);
						System.out.println("URI ::::::: " + uri);

						// byte[] blockImage = notionClient.getBlockImage(uri, "asfaf");
						// System.out.println("blockImage ::::::+++++++ " + blockImage);

						// UriComponentsBuilder uriComponentsBuilder =
						// 	UriComponentsBuilder.fromUriString(block.getImage().getFile().getUrl());

						// String Url 을 Url 형태로
						// 	UriComponentsBuilder uriComponentsBuilder =
						// 		UriComponentsBuilder.fromUriString(block.getImage().getFile().getUrl());
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

						// todo: 이미지 byte 로 받아와야함

						// 	image.add(
						// 		ImageOfNotionBlockDto
						// 			.builder()
						// 			.fileName(fileName)
						// 			.uriBuilder(uriComponentsBuilder)
						// 			.build()
						// 	);
					} catch (Exception error) {
						System.out.println("Notion Block Convert To Markdown Error : " + error.getMessage());
						isSuccess = false;
						new NotionBlockConversionInfo(markDown.toString(), images, isSuccess);
					}
					break;

				default:
					markDown.append("\n");
			}
		}

		System.out.println("============== Markdown ============== \n" + markDown);
		return
			new NotionBlockConversionInfo(markDown.toString(), images, isSuccess);
	}

}
