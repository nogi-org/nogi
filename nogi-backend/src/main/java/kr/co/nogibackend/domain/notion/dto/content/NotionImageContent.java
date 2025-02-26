package kr.co.nogibackend.domain.notion.dto.content;

import static kr.co.nogibackend.response.code.NotionResponseCode.F_FILE_URL_PARSING;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.dto.property.NotionFileProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionImageContent {

  private String type;
  private NotionFileProperty file;
  private List<NotionRichTextContent> caption;

  public String createCaption() {
    return this.caption.isEmpty()
        ? "TIL_IMAGE"
        : NotionRichTextContent.mergePlainText(this.caption, true);
  }

  public URI createURI() {
    try {
      return new URI(this.file.getUrl());
    } catch (URISyntaxException error) {
      throw new GlobalException(F_FILE_URL_PARSING);
    }
  }

  // 경로의 마지막 부분
  public String parseFileName(URI uri) {
    String encodedFileName = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
    return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8);
  }

  // 카테고리 하위 image 디렉토리에 이미지파일 생성한다고 가정하고 경로 설정
  public String createMarkdownPath(String category, String fileName) {
    return category + "/image/" + fileName;
  }

}
