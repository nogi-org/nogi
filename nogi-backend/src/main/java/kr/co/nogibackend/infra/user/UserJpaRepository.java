package kr.co.nogibackend.infra.user;

import kr.co.nogibackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
