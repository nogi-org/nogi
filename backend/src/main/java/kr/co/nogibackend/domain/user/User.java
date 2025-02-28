package kr.co.nogibackend.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.nogibackend.domain.BaseEntity;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.StringUtils;

@Table(
    name = "tb_user"
)
@Getter
@Entity
@Audited
@Builder
@EnableJpaAuditing
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tb_user SET deleted = true, deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private Role role;
  private String notionBotToken;
  @Column(nullable = true, length = 255, unique = true)
  private String notionDatabaseId;
  private String githubAuthToken;
  private String githubRepository;
  private String githubDefaultBranch;
  private String githubEmail;
  private String githubOwner;
  @Builder.Default
  private Boolean isNotificationAllowed = true;

  public User update(UserUpdateCommand command) {
    if (StringUtils.hasText(command.getNotionBotToken())) {
      this.notionBotToken = command.getNotionBotToken();
    }
    if (StringUtils.hasText(command.getNotionDatabaseId())) {
      this.notionDatabaseId = command.getNotionDatabaseId();
    }
    if (StringUtils.hasText(command.getGithubAuthToken())) {
      this.githubAuthToken = command.getGithubAuthToken();
    }
    if (StringUtils.hasText(command.getGithubRepository())) {
      this.githubRepository = command.getGithubRepository();
    }
    if (StringUtils.hasText(command.getGithubDefaultBranch())) {
      this.githubDefaultBranch = command.getGithubDefaultBranch();
    }
    if (StringUtils.hasText(command.getGithubEmail())) {
      this.githubEmail = command.getGithubEmail();
    }
    if (StringUtils.hasText(command.getGithubOwner())) {
      this.githubOwner = command.getGithubOwner();
    }
    if (command.getIsNotificationAllowed() != null) {
      this.isNotificationAllowed = command.getIsNotificationAllowed();
    }
    return this;
  }

  public enum Role {
    NOGI_BOT, ADMIN, USER
  }
}
