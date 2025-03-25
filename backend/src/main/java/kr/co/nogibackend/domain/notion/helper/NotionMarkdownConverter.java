package kr.co.nogibackend.domain.notion.helper;

import static kr.co.nogibackend.response.code.NotionResponseCode.F_PROCESS_MARKDOWN;

import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.dto.content.NotionCodeContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionHeadingContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionImageContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionListItemContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionParagraphContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTodoContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionMarkdownConverter {

  @Value("${github.resources-base-path}")
  public String RESOURCES_BASE_PATH;

  /**
   * <h1>마크다운 변환</h1>
   */
  public NotionBlockConversionInfo convert(
      List<NotionBlockInfo> blocks
      , String githubOwner
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

          case "image":
            markdown.append(this.buildImageMarkdown(block.getImage(), githubOwner));
            images = this.buildImagesBase64(block.getImage());
            break;

          case "bulleted_list_item":
            markdown.append(this.buildListItem(block.getBulleted_list_item(), false));
            break;

          case "numbered_list_item":
            markdown.append(this.buildListItem(block.getNumbered_list_item(), true));
            break;

          case "table":
            markdown.append(this.buildTable(block.getTable()));
            break;

          default:
            markdown.append("  \n");
        }
      }
      return new NotionBlockConversionInfo(markdown.toString(), images);
    } catch (Exception error) {
      log.error("Markdown Convert Error : {}", error.getMessage());
      throw new GlobalException(F_PROCESS_MARKDOWN);
    }
  }

  /**
   * todo: 토글
   */
//  public String buildToggle() {
//    StringBuilder markdown = new StringBuilder();
//    return markdown.toString();
//  }

  /**
   * <h1>Table 변환기</h1>
   * <ul>
   *   <li>Table 데이터구조는 Test/resource/json/NotionTableBlockData.json 에서 확인 가능</li>
   *   <li>노션 cell에서 줄바꿈 + 스타일(굵기, 기울기 등) 넣으면 content가 여러개로 받아짐</li>
   * </ul>
   */
  public String buildTable(NotionTableContent table) {
    StringBuilder markdown = new StringBuilder();

    for (int i = 0; i < table.getRows().size(); i++) {

      NotionTableRowContent row = table.getRows().get(i);
      List<List<NotionRichTextContent>> cells = row.getCells();

      // 각 row 내부 cell 작업
      for (List<NotionRichTextContent> cell : cells) {
        StringBuilder sumContent = new StringBuilder();

        // content 모두 합치기, 스타일 적용
        for (NotionRichTextContent content : cell) {
          content.convertEscapeSymbol();
          content.convertAnnotationsContent();
          content.convertLink();
          // shift + enter 경우 줄바꿈 처리
          content.convertLineBreakToBrInTextContent();
          sumContent
              .append(content.getText().getContent())
              .append("<br>");
        }

        markdown
            .append("|")
            .append(sumContent);
      }

      markdown
          .append("|")
          .append("  \n");

      // header 경우 테이블 구분선 추가
      if (i == 0) {
        markdown
            .append("|:---".repeat(cells.size()))
            .append("|")
            .append("\n");
      }

    }

    return markdown.toString();
  }

  /**
   * <h1>List(글머리, 번호) 마크다운 변환기</h1>
   */
  public String buildListItem(NotionListItemContent listItem, boolean isNumberList) {
    return buildListItem(listItem, isNumberList, 0);
  }

  private String buildListItem(NotionListItemContent listItem, boolean isNumberList, int depth) {
    StringBuilder markdown = new StringBuilder();

    // depth에 따라 들여쓰기 (스페이스 3칸씩)
    String indent = "   ".repeat(depth);
    String prefix = isNumberList ? "1. " : "* ";

    for (NotionRichTextContent richText : listItem.getRich_text()) {
      richText.convertEscapeSymbol();
      richText.convertAnnotationsContent();
      richText.convertLink();
    }

    markdown
        .append(indent)
        .append(prefix)
        .append(NotionRichTextContent.mergeRichText(listItem.getRich_text()))
        .append("  \n");

    // 자식 항목 처리
    if (listItem.getChildren() != null && !listItem.getChildren().isEmpty()) {
      for (NotionListItemContent child : listItem.getChildren()) {
        markdown.append(this.buildListItem(child, isNumberList, depth + 1));
      }
    }

    return markdown.toString();
  }

  /**
   * <h1>Image 마크다운 변환기</h1>
   */
  public String buildImageMarkdown(NotionImageContent image, String githubOwner) {
    StringBuilder markDown = new StringBuilder();

    // 마크 다운에 들어갈 이미지 경로 생성
    String path = image.createImagePath(RESOURCES_BASE_PATH, githubOwner);

    // 캡션 생성
    String caption = image.createCaption();

    markDown
        .append("![")
        .append(caption)
        .append("](")
        .append(path)
        .append(")")
        .append("  \n");

    return markDown.toString();
  }

  /**
   * <h1>Image 데이터 변환기</h1>
   */
  public List<CompletedPageMarkdownResult.ImageOfNotionBlock> buildImagesBase64(
      NotionImageContent image
  ) {
    List<CompletedPageMarkdownResult.ImageOfNotionBlock> images = new ArrayList<>();
    images.add(image.buildImageOfNotionBlock());
    return images;
  }

  /**
   * <h1>TODO 변환기</h1>
   */
  public String buildTodo(NotionTodoContent todo) {
    StringBuilder markdown = new StringBuilder();
    String checkBox = todo.isChecked() ? "- [x] " : "- [ ] ";
    markdown
        .append(checkBox)
        .append(NotionRichTextContent.mergeRichText(todo.getRich_text()))
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
      richText.convertEscapeSymbol();
      richText.convertAnnotationsContent();
      richText.convertLink();
    }

    markdown
        .append(prefix)
        .append(" ")
        .append(NotionRichTextContent.mergeRichText(header.getRich_text()))
        .append("  \n");

    return markdown.toString();
  }

  /**
   * <h1>paragraph 변환기</h1>
   */
  public String buildParagraph(NotionParagraphContent paragraph) {
    StringBuilder markdown = new StringBuilder();

    // 빈 블럭 처리
    if (paragraph.getRich_text().isEmpty()) {
      markdown.append("  \n");
      return markdown.toString();
    }

    for (NotionRichTextContent richText : paragraph.getRich_text()) {
      richText.convertEscapeSymbol();
      richText.convertAnnotationsContent();
      richText.convertLink();
    }

    markdown
        .append(NotionRichTextContent.splitRichText(paragraph.getRich_text()))
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

}
