package kr.co.nogibackend.domain.user;

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
	name = "tb_user"
)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;// github owner
	private String notionAuthToken;
	private String notionDatabaseId;
	private String githubAuthToken;
	private String githubRepository;
}
