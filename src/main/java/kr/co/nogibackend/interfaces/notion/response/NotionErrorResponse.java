package kr.co.nogibackend.interfaces.notion.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.response
  File Name    : NotionErrorResponse
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Getter
@Setter
@ToString
public class NotionErrorResponse {
	private String object;
	private int status;
	private String code;
	private String message;
}
