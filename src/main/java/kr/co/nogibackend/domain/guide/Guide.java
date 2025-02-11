package kr.co.nogibackend.domain.guide;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
  Package Name : kr.co.nogibackend.domain
  File Name    : User
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Table(
	name = "tb_guide"
)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "LONGTEXT")
	private String image;
	
	private String content;

}
