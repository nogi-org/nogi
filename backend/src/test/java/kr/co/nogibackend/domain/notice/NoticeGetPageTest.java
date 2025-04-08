package kr.co.nogibackend.domain.notice;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class NoticeGetPageTest {

  @Autowired
  MockMvc mvc;

  @Test
  @DisplayName("공지사항 페이지 조회")
  void test1() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/notice/page"))
        .andDo(print())
        .andExpect(status().isOk());
  }

}
