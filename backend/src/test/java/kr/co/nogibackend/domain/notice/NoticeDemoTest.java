package kr.co.nogibackend.domain.notice;

import kr.co.nogibackend.domain.notice.service.NoticeGetService;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import kr.co.nogibackend.interfaces.notice.response.NoticeRecipientsResponse;
import kr.co.nogibackend.interfaces.notice.response.NoticesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class NoticeDemoTest {

  @Autowired
  private NoticeGetService noticeGetService;

  @Test
  @DisplayName("데모1")
  void test1() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    Page<NoticeRecipientsResponse> recipients =
        noticeGetService.getRecipients(7L, new NoticeRecipientsRequest(false), pageRequest);
    System.out.println("recipients = " + recipients.getContent().size());
  }

  @Test
  @DisplayName("데모2")
  void test2() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    Page<NoticesResponse> page = noticeGetService.getNotices(pageRequest);
    System.out.println("page content = " + page.getContent());
  }

}
