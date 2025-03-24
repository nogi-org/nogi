package kr.co.nogibackend.domain.notion.helper;

import static kr.co.nogibackend.response.code.NotionResponseCode.F_PROCESS_MARKDOWN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.dto.content.NotionCodeContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionHeadingContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionImageContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionListItemContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionParagraphContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowCellsContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTextContent;
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
   * todo
   * <h1>Table 변환기</h1>
   */
  public String buildTable(NotionTableContent table) {
    StringBuilder markdown = new StringBuilder();

    List<NotionTableRowContent> rows = table.getRows();

    if (rows == null || rows.isEmpty()) {
      return ""; // 테이블에 행이 없으면 빈 문자열 반환
    }

    boolean hasColumnHeader = table.isHas_column_header();

    // 1. 헤더 출력
    if (rows.get(0).getCells() != null) {
      List<List<NotionTableRowCellsContent>> headerCells = rows.get(0).getCells();
      for (List<NotionTableRowCellsContent> cellGroup : headerCells) {
        String cellText =
            cellGroup
                .stream()
                .map(NotionTableRowCellsContent::getPlain_text)
                .collect(Collectors.joining());
        markdown
            .append("|")
            .append(cellText)
            .append("  \n");
      }
      markdown.append("|\n");

      // 2. 구분선 출력
      markdown
          .append("|:---".repeat(headerCells.size()))
          .append("|\n");
    }

    // 3. 본문 출력
    int startIndex = hasColumnHeader ? 1 : 0;
    for (int i = startIndex; i < rows.size(); i++) {
      List<List<NotionTableRowCellsContent>> rowCells = rows.get(i).getCells();
      for (List<NotionTableRowCellsContent> cellGroup : rowCells) {
        String cellText = cellGroup.stream()
            .map(NotionTableRowCellsContent::getPlain_text)
            .collect(Collectors.joining());
        markdown.append("| ").append(cellText).append("  \n");
      }
      markdown.append("|\n");
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
      this.escapeSymbol(richText);
      this.buildAnnotationsContent(richText);
      this.buildLink(richText);
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
    String link = "["
        + richText.getText().getContent()
        + "]"
        + "("
        + richText.getText().getLink().getUrl()
        + ")";
    richText.getText().setContent(link);
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

}
