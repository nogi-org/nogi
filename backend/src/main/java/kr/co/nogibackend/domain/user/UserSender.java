package kr.co.nogibackend.domain.user;

public interface UserSender {

  void sendSignUpNotification(Long userId, String ownerName);
}
