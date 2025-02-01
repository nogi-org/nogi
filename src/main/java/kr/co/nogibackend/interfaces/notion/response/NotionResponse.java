package kr.co.nogibackend.interfaces.notion.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionResponse<T> {

	private String object;
	private List<T> results;
	private String next_cursor;
	private boolean has_more;
	private String type;
	private Object block;

	public static <T> NotionResponse<T> empty() {
		NotionResponse<T> response = new NotionResponse<>();
		response.setResults(new ArrayList<>());
		response.setHas_more(false);
		return response;
	}

}
