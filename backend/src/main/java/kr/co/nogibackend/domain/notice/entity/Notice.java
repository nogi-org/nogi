package kr.co.nogibackend.domain.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import kr.co.nogibackend.domain.BaseEntity;
import kr.co.nogibackend.domain.admin.dto.request.NotionCreateNoticeRequest;
import kr.co.nogibackend.domain.notion.dto.content.NotionCalloutContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty.EMOJI_TYPE;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty.PARENT_TYPE;
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

  public NotionCreateNoticeRequest buildNewPublishToNotion(String databaseId) {
    return new NotionCreateNoticeRequest(
        NotionParentProperty.ofParent(PARENT_TYPE.DATABASE, databaseId),
        NotionEmojiProperty.buildEmoji(EMOJI_TYPE.EMOJI, "\uD83D\uDCE2"),
        NotionNogiProperties.createNewNotice(this.title),
        this.createContentCallOutBlock()
    );
  }

  public List<NotionBlockInfo> createContentCallOutBlock() {
    NotionRichTextContent linkText =
        NotionRichTextContent.buildLinkText(this.title + " \uD83D\uDC48 공지 확인하기", this.url);
    List<NotionRichTextContent> richTexts = List.of(linkText);

    NotionBlockInfo notionBlockInfo = new NotionBlockInfo();
    notionBlockInfo.setObject("block");
    notionBlockInfo.setType(NotionBlockInfo.CALL_OUT);
    notionBlockInfo.setCallout(NotionCalloutContent.of(richTexts, "\uD83D\uDCA1"));
    return List.of(notionBlockInfo);
  }

  public void updateUrl() {
    this.url = this.url + "/" + this.id;
  }

}
