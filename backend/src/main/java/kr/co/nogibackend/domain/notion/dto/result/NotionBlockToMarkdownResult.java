package kr.co.nogibackend.domain.notion.dto.result;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult.ImageOfNotionBlock;

public record NotionBlockToMarkdownResult(
    String markdown,
    List<ImageOfNotionBlock> images
) {

}
