package kr.co.nogibackend.domain.notion.dto.info;

import java.time.LocalDateTime;

import kr.co.nogibackend.domain.notion.dto.content.NotionLastEditedByProperty;
import kr.co.nogibackend.domain.notion.dto.content.NotionParentResponse;
import kr.co.nogibackend.domain.notion.dto.property.NotionCoverProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionCreatedByProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionPageInfo {

	private String object;
	private String id;
	private LocalDateTime created_time;
	private NotionCreatedByProperty created_by;
	private LocalDateTime last_edited_time;
	private NotionLastEditedByProperty last_edited_by;
	private boolean archived;
	private boolean in_trash;
	private NotionEmojiProperty icon;
	private NotionCoverProperty cover;
	private NotionNogiProperties properties;
	private NotionParentResponse parent;
	private String url;
	private String public_url;

  /*
   Description : 페이지 제목 가져오기
   */
	//  public String getPageTitle() {
	//    return
	//        this
	//            .getProperties()
	//            .getTtt_title()
	//            .getRich_text()
	//            .get(0)
	//            .getPlain_text();
	//  }

  /*
   Description : 카테고리 가져오기
   */
	//  public String getCategory() {
	//    return
	//        this.getProperties()
	//            .getTtt_category()
	//            .getSelect()
	//            .getName();
	//  }

  /*
   Description : 서브카테고리
   */
	//  public List<NotionRichTextResponse> getSubCategories() {
	//    return
	//        this.getProperties()
	//            .getTtt_sub_categories()
	//            .getRich_text();
	//  }

  /*
   Description : 서브카테고리 경로
   */
	//  public String getSubCategory() {
	//    return
	//        this.getProperties()
	//            .getTtt_sub_categories()
	//            .getRich_text()
	//            .get(0)
	//            .getText()
	//            .getContent();
	//  }

  /*
   Description : TIL 이미지 경로
   */
	//  public String getTilImagePath() {
	//    String path = "./";
	//
	//    if (this.getSubCategories().size() > 0) {
	//      String subCategory = this.getSubCategory();
	//      String[] split1 = subCategory.split("/");
	//      int length = split1.length;
	//
	//      if (length > 0) {
	//        for (int j = 0; j < length; j++) {
	//          path += "../";
	//        }
	//      }
	//    }
	//    return path;
	//  }

  /*
   Description : GitHub에서 사용가능한 제목으로 변경
   */
	//  public String convertTitle() {
	//    return
	//        this.getPageTitle()
	//            .replaceAll("[^a-zA-Z0-9가-힣]", "_");
	//  }

}
