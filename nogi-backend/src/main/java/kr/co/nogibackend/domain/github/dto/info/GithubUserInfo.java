package kr.co.nogibackend.domain.github.dto.info;

public record GithubUserInfo(
	String login,
	Long id,
	String node_id,
	String avatar_url,
	String gravatar_id,
	String url,
	String html_url,
	String followers_url,
	String following_url,
	String gists_url,
	String starred_url,
	String subscriptions_url,
	String organizations_url,
	String repos_url,
	String events_url,
	String received_events_url,
	String type,
	Boolean site_admin,
	String name,
	String company,
	String blog,
	String location,
	String email,
	Boolean hireable,
	String bio,
	String twitter_username,
	Integer public_repos,
	Integer public_gists,
	Integer followers,
	Integer following,
	String created_at,
	String updated_at
) {

}
