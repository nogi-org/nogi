package kr.co.nogibackend.infra.user.jpa;

import kr.co.nogibackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpa extends JpaRepository<User, Long> {

}
