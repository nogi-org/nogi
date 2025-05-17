package kr.co.nogibackend.infra.notifier.adapter;

import static com.slack.api.model.block.Blocks.asBlocks;

import com.slack.api.Slack;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.webhook.Payload;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.co.nogibackend.domain.notifier.port.NotifierPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackNotifierAdapter implements NotifierPort {

	@Value("${slack.webhook-uri}")
	private String webhookUri;

	@Value("${slack.channel}")
	private String channel;

	@Value("${slack.username}")
	private String username;

	private static final String EMOJI = "🎉"; // 직접 지정한 이모지 값

	@Override
	public void send(Long userId, String ownerName) {
		String timestamp = LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		// Markdown 텍스트 객체 생성
		MarkdownTextObject text = MarkdownTextObject.builder()
				.text("*새로운 회원 가입 알림* " + EMOJI + "\n\n" +
						"*👤 유저 ID:* `" + userId + "`\n" +
						"*👑 회원명:* `" + ownerName + "`\n" +
						"*🕒 가입 시간:* `" + timestamp + "`")
				.build();

		List<LayoutBlock> blocks = asBlocks(
				SectionBlock.builder().text(text).build(), // 섹션 블록에 MarkdownTextObject 추가
				DividerBlock.builder().build() // 구분선 추가
		);

		var payload = Payload.builder()
				.channel(channel)
				.username(username)
				.iconEmoji(EMOJI)
				.blocks(blocks)
				.build();

		try {
			Slack.getInstance().send(webhookUri, payload);
		} catch (Exception e) {
			log.error("Slack 회원 가입 알림 전송 실패", e);
		}
	}
}
