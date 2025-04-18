package kr.co.nogibackend.domain.notion.info;

import java.util.List;
import kr.co.nogibackend.domain.notion.result.CompletedPageMarkdownResult;

public record NotionBlockConversionInfo(
    String content,
    List<CompletedPageMarkdownResult.ImageOfNotionBlock> images
) {

}
