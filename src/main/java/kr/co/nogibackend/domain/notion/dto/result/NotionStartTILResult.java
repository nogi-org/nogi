package kr.co.nogibackend.domain.notion.dto.result;

import java.util.List;
import java.util.Optional;

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
	StatusDetail statusDetail// 결과정보
) {

	// custom
	public NotionStartTILResult(
		Long userId
		, NotionPageInfo page
		, NotionBlockConversionInfo encoding
	) {
		this(
			userId
			, page.getId()
			, page.getProperties().getNogiCategory().getSelect().getName()
			, page.getProperties().getNogiCommitDate().getDate().getStart()
			, page.getProperties().getNogiTitle().getTitle().get(0).getPlain_text()
			, encoding.content()
			, encoding.images()
			, encoding.statusDetail()
		);
	}

	// 내부
	public record StatusDetail(boolean isSuccess, Optional<String> reason) {

		public StatusDetail(boolean isSuccess) {
			this(isSuccess, Optional.empty());
		}

		public StatusDetail(boolean isSuccess, String reason) {
			this(isSuccess, Optional.of(reason));
		}

	}

	public record ImageOfNotionBlock(
		String fileEnc64,// 이미지 파일
		String fileName,// 이미지 파일명
		String filePath// 이미지 파일 경로
	) {
	}

}
