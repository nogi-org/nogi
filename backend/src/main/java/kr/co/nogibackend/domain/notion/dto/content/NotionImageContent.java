package kr.co.nogibackend.domain.notion.dto.content;

import static kr.co.nogibackend.domain.notion.NotionService.RESOURCES_IMAGE_NAME;
import static kr.co.nogibackend.domain.notion.NotionService.RESOURCES_PATH_NAME;
import static kr.co.nogibackend.response.code.NotionResponseCode.F_FILE_URL_PARSING;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
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

  public URI createURL() {
    try {
      return new URI(this.file.getUrl());
    } catch (URISyntaxException error) {
      throw new GlobalException(F_FILE_URL_PARSING);
    }
  }

  // 카테고리 하위 image 디렉토리에 이미지파일 생성한다고 가정하고 경로 설정
  public String createImagePath(String fileName) {
    return RESOURCES_PATH_NAME + "/" + RESOURCES_IMAGE_NAME + "/" + fileName;
  }

  // 중복 방지를 위해 UUID 를 파일 이름 앞에 붙임
  public String createFileName() {
    return UUID.randomUUID() + "-" + this.parseFileName();
  }

  // 상대경로로 마크다운에 작성되는 파일 경로 설정(ex: ../../resources/image/demo.jpg)
  public String createMarkdownImagePath(String RelativePath, String fileName) {
    return RelativePath + RESOURCES_PATH_NAME + "/" + RESOURCES_IMAGE_NAME + "/" + fileName;
  }

  // url 에서 파일 이름 파싱해서 가져오기
  private String parseFileName() {
    String path = this.file.getUrl().substring(this.file.getUrl().lastIndexOf('/') + 1);
    return path.split("\\?")[0];
  }

}
