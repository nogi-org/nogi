package kr.co.nogibackend.domain.notion.dto.info;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult;

public record NotionBlockConversionInfo(
    String content,
    List<CompletedPageMarkdownResult.ImageOfNotionBlock> images
) {

}
