package kr.co.nogibackend.domain.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import kr.co.nogibackend.domain.base.BaseEntity;
import kr.co.nogibackend.domain.notice.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Table(
		name = "tb_notice_user"
)
@Getter
@Entity
@Audited
@Builder
@EnableJpaAuditing
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tb_notice_user SET deleted = true, deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
public class NoticeUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notice_id")
	private Notice notice;

	@Column(nullable = false)
	private boolean isSuccess;

	public void updateIsSuccess(List<PublishNewNoticeResult> results) {
		Long userId = this.user.getId();
		results.forEach(result -> {
			if (result.user().getId().equals(userId)) {
				this.isSuccess = result.isSuccess();
			}
		});
	}

}
