package kr.co.nogibackend.domain.github.dto.request;

import java.util.List;

public record GithubCreateIssueRequest(
	String title, // 이슈 제목 (필수)
	String body, // 이슈 내용 (선택)
	List<String> assignees // 이슈를 할당할 사용자 리스트 (선택)
) {
}