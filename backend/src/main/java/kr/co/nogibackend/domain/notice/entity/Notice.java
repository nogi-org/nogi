package kr.co.nogibackend.domain.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import kr.co.nogibackend.domain.base.BaseEntity;
import kr.co.nogibackend.domain.notion.command.NotionCreateNoticeCommand;
import kr.co.nogibackend.domain.notion.content.NotionCalloutContent;
import kr.co.nogibackend.domain.notion.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.property.NotionEmojiProperty.EMOJI_TYPE;
import kr.co.nogibackend.domain.notion.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty.PARENT_TYPE;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Table(
		name = "tb_notice"
)
@Getter
@Entity
@Audited
@Builder
@EnableJpaAuditing
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tb_notice SET deleted = true, deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
public class Notice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 1000)
	private String title;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String content;

	@OneToMany(mappedBy = "notice")
	private List<NoticeUser> noticeUsers;

	public NotionCreateNoticeCommand buildNewPublishToNotion(String databaseId) {
		return new NotionCreateNoticeCommand(
				NotionParentProperty.ofParent(PARENT_TYPE.DATABASE, databaseId),
				NotionEmojiProperty.buildEmoji(EMOJI_TYPE.EMOJI, "\uD83D\uDCE2"),
				NotionNogiProperties.createNewNotice(this.title),
				this.createContentCallOutBlock()
		);
	}

	public List<NotionBlockResult> createContentCallOutBlock() {
		NotionRichTextContent linkText =
				NotionRichTextContent.buildLinkText(this.title + " \uD83D\uDC48 공지 확인하기", this.url);
		List<NotionRichTextContent> richTexts = List.of(linkText);

		NotionBlockResult notionBlockResult = new NotionBlockResult();
		notionBlockResult.setObject("block");
		notionBlockResult.setType(NotionBlockResult.CALL_OUT);
		notionBlockResult.setCallout(NotionCalloutContent.of(richTexts, "\uD83D\uDCA1"));
		return List.of(notionBlockResult);
	}

	public void updateUrl() {
		this.url = this.url + "/" + this.id;
	}

}
