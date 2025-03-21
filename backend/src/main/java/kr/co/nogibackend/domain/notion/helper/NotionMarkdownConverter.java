package kr.co.nogibackend.domain.notion.helper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionMarkdownConverter {

  @Value("${github.resources-base-path}")
  public String RESOURCES_BASE_PATH;

  /*
  블럭 조회 후 각각의 블럭을 markdown 으로 변환
  줄바꿈 경우: 띄어쓰기 두번 + \n 으로 처리
  // todo: 리팩토링 필요 (마크다운 객체 분리, 각 변환은 메소드로 따로 빼기)
   */
  public NotionBlockConversionInfo convertMarkdown(
      List<NotionBlockInfo> blocks
      , String notionAccessToken
      , Long userId
      , String githubOwner
  ) {
    StringBuilder markDown = new StringBuilder();
    List<CompletedPageMarkdownResult.ImageOfNotionBlock> images = new ArrayList<>();

    for (NotionBlockInfo block : blocks) {
      try {
        switch (block.getType()) {
          case "heading_1":
            markDown
                .append("# ")
                .append(
                    NotionRichTextContent.mergePlainText(block.getHeading_1().getRich_text(), true))
                .append("  \n");
            break;
          case "heading_2":
            markDown
                .append("## ")
                .append(
                    NotionRichTextContent.mergePlainText(block.getHeading_2().getRich_text(), true))
                .append("  \n");
            break;

          case "heading_3":
            markDown
                .append("### ")
                .append(
                    NotionRichTextContent.mergePlainText(block.getHeading_3().getRich_text(), true))
                .append("  \n");
            break;

          case "paragraph":
            if (block.getParagraph().getRich_text().isEmpty()) {
              markDown
                  .append("  \n");
            } else {
              markDown
                  .append(NotionRichTextContent.mergePlainText(block.getParagraph().getRich_text(),
                      true))
                  .append("  \n");
            }
            break;
          case "bulleted_list_item":
            markDown
                .append("* ")
                .append(
                    NotionRichTextContent.mergePlainText(
                        block.getBulleted_list_item().getRich_text(),
                        true))
                .append("  \n");
            break;

          case "numbered_list_item":
            markDown
                .append("1. ")
                .append(
                    NotionRichTextContent.mergePlainText(
                        block.getNumbered_list_item().getRich_text(), true)
                )
                .append("  \n");
            break;

          case "code":
            markDown
                .append("```")
                .append(block.getCode().getLanguage())
                .append("  \n");

            for (NotionRichTextContent richTest : block.getCode().getRich_text()) {
              markDown.append(richTest.getPlain_text()).append("  \n");
            }

            markDown
                .append("```")
                .append("  \n");
            break;

          case "divider":
            markDown.append("---").append("  \n");
            break;

          case "to_do":
            String checkBox = block.getTo_do().isChecked() ? "- [x] " : "- [ ] ";
            markDown
                .append(checkBox)
                .append(NotionRichTextContent.mergePlainText(block.getTo_do().getRich_text(), true))
                .append("  \n");
            break;

          case "image":
            // 이미지 요청
            URI imageUri = block.getImage().createURL();
            String imageEnc64 = this.getImageOfBlock(imageUri);

            // 이미지명 생성
            String fileName = block.getImage().createFileName();

            // 마크 다운에 들어갈 이미지 경로 생성
            String imagePath =
                block.getImage().createImagePath(RESOURCES_BASE_PATH, githubOwner, fileName);

            // 캡션 생성
            String caption = block.getImage().createCaption();

            markDown
                .append("![")
                .append(caption)
                .append("](")
                .append(imagePath)
                .append(")")
                .append("  \n");

            images
                .add(new CompletedPageMarkdownResult.ImageOfNotionBlock(imageEnc64, fileName,
                    imagePath));

            break;
          case "table":
            // todo: block 호출 분리하기(요청 실패 시 무시하고 다음걸로 넘어가게)
            NotionInfo<NotionBlockInfo> rows =
                this.getBlocksOfPage(notionAccessToken, block.getId(), userId);
            markDown.append(this.convertMarkdownByTable(rows.getResults()));
            break;
          default:
            markDown.append("  \n");
        }
      } catch (Exception error) {
        ExecutionResultContext.addNotionPageErrorResult("Markdown 변환 중 문제가 발생했어요.", userId);
        return new NotionBlockConversionInfo(markDown.toString(), images);
      }
    }
    return new NotionBlockConversionInfo(markDown.toString(), images);
  }

  public String convertMarkdownByTable(List<NotionBlockInfo> rows) {
    StringBuilder str = new StringBuilder();

    // 헤더 추가 (예제: 첫 번째 행을 기준으로 생성)
    if (!rows.isEmpty()) {
      rows.get(0).getTable_row().getCells().forEach(cell -> {
        str.append("| ").append(cell.getPlain_text()).append(" ");
      });
      str.append("|\n");

      // 구분선 추가
      rows.get(0).getTable_row().getCells().forEach(cell -> {
        str.append("|:---");
      });
      str.append("|\n");
    }

    // 헤더 제외하고 본문
    for (int i = 1; i < rows.size(); i++) {
      rows.get(i).getTable_row().getCells().forEach(cell -> {
        str.append("| ").append(cell.getPlain_text()).append(" ");
      });
      str.append("|\n");
    }

    return str.toString();
  }


}
