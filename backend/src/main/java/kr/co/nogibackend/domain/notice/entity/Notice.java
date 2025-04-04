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
import kr.co.nogibackend.domain.notion.dto.content.NotionCalloutContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionLinkContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
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

  // todo: 메소드 불러와서 사용하기
  public List<NotionBlockInfo> buildBlock() {
    List<NotionRichTextContent> richTexts =
        List.of(
            NotionRichTextContent
                .builder()
                .type("text")
                .text(
                    NotionTextContent
                        .builder()
                        .content(this.title + " \uD83D\uDC48 공지 확인하러 가기")
                        .link(NotionLinkContent.builder().url(this.url).build())
                        .build()
                )
                .build()
        );

    return
        List.of(
            NotionBlockInfo
                .builder()
                .object("block")
                .type(NotionBlockInfo.CALL_OUT)
                .callout(
                    NotionCalloutContent
                        .builder()
                        .icon(NotionEmojiProperty.builder().type("emoji").emoji("\uD83D\uDCA1")
                            .build())
                        .rich_text(richTexts)
                        .build()
                )
                .build()
        );
  }

}
