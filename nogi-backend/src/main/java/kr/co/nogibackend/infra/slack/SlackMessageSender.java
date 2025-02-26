package kr.co.nogibackend.infra.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackMessageSender {

  @Value("${slack.webhook-uri}")
  private String webhookUri;

  @Value("${slack.channel}")
  private String channel;

  @Value("${slack.username}")
  private String username;

  @Value("${slack.emoji}")
  private String emoji;

  public void sendMessage(String message) {
    try {
      var payload = Payload.builder()
          .channel(channel)
          .username(username)
          .iconEmoji(emoji)
          .text(message)
          .build();

      Slack.getInstance().send(webhookUri, payload);
    } catch (Exception e) {
      log.error("slack send error", e);
    }
  }
}