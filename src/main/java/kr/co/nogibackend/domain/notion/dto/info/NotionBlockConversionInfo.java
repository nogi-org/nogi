package kr.co.nogibackend.domain.notion.dto.info;

import java.util.List;

import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;

public record NotionBlockConversionInfo(
	String content,
	List<NotionStartTILResult.ImageOfNotionBlock> images,
	boolean isSuccess
) {
}
