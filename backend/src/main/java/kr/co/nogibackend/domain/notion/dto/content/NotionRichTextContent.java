package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionRichTextContent {

  private String type;
  private NotionTextContent text;
  private NotionAnnotationsContent annotations;
  private String plain_text;
  private String href;

  public static String mergeRichTexts(List<NotionRichTextContent> texts) {
    return NotionRichTextContent.mergeOrSplitRichTexts(texts, true);
  }

  public static String splitRichTexts(List<NotionRichTextContent> texts) {
    return NotionRichTextContent.mergeOrSplitRichTexts(texts, false);
  }

  /**
   * <h1>notion block에서 shift + enter 한 경우 처리</h1>
   * <ul>
   *   <li>notion block에서 shift + enter 한 경우 기본적으로 \n가 포함되어 한줄로 인식되어, \n을 제거하고 한줄로 표시</li>
   *   <li>notion block에서 shift + enter 하고 글자 굵기, 기울기 와 같이 스타일을 넣는 경우 rich_text에 각각 배열 원소로 취급됨</li>
   * </ul>
   */
  private static String mergeOrSplitRichTexts(List<NotionRichTextContent> texts, boolean merge) {
    StringBuilder strb = new StringBuilder();
    String replacement = merge ? " " : "  \n";

    for (NotionRichTextContent text : texts) {
      String str = text.getText().getContent().replaceAll("\\r?\\n", replacement);
      strb.append(str);
    }
    return strb.toString();
  }

  /**
   * <h1>notion block에서 shift + enter 한 경우 처리</h1>
   */
  public void splitContentInTextToBr() {
    this.text =
        NotionTextContent
            .builder()
            .content(this.getText().getContent().replaceAll("\\r?\\n", "<br>"))
            .build();
  }

  /**
   * <h1>텍스트에 특수기호(**, ~~) escape 처리</h1>
   */
  public void convertEscapeSymbol() {
    if (this.text == null) {
      return;
    }

    this.text =
        NotionTextContent
            .builder()
            .content(this.text.getContent().replaceAll("(?<!\\\\)\\*\\*", "\\\\*\\\\*"))
            .build();

    this.text =
        NotionTextContent
            .builder()
            .content(this.text.getContent().replaceAll("(?<!\\\\)~~", "\\\\~\\\\~"))
            .build();
  }

  /**
   * <h1>텍스트 스타일 처리</h1>
   * <ul>
   *   <li>코드블럭 먼저 설정 후 다른 스타일 설정해야 됨. 반대경우 안먹힘</li>
   * </ul>
   */
  public void convertAnnotationsContent() {
    if (this.text == null || this.annotations == null) {
      return;
    }

    if (this.annotations.isCode()) {
      NotionTextContent
          .builder()
          .content("`" + this.text.getContent() + "`")
          .build();
    }

    if (this.annotations.isBold()) {
      NotionTextContent
          .builder()
          .content("**" + this.text.getContent() + "**")
          .build();
    }

    if (this.annotations.isItalic()) {
      NotionTextContent
          .builder()
          .content("_" + this.text.getContent() + "_")
          .build();
    }

    if (this.annotations.isStrikethrough()) {
      NotionTextContent
          .builder()
          .content("~~" + this.text.getContent() + "~~")
          .build();
    }

    if (this.annotations.isUnderline()) {
      NotionTextContent
          .builder()
          .content("<ins>" + this.text.getContent() + "</ins>")
          .build();
    }
  }

  /**
   * <h1>링크 만들기</h1>
   * <ul>
   *   <li>[링크텍스트](URL)</li>
   * </ul>
   */
  public void convertLink() {
    if (this.text == null || this.text.getLink() == null) {
      return;
    }

    String link = "["
        + this.text.getContent()
        + "]("
        + this.text.getLink().getUrl()
        + ")";

    this.text =
        NotionTextContent
            .builder()
            .content(link)
            .build();
  }

}
