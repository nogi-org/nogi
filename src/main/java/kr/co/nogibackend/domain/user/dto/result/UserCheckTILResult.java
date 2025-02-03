package kr.co.nogibackend.domain.user.dto.result;

import kr.co.nogibackend.domain.user.NogiHistoryType;

public record UserCheckTILResult(
	Long userId,// 유저 ID
	String notionPageId,// 노션 페이지 ID
	NogiHistoryType type,// 히스토리 타입
	String newCategory,// 디렉터리 하위 구조
	String newTitle,// md 파일 제목
	String prevCategory,// 디렉터리 하위 구조
	String prevTitle// md 파일 제목
) {
	public static UserCheckTILResult of(
		Long userId,
		String notionPageId,
		NogiHistoryType type,
		String newCategory,
		String newTitle
	) {
		return new UserCheckTILResult(
			userId,
			notionPageId,
			type,
			newCategory,
			newTitle,
			null,
			null
		);
	}

	public static UserCheckTILResult of(
		Long userId,
		String notionPageId,
		NogiHistoryType type,
		String newCategory,
		String newTitle,
		String prevCategory,
		String prevTitle
	) {
		return new UserCheckTILResult(
			userId,
			notionPageId,
			type,
			newCategory,
			newTitle,
			prevCategory,
			prevTitle
		);
	}
}
