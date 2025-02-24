package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubBranchInfo(
	String name,
	Commit commit,
	Links _links,
	@JsonProperty("protected")
	boolean isProtected,
	Protection protection,
	@JsonProperty("protection_url") String protectionUrl,
	String pattern,
	@JsonProperty("required_approving_review_count") Integer requiredApprovingReviewCount
) {

	public record Commit(
		String url,
		String sha,
		@JsonProperty("node_id") String nodeId,
		@JsonProperty("html_url") String htmlUrl,
		@JsonProperty("comments_url") String commentsUrl,
		// 내부 commit 세부 데이터
		CommitData commit,
		// author와 committer는 SimpleUser (또는 빈 객체) 형태 – 필요에 따라 null 처리
		SimpleUser author,
		SimpleUser committer,
		List<Parent> parents
		// stats, files 등 선택적 필드는 필요시 추가
	) {

		public record CommitData(
			String url,
			GitUser author,
			GitUser committer,
			String message,
			@JsonProperty("comment_count") int commentCount,
			Tree tree,
			Verification verification
		) {
		}

		public record GitUser(
			String name,
			String email,
			String date
		) {
		}

		public record Tree(
			String sha,
			String url
		) {
		}

		public record Verification(
			boolean verified,
			String reason,
			// payload, signature, verified_at는 null 가능하므로 String(또는 Optional<String>)로 처리
			String payload,
			String signature,
			@JsonProperty("verified_at") String verifiedAt
		) {
		}
	}

	// GitHub 사용자 (SimpleUser) – required 필드만 포함
	public record SimpleUser(
		String name,
		String email,
		String login,
		long id,
		@JsonProperty("node_id") String nodeId,
		@JsonProperty("avatar_url") String avatarUrl,
		@JsonProperty("gravatar_id") String gravatarId,
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
		@JsonProperty("site_admin") boolean siteAdmin,
		@JsonProperty("starred_at") String starredAt,
		@JsonProperty("user_view_type") String userViewType
	) {
	}

	public record Parent(
		String sha,
		String url,
		@JsonProperty("html_url") String htmlUrl
	) {
	}

	public record Links(
		String html,
		String self
	) {
	}

	public record Protection(
		String url,
		boolean enabled,
		@JsonProperty("required_status_checks") RequiredStatusChecks requiredStatusChecks,
		@JsonProperty("enforce_admins") EnforceAdmins enforceAdmins,
		@JsonProperty("required_pull_request_reviews") RequiredPullRequestReviews requiredPullRequestReviews,
		Restrictions restrictions,
		@JsonProperty("required_linear_history") RequiredLinearHistory requiredLinearHistory,
		@JsonProperty("allow_force_pushes") AllowForcePushes allowForcePushes,
		@JsonProperty("allow_deletions") AllowDeletions allowDeletions,
		@JsonProperty("block_creations") BlockCreations blockCreations,
		@JsonProperty("required_conversation_resolution") RequiredConversationResolution requiredConversationResolution,
		String name,
		@JsonProperty("protection_url") String protectionUrl,
		@JsonProperty("required_signatures") RequiredSignatures requiredSignatures,
		@JsonProperty("lock_branch") LockBranch lockBranch,
		@JsonProperty("allow_fork_syncing") AllowForkSyncing allowForkSyncing
	) {

		public record RequiredStatusChecks(
			String url,
			@JsonProperty("enforcement_level") String enforcementLevel,
			List<String> contexts,
			List<Check> checks,
			@JsonProperty("contexts_url") String contextsUrl,
			boolean strict
		) {
			public record Check(
				String context,
				@JsonProperty("app_id") Integer appId
			) {
			}
		}

		public record EnforceAdmins(
			String url,
			boolean enabled
		) {
		}

		public record RequiredPullRequestReviews(
			String url,
			@JsonProperty("dismissal_restrictions") DismissalRestrictions dismissalRestrictions,
			@JsonProperty("bypass_pull_request_allowances") BypassPullRequestAllowances bypassPullRequestAllowances,
			@JsonProperty("dismiss_stale_reviews") boolean dismissStaleReviews,
			@JsonProperty("require_code_owner_reviews") boolean requireCodeOwnerReviews,
			@JsonProperty("required_approving_review_count") int requiredApprovingReviewCount,
			@JsonProperty("require_last_push_approval") boolean requireLastPushApproval
		) {
			public record DismissalRestrictions(
				List<SimpleUser> users,
				List<Team> teams,
				List<GitHubApp> apps,
				String url,
				@JsonProperty("users_url") String usersUrl,
				@JsonProperty("teams_url") String teamsUrl
			) {
			}

			public record BypassPullRequestAllowances(
				List<SimpleUser> users,
				List<Team> teams,
				List<GitHubApp> apps
			) {
			}
		}

		public record Restrictions(
			String url,
			@JsonProperty("users_url") String usersUrl,
			@JsonProperty("teams_url") String teamsUrl,
			@JsonProperty("apps_url") String appsUrl,
			List<SimpleUser> users,
			List<Team> teams,
			List<GitHubApp> apps
		) {
		}

		public record RequiredLinearHistory(
			boolean enabled
		) {
		}

		public record AllowForcePushes(
			boolean enabled
		) {
		}

		public record AllowDeletions(
			boolean enabled
		) {
		}

		public record BlockCreations(
			boolean enabled
		) {
		}

		public record RequiredConversationResolution(
			boolean enabled
		) {
		}

		public record RequiredSignatures(
			String url,
			boolean enabled
		) {
		}

		public record LockBranch(
			// 기본값 false (JSON 매핑시 default 처리 필요)
			boolean enabled
		) {
		}

		public record AllowForkSyncing(
			boolean enabled
		) {
		}
	}

	public record Team(
		int id,
		@JsonProperty("node_id") String nodeId,
		String url,
		@JsonProperty("html_url") String htmlUrl,
		String name,
		String slug,
		String description,
		String privacy,
		@JsonProperty("notification_setting") String notificationSetting,
		String permission,
		// 예: {"pull": true, "triage": true, "push": true, "maintain": true, "admin": true}
		Map<String, Boolean> permissions,
		@JsonProperty("members_url") String membersUrl,
		@JsonProperty("repositories_url") String repositoriesUrl,
		// parent 는 null 또는 간단한 문자열/객체로 처리
		String parent
	) {
	}

	public record GitHubApp(
		int id,
		String slug,
		@JsonProperty("node_id") String nodeId,
		@JsonProperty("client_id") String clientId,
		// owner 는 SimpleUser 또는 Enterprise로 올 수 있음 – 여기서는 Object로 처리하거나 별도 record를 정의
		Object owner,
		String name,
		String description,
		@JsonProperty("external_url") String externalUrl,
		@JsonProperty("html_url") String htmlUrl,
		@JsonProperty("created_at") String createdAt,
		@JsonProperty("updated_at") String updatedAt,
		// 권한 정보 (예시)
		Map<String, String> permissions,
		List<String> events
	) {
	}

	public record Enterprise(
		String description,
		@JsonProperty("html_url") String htmlUrl,
		@JsonProperty("website_url") String websiteUrl,
		int id,
		@JsonProperty("node_id") String nodeId,
		String name,
		String slug,
		@JsonProperty("created_at") String createdAt,
		@JsonProperty("updated_at") String updatedAt,
		@JsonProperty("avatar_url") String avatarUrl
	) {
	}
}