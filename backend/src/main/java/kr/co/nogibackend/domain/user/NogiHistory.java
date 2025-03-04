package kr.co.nogibackend.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.nogibackend.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Table(
    name = "tb_nogi_history"
)
@Getter
@Entity
@Builder
@Audited
@EnableJpaAuditing
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tb_nogi_history SET deleted = true, deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
public class NogiHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User user;

  @Column(nullable = false, length = 255, unique = true) // Not Null + Unique 설정
  private String notionPageId;

  @Column(nullable = false, length = 100) // Not Null 설정
  private String category;

  @Column(nullable = false, length = 255) // Not Null 설정
  private String title;

  @Lob
  @Column(nullable = false, columnDefinition = "TEXT") // Not Null 설정
  private String content;

  public void updateMarkdownInfo(String category, String title, String content) {
    this.category = category;
    this.title = title;
    this.content = content;
  }
}
