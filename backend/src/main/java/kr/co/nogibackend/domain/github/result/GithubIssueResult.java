package kr.co.nogibackend.domain.github.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GithubIssueResult(
		SimpleUser assignee,
		@JsonProperty("closed_at") String closedAt,
		int comments,
		@JsonProperty("comments_url") String commentsUrl,
		@JsonProperty("events_url") String eventsUrl,
		@JsonProperty("html_url") String htmlUrl,
		Long id,
		@JsonProperty("node_id") String nodeId,
		List<Label> labels,
		@JsonProperty("labels_url") String labelsUrl,
		Milestone milestone,
		int number,
		@JsonProperty("repository_url") String repositoryUrl,
		String state,
		boolean locked,
		String title,
		String url,
		SimpleUser user,
		@JsonProperty("author_association") String authorAssociation,
		@JsonProperty("created_at") String createdAt,
		@JsonProperty("updated_at") String updatedAt
) {

	// GitHub에서 사용하는 Simple User 구조 (필수 필드 위주)
	public record SimpleUser(
			String login,
			Long id,
			@JsonProperty("node_id") String nodeId,
			@JsonProperty("avatar_url") String avatarUrl,
			String url,
			@JsonProperty("html_url") String htmlUrl,
			@JsonProperty("followers_url") String followersUrl,
			@JsonProperty("following_url") String followingUrl,
			@JsonProperty("gists_url") String gistsUrl,
			@JsonProperty("starred_url") String starredUrl,
			@JsonProperty("subscriptions_url") String subscriptionsUrl,
			@JsonProperty("organizations_url") String organizationsUrl,
			@JsonProperty("repos_url") String reposUrl,
			@JsonProperty("events_url") String eventsUrl,
			@JsonProperty("received_events_url") String receivedEventsUrl,
			String type,
			@JsonProperty("site_admin") boolean siteAdmin
	) {

	}

	// Label 객체 – 주의: schema에선 label은 문자열 또는 객체로 올 수 있으나,
	// 여기서는 객체 타입으로 매핑합니다.
	public record Label(
			Long id,
			@JsonProperty("node_id") String nodeId,
			String url,
			String name,
			String description,
			String color,
			@JsonProperty("default") boolean isDefault
	) {

	}

	// Milestone 객체 – nullable 값은 String 타입으로 처리 (null 허용)
	public record Milestone(
			String url,
			@JsonProperty("html_url") String htmlUrl,
			@JsonProperty("labels_url") String labelsUrl,
			int id,
			@JsonProperty("node_id") String nodeId,
			int number,
			String state,
			String title,
			String description,
			@JsonProperty("due_on") String dueOn,
			@JsonProperty("closed_at") String closedAt,
			@JsonProperty("open_issues") int openIssues,
			@JsonProperty("closed_issues") int closedIssues,
			@JsonProperty("created_at") String createdAt,
			@JsonProperty("updated_at") String updatedAt,
			SimpleUser creator
	) {

	}
}
