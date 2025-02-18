package kr.co.nogibackend.domain.user.dto.result;

import kr.co.nogibackend.domain.user.NogiHistoryType;

public record UserCheckTILResult(
	Long userId,// 유저 ID
	String userName,// 유저 이름 (github owner)
	String repository,// 깃허브 레포지토리
	String branch, // 브랜치 이름
	String githubEmail,// 깃허브 이메일
	String notionPageId,// 노션 페이지 ID
	String notionBotToken,// 노션 인증 토큰
	NogiHistoryType type,// 히스토리 타입
	String newCategory,// 디렉터리 하위 구조
	String newTitle,// md 파일 제목
	String prevCategory,// 디렉터리 하위 구조
	String prevTitle,// md 파일 제목
	String githubToken,// 깃허브 토큰
	boolean isSuccess// 성공 여부
) {
	public static UserCheckTILResult of(
		Long userId,
		boolean isSuccess
	) {
		return new UserCheckTILResult(
			userId,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			isSuccess
		);
	}

	public static UserCheckTILResult of(
		Long userId,
		String userName,
		String repository,
		String branch,
		String githubEmail,
		String notionPageId,
		String notionBotToken,
		NogiHistoryType type,
		String newCategory,
		String newTitle,
		String githubToken,
		boolean isSuccess
	) {
		return new UserCheckTILResult(
			userId,
			userName,
			repository,
			branch,
			githubEmail,
			notionPageId,
			notionBotToken,
			type,
			newCategory,
			newTitle,
			null,
			null,
			githubToken,
			isSuccess
		);
	}

	public static UserCheckTILResult of(
		Long userId,
		String userName,
		String repository,
		String branch,
		String githubEmail,
		String notionPageId,
		String notionBotToken,
		NogiHistoryType type,
		String newCategory,
		String newTitle,
		String prevCategory,
		String prevTitle,
		String githubToken,
		boolean isSuccess
	) {
		return new UserCheckTILResult(
			userId,
			userName,
			repository,
			branch,
			githubEmail,
			notionPageId,
			notionBotToken,
			type,
			newCategory,
			newTitle,
			prevCategory,
			prevTitle,
			githubToken,
			isSuccess
		);
	}
}
