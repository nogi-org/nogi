package kr.co.nogibackend.domain.notion.dto.property;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
file 과 external 합침
reference:
https://developers.notion.com/reference/file-object
 */
@Getter
@Setter
@ToString
public class NotionFileProperty {

	private String url;
	private LocalDateTime expiry_time;

}
