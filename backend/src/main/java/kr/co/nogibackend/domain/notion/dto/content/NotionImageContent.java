package kr.co.nogibackend.domain.notion.dto.content;

import static kr.co.nogibackend.response.code.NotionResponseCode.F_FILE_URL_PARSING;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
				? "IMAGE"
				: NotionRichTextContent.mergePlainText(this.caption, true);
	}

	public URI createURL() {
		try {
			return new URI(this.file.getUrl());
		} catch (URISyntaxException error) {
			throw new GlobalException(F_FILE_URL_PARSING);
		}
	}

	// 중복 방지를 위해 UUID 를 파일 이름 앞에 붙임
	public String createFileName() {
		return UUID.randomUUID() + "-" + this.parseFileName();
	}

	// 관리자 깃허브 계정(nogi bot) 리소스 저장소에 유저별로 이미지 업로드함.
	public String createImagePath(String RESOURCES_BASE_PATH, String githubOwner, String fileName) {
		return RESOURCES_BASE_PATH + githubOwner + "/images/" + fileName;
	}

	// url 에서 파일 이름 파싱해서 가져오기
	private String parseFileName() {
		String path = this.file.getUrl().substring(this.file.getUrl().lastIndexOf('/') + 1);
		String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8); // URL 디코딩
		return decodedPath.split("\\?")[0];
	}

}
