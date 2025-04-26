package kr.co.nogibackend.domain.github.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GithubRepoResult(
		Long id,

		@JsonProperty("node_id")
		String nodeId,

		String name,

		@JsonProperty("full_name")
		String fullName,

		GithubOwnerInfo owner,

		@JsonProperty("private")
		Boolean privateRepo, // "private" 예약어 방지

		@JsonProperty("html_url")
		String htmlUrl,

		String description,
		Boolean fork,
		String url,

		@JsonProperty("default_branch")
		String defaultBranch,

		@JsonProperty("has_issues")
		Boolean hasIssues,

		@JsonProperty("has_projects")
		Boolean hasProjects,

		@JsonProperty("has_wiki")
		Boolean hasWiki,

		@JsonProperty("has_pages")
		Boolean hasPages,

		@JsonProperty("has_downloads")
		Boolean hasDownloads,

		Boolean archived,
		Boolean disabled,
		String visibility,

		@JsonProperty("created_at")
		String createdAt,

		@JsonProperty("updated_at")
		String updatedAt,

		@JsonProperty("pushed_at")
		String pushedAt,

		@JsonProperty("forks_count")
		Integer forksCount,

		@JsonProperty("stargazers_count")
		Integer stargazersCount,

		@JsonProperty("watchers_count")
		Integer watchersCount,

		Integer size,

		@JsonProperty("open_issues_count")
		Integer openIssuesCount,

		@JsonProperty("is_template")
		Boolean isTemplate,

		GithubLicenseInfo license,

		List<String> topics,

		GithubPermissions permissions
) {

	public record GithubOwnerInfo(
			String login,
			Long id,

			@JsonProperty("node_id")
			String nodeId,

			@JsonProperty("avatar_url")
			String avatarUrl,

			String url,

			@JsonProperty("html_url")
			String htmlUrl,

			String type,

			@JsonProperty("site_admin")
			Boolean siteAdmin
	) {

	}

	public record GithubLicenseInfo(
			String key,
			String name,
			String url,

			@JsonProperty("spdx_id")
			String spdxId,

			@JsonProperty("node_id")
			String nodeId,

			@JsonProperty("html_url")
			String htmlUrl
	) {

	}

	public record GithubPermissions(
			Boolean admin,
			Boolean push,
			Boolean pull
	) {

	}
}
