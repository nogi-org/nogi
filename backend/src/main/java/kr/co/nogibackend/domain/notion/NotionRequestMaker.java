package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.constant.NotionPropertyKey.STATUS;
import static kr.co.nogibackend.domain.notion.constant.NotionPropertyValue.STATUS_DONE;
import static kr.co.nogibackend.domain.notion.constant.NotionPropertyValue.STATUS_FAILED;

import java.util.HashMap;
import java.util.Map;
import kr.co.nogibackend.domain.notion.constant.NotionPropertyValue;

public class NotionRequestMaker {

  /*
  페이지 필터 생성
  {
    "filter": {
    "property": "Task completed",
    "checkbox": {
      "does_not_equal": true
    }
    }
  }
   */
  public static Map<String, Object> createPageFilterEqStatus(NotionPropertyValue status) {
    Map<String, Object> value =
        Map.of(
            "property", STATUS.getName()
            , "select", Map.of("equals", status.getName()));
    return filter(value);
  }

  /*
   페이지 상태값 변경 요청객체 생성
   {
      "properties": {
        "nogiStatus": {
          "select": {
            "name": "완료"
          }
        }
      }
    }
   */
  public static Map<String, Object> requestUpdateStatusOfPage(boolean isSuccess) {
    String statusName = isSuccess ? STATUS_DONE.getName() : STATUS_FAILED.getName();
    Map<String, Object> name = new HashMap<>();
    name.put("name", statusName);

    Map<String, Object> status = new HashMap<>();
    status.put("select", name);

    Map<String, Object> nogiStatus = new HashMap<>();
    nogiStatus.put(STATUS.getName(), status);

    return properties(nogiStatus);
  }

  public static Map<String, Object> filter(Map<String, Object> value) {
    return Map.of("filter", value);
  }

  public static Map<String, Object> properties(Map<String, Object> value) {
    return Map.of("properties", value);
  }

}
