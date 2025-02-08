package kr.co.nogibackend.domain.notion.dto.info;

import java.util.List;

import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;

public record NotionBlockConversionInfo(
	String content,
	List<NotionStartTILResult.ImageOfNotionBlock> images,
	NotionStartTILResult.StatusDetail statusDetail
) {

	public NotionBlockConversionInfo(
		String content,
		List<NotionStartTILResult.ImageOfNotionBlock> images,
		boolean isSuccess
	) {
		this(content, images, new NotionStartTILResult.StatusDetail(isSuccess));
	}

	public NotionBlockConversionInfo(
		String content,
		List<NotionStartTILResult.ImageOfNotionBlock> images,
		boolean isSuccess,
		String reason
	) {
		this(content, images, new NotionStartTILResult.StatusDetail(isSuccess, reason));
	}

}
