package kr.co.nogibackend.config.security;

import java.util.Collection;
import java.util.List;
import kr.co.nogibackend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
public class Auth implements UserDetails {

  private Long userId;
  private User.Role role;

  // UsernamePasswordAuthenticationToken에 들어갈 권한리스트 빌드
  public List<GrantedAuthority> toSimpleGrantedAuthority() {
    return List.of(new SimpleGrantedAuthority(this.role.name()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

}

