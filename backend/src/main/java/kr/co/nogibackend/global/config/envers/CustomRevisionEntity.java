package kr.co.nogibackend.global.config.envers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Getter
@Setter
@Entity
@RevisionEntity
@Table(name = "REVINFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomRevisionEntity implements Serializable {

	@Id
	@GeneratedValue
	@RevisionNumber
	@Column(name = "REV")
	private long id;
	@RevisionTimestamp
	@Column(name = "REVSTMP")
	private long timestamp;
	private String userId;
}
