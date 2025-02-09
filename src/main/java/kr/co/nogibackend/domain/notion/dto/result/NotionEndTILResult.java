package kr.co.nogibackend.domain.notion.dto.result;

public record NotionEndTILResult(
	Long userId,// 유저 ID
	String notionAuthToken,// notion auth token
	String notionPageId,// 노션 페이지 ID
	String category,// 디렉터리 하위 구조
	String title,// 제목(ex 파일명.md 에서 파일명으로 사용할 값)
	boolean isSuccess// 노션 상태 반영한 결과(true 는 노션에 상태값 반영 됨, false 는 상태값 반영 안됨)
) {
}
