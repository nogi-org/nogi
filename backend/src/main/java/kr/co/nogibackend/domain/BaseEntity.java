package kr.co.nogibackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Audited
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @CreatedDate
  @Column(updatable = false)
  protected LocalDateTime createdAt; // 생성일시
  @CreatedBy
  @Column(nullable = true, updatable = false, length = 100)
  protected Long createdBy; // 생성자
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @LastModifiedDate
  @Column(nullable = false)
  protected LocalDateTime modifiedAt; // 수정일시
  @LastModifiedBy
  @Column(nullable = true, length = 100)
  protected Long modifiedBy; // 수정자
  private Boolean deleted = Boolean.FALSE; // 삭제 여부
  private LocalDateTime deletedOn; // 삭제 시점

  @PreRemove
  public void markDeleted() {
    this.deleted = true;
    this.deletedOn = LocalDateTime.now();
  }
}
