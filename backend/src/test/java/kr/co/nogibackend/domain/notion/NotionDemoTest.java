package kr.co.nogibackend.domain.notice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nogibackend.domain.notice.service.NoticeReadService;
import kr.co.nogibackend.domain.notion.service.NotionPageService;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import kr.co.nogibackend.interfaces.notice.response.NoticeRecipientsResponse;
import kr.co.nogibackend.interfaces.notice.response.NoticesResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionUserPagesResponse;
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
	private NoticeReadService noticeReadService;
	@Autowired
	private NotionPageService notionPageService;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("데모4")
	void test4() throws JsonProcessingException {
		String page = notionPageService.getPage(29L, "1d8a9cc8-1056-811f-b46c-e16cc083e7eb");
		System.out.println("pages = " + page);
	}

	@Test
	@DisplayName("데모3")
	void test3() throws JsonProcessingException {
		NotionUserPagesResponse pages = notionPageService.getPages(29L, null);
		System.out.println("pages = " + objectMapper.writeValueAsString(pages));
	}

	@Test
	@DisplayName("데모2")
	void test2() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<NoticesResponse> page = noticeReadService.getNotices(pageRequest);
		System.out.println("page content = " + page.getContent());
	}

	@Test
	@DisplayName("데모1")
	void test1() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<NoticeRecipientsResponse> recipients =
				noticeReadService.getRecipients(7L, new NoticeRecipientsRequest(false), pageRequest);
		System.out.println("recipients = " + recipients.getContent().size());
	}

}
