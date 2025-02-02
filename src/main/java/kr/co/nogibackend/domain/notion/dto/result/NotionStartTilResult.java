package kr.co.nogibackend.domain.notion.dto.result;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

public record NotionStartTilResult(
	String notionPageId,// 노션 페이지 ID
	String title,// 제목(ex 파일명.md 에서 파일명으로 사용할 값)
	String content,// markdown 파일(base64 인코딩된 파일 내용)
	String category,// 디렉터리 하위 구조
	LocalDate commitDate,// 커밋 일자
	List<ImageOfNotionBlock> images// 이미지 경로 정보
) {

	public record ImageOfNotionBlock(
		String fileName,// 이미지 파일명 or 고유 ID
		UriComponentsBuilder uriBuilder// 이미지 URL ( /category/image/fileName )
	) {

	}
}
