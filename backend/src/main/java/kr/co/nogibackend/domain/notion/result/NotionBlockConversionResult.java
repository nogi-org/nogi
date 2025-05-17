package kr.co.nogibackend.domain.notion.result;

import java.util.List;

public record NotionBlockConversionResult(
		String content,
		List<CompletedPageMarkdownResult.ImageOfNotionBlock> images
) {

}
