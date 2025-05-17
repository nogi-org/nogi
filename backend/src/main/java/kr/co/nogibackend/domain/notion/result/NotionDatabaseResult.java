package kr.co.nogibackend.domain.notion.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import kr.co.nogibackend.domain.notion.property.NotionDatabaseNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionDatabaseResult {

	private String object;
	private String id;
	private LocalDateTime created_time;
	private NotionParentProperty parent;
	private NotionDatabaseNogiProperties properties;

}
