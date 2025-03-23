package kr.co.nogibackend.domain.notion.helper;

import static kr.co.nogibackend.response.code.NotionResponseCode.F_PROCESS_MARKDOWN;

import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.dto.content.NotionCodeContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionHeadingContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionParagraphContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTodoContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionMarkdownConverter {

	@Value("${github.resources-base-path}")
	public String RESOURCES_BASE_PATH;

	/**
	 * <h1>마크다운 변환</h1>
	 * <ul>
	 *   <li>줄바꿈 경우: 띄어쓰기 두번 + \n 으로 처리</li>
	 * </ul>
	 */
	public NotionBlockConversionInfo convert(
			List<NotionBlockInfo> blocks
			, String githubOwner // 전처리로 빼기
	) {
		StringBuilder markdown = new StringBuilder();
		List<CompletedPageMarkdownResult.ImageOfNotionBlock> images = new ArrayList<>();
		try {
			for (NotionBlockInfo block : blocks) {
				switch (block.getType()) {
					case "heading_1":
						markdown.append(this.buildHeading1(block.getHeading_1()));
						break;

					case "heading_2":
						markdown.append(this.buildHeading2(block.getHeading_2()));
						break;

					case "heading_3":
						markdown.append(this.buildHeading3(block.getHeading_3()));
						break;

					case "paragraph":
						markdown.append(this.buildParagraph(block.getParagraph()));
						break;

					case "code":
						markdown.append(this.buildCode(block.getCode()));
						break;

					case "divider":
						markdown.append("---").append("  \n");
						break;

					case "to_do":
						markdown.append(this.buildTodo(block.getTo_do()));
						break;

					// todo: 하위 자식 요소 가져와서 처리
//					case "bulleted_list_item":
//						markDown
//								.append("* ")
//								.append(
//										NotionRichTextContent.mergePlainText(
//												block.getBulleted_list_item().getRich_text(),
//												true))
//								.append("  \n");
//						break;

//					case "numbered_list_item":
//						markDown
//								.append("1. ")
//								.append(
//										NotionRichTextContent.mergePlainText(
//												block.getNumbered_list_item().getRich_text(), true)
//								)
//								.append("  \n");
//						break;

//
//					case "image":
//						// 이미지명 생성
//						String fileName = block.getImage().createFileName();
//
//						// 마크 다운에 들어갈 이미지 경로 생성
//						String path =
//								block.getImage().createImagePath(RESOURCES_BASE_PATH, githubOwner, fileName);
//
//						// 캡션 생성
//						String caption = block.getImage().createCaption();
//
//						markDown
//								.append("![")
//								.append(caption)
//								.append("](")
//								.append(path)
//								.append(")")
//								.append("  \n");
//
//						// 이미지 데이터
//						ImageOfNotionBlock image =
//								new ImageOfNotionBlock(block.getImage().getBase64(), fileName, path);
//						images.add(image);
//						break;
//
//					case "table":
//						break;

					default:
						markdown.append("  \n");
				}
			}
			return new NotionBlockConversionInfo(markdown.toString(), images);
		} catch (Exception error) {
			throw new GlobalException(F_PROCESS_MARKDOWN);
		}
	}

	/**
	 * <h1>TODO 변환기</h1>
	 */
	public String buildTodo(NotionTodoContent todo) {
		StringBuilder markdown = new StringBuilder();
		String checkBox = todo.isChecked() ? "- [x] " : "- [ ] ";
		markdown
				.append(checkBox)
				.append(this.mergeRichText(todo.getRich_text()))
				.append("  \n");
		return markdown.toString();
	}

	/**
	 * <h1>Heading 변환기</h1>
	 */
	public String buildHeading(int prefixCount, NotionHeadingContent header) {
		StringBuilder markdown = new StringBuilder();
		String prefix = this.buildHeadingPrefix(prefixCount);

		for (NotionRichTextContent richText : header.getRich_text()) {
			this.escapeSymbol(richText);
			this.buildAnnotationsContent(richText);
			this.buildLink(richText);
		}

		markdown
				.append(prefix)
				.append(" ")
				.append(this.mergeRichText(header.getRich_text()))
				.append("  \n");

		return markdown.toString();
	}

	/**
	 * <h1>paragraph 변환기</h1>
	 */
	public String buildParagraph(NotionParagraphContent paragraph) {
		StringBuilder markdown = new StringBuilder();

		if (paragraph.getRich_text().isEmpty()) {
			markdown.append("  \n");
			return markdown.toString();
		}

		for (NotionRichTextContent richText : paragraph.getRich_text()) {
			this.escapeSymbol(richText);
			this.buildAnnotationsContent(richText);
			this.buildLink(richText);
		}

		markdown
				.append(this.splitText(paragraph.getRich_text()))
				.append("  \n");
		return markdown.toString();
	}

	/**
	 * <h1>Code 변환기</h1>
	 */
	public String buildCode(NotionCodeContent code) {
		StringBuilder markdown = new StringBuilder();
		markdown
				.append("```")
				.append(code.getLanguage())
				.append("  \n");

		for (NotionRichTextContent richTest : code.getRich_text()) {
			markdown
					.append(richTest.getText().getContent())
					.append("  \n");
		}

		markdown
				.append("```")
				.append("  \n");
		return markdown.toString();
	}

	private String buildHeadingPrefix(int prefixCount) {
		return "#".repeat(Math.max(0, prefixCount));
	}

	private String buildHeading1(NotionHeadingContent header) {
		return this.buildHeading(1, header);
	}

	private String buildHeading2(NotionHeadingContent header) {
		return this.buildHeading(2, header);
	}

	private String buildHeading3(NotionHeadingContent header) {
		return this.buildHeading(3, header);
	}

	/**
	 * <h1>링크 만들기</h1>
	 * <ul>
	 *   <li>[링크텍스트](URL)</li>
	 * </ul>
	 */
	private void buildLink(NotionRichTextContent richText) {
		if (richText.emptyText() || richText.emptyLink()) {
			return;
		}
		StringBuilder link = new StringBuilder();
		link
				.append("[")
				.append(richText.getText().getContent())
				.append("]")
				.append("(")
				.append(richText.getText().getLink().getUrl())
				.append(")");
		richText.getText().setContent(link.toString());
	}

	/**
	 * <h1>notion block에서 shift + enter 한 경우 처리</h1>
	 * <ul>
	 *   <li>notion block에서 shift + enter 한 경우 기본적으로 \n가 포함되어 한줄로 인식되어, \n을 제거하고 한줄로 표시</li>
	 *   <li>notion block에서 shift + enter 하고 글자 굵기, 기울기 와 같이 스타일을 넣는 경우 rich_text에 각각 배열 원소로 취급됨</li>
	 * </ul>
	 */
	private String mergeRichText(List<NotionRichTextContent> texts) {
		StringBuilder strb = new StringBuilder();

		for (NotionRichTextContent text : texts) {
			String str =
					text.getText().getContent().replaceAll("\\r?\\n", " ");
			strb.append(str);
		}
		return strb.toString();
	}

	private String splitText(List<NotionRichTextContent> texts) {
		StringBuilder strb = new StringBuilder();

		for (NotionRichTextContent text : texts) {
			String str =
					text.getText().getContent().replaceAll("\\r?\\n", "  \n");
			strb.append(str);
		}
		return strb.toString();
	}

	private void buildAnnotationsContent(NotionRichTextContent richText) {
		if (richText.emptyText()) {
			return;
		}
		StringBuilder text = new StringBuilder();

		if (richText.getAnnotations().isBold()) {
			text.append("**").append(richText.getText().getContent()).append("**");
		}

		if (richText.getAnnotations().isItalic()) {
			text.append("_").append(richText.getText().getContent()).append("_");
		}

		if (richText.getAnnotations().isStrikethrough()) {
			text.append("~~").append(richText.getText().getContent()).append("~~");
		}

		if (richText.getAnnotations().isCode()) {
			text.append("```").append(richText.getText().getContent()).append("```");
		}

		if (text.toString().isEmpty()) {
			return;
		}

		richText.getText().setContent(text.toString());
	}


	private void escapeSymbol(NotionRichTextContent richText) {
		if (richText == null) {
			return;
		}
		NotionTextContent text = richText.getText();
		text.setContent(text.getContent().replaceAll("(?<!\\\\)\\*\\*", "\\\\*\\\\*"));
		text.setContent(text.getContent().replaceAll("(?<!\\\\)~~", "\\\\~\\\\~"));
	}

//	public String convertMarkdownByTable(List<NotionBlockInfo> rows) {
//		StringBuilder str = new StringBuilder();
//
//		// 헤더 추가 (예제: 첫 번째 행을 기준으로 생성)
//		if (!rows.isEmpty()) {
//			rows.get(0).getTable_row().getCells().forEach(cell -> {
//				str.append("| ").append(cell.getPlain_text()).append(" ");
//			});
//			str.append("|\n");
//
//			// 구분선 추가
//			rows.get(0).getTable_row().getCells().forEach(cell -> {
//				str.append("|:---");
//			});
//			str.append("|\n");
//		}
//
//		// 헤더 제외하고 본문
//		for (int i = 1; i < rows.size(); i++) {
//			rows.get(i).getTable_row().getCells().forEach(cell -> {
//				str.append("| ").append(cell.getPlain_text()).append(" ");
//			});
//			str.append("|\n");
//		}
//
//		return str.toString();
//	}


}
