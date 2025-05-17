package kr.co.nogibackend.domain.notifier.port;

public interface NotifierPort {

	// todo: command 만들어서 넘기기, 메세지 내용은 command에서 처리, 다형성 활용하기
	void send(Long userId, String ownerName);

}
