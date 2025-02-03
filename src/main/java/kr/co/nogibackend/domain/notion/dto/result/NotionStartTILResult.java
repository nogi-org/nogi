package kr.co.nogibackend.domain.notion.dto.result;

import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

public record NotionStartTILResult(
	Long userId,// 유저 ID
	String notionPageId,// 노션 페이지 ID

	String category,// 디렉터리 하위 구조
	String commitDate,// 커밋 일자
	String title,// 제목(ex 파일명.md 에서 파일명으로 사용할 값)

	String content,// markdown 파일(base64 인코딩된 파일 내용)
	List<ImageOfNotionBlock> images,// 이미지 경로 정보

	boolean isSuccess //성공, 실패
) {

	public static NotionStartTILResult of(
		Long userId
		, NotionPageInfo page
		, NotionBlockConversionInfo encoding
	) {
		return new NotionStartTILResult(
			userId
			, page.getId()
			, page.getProperties().getNogiCategory().getSelect().getName()
			, page.getProperties().getNogiCommitDate().getDate().getStart()
			, page.getProperties().getNogiTitle().getTitle().get(0).getPlain_text()
			, encoding.content()
			, encoding.images()
			, encoding.isSuccess()
		);
	}

	public record ImageOfNotionBlock(
		byte[] data,// 이미지 파일
		String fileName,// 이미지 파일명 or 고유 ID
		UriComponentsBuilder uriBuilder// 이미지 URL ( /category/image/fileName )
	) {
	}
}
