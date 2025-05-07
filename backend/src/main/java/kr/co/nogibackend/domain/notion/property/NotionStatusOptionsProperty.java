package kr.co.nogibackend.domain.notion.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionStatusOptionsProperty {

	List<NotionMultiSelectProperty> options;

}
