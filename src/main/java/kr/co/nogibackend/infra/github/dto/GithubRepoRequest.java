package kr.co.nogibackend.infra.github.dto;

public record GithubRepoRequest(
	String name, // 저장소 이름
	Boolean auto_init // 저장소 초기화 여부
) {
}
