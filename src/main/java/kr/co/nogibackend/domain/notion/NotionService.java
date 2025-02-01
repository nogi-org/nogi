package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.interfaces.notion.NotionClient;
import kr.co.nogibackend.interfaces.notion.request.NotionRequestMaker;
import kr.co.nogibackend.interfaces.notion.response.NotionBlockResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionPageResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

	private final NotionClient notionClient;

	public void startTIL(List<User> users) {
	}

	public String startTIL(User user) {
		// 대기 상태 TIL 페이지 조회
		List<NotionPageResponse> pages = this.getPendingPages(user);

		// 페이지의 블럭 조회
		List<NotionBlockResponse> blocks = this.getBlocksOfPages(user, pages);
		System.out.println("조회된 블락 개수 : " + blocks.size());
		System.out.println("조회된 블락 : " + blocks);
		// System.out.println("요청한 페이지ID : " + page.getId() + ", 블럭의 페이지ID : " + body.getResults()
		// 	.get(0)
		// 	.getParent()
		// 	.getPage_id());
		// System.out.println("블럭 !!! ::: " + body.getResults().size());
		// System.out.println("블럭 !!! ::: " + body);

		// 블럭 ->  마크다운 가공
		// 생성 또는 수정인지 판단
		// dto 로 반환
		return null;
	}

	public void endTIL(List<User> users) {
	}

	public void endTIL(User user) {
	}

	private List<NotionBlockResponse> getBlocksOfPages(User user, List<NotionPageResponse> pages) {
		List<NotionBlockResponse> blocks = new ArrayList<>();
		for (NotionPageResponse page : pages) {
			// boolean hasMore = true;
			NotionResponse<NotionBlockResponse> body =
				notionClient
					.getBlocksFromPage(user.getNotionAuthToken(), page.getId())
					.getBody();

			// hasMore = body.isHas_more() ? true : false;
			System.out.println("has more : " + body.isHas_more());

			// while (hasMore) {
			// 	NotionResponse<NotionBlockResponse> body1 =
			// 		notionClient
			// 			.getBlocksFromPage(user.getNotionAuthToken(), page.getId())
			// 			.getBody();
			// 	blocks.addAll(body1.getResults());
			// 	hasMore = body1.isHas_more() ? true : false;
			// }
		}
		return blocks;
	}

	private NotionResponse<NotionBlockResponse> getBlocks(String authToken, String pageId) {
		try {
			return
				notionClient
					.getBlocksFromPage(authToken, pageId)
					.getBody();
		} catch (Exception error) {
			// todo: 에러 핸들링 하기(notice 역할 만들고 작업하기)
			System.out.println(error.getMessage());
			return NotionResponse.empty();
		}
	}

	private List<NotionPageResponse> getPendingPages(User user) {
		try {
			Map<String, Object> filter = NotionRequestMaker.filterStatusEq(STATUS_PENDING);
			return
				notionClient
					.getPagesFromDatabase(user.getNotionAuthToken(), user.getNotionDatabaseId(), filter)
					.getBody()
					.getResults();
		} catch (Exception error) {
			// todo: 에러 핸들링 하기(notice 역할 만들고 작업하기)
			System.out.println(error.getMessage());
			return Collections.emptyList();
		}
	}

}
