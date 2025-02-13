package kr.co.nogibackend.infra.user;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.nogibackend.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
