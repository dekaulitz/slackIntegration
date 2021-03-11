package com.github.dekaulitz.slackIntegration.helper;

import com.github.dekaulitz.slackIntegration.model.TaskUserModel;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TemplateHelper {

  public static List<LayoutBlock> getBlockTemplate(
      TaskUserModel taskUserModel) {
    return Blocks.asBlocks(
        Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText(
                "You have a new request:\n*Order Stuck at Paid - Rebooking Request*"))),
        Blocks.divider(),
        Blocks.section(sectionBlockBuilder -> {
          sectionBlockBuilder.fields(Arrays.asList(
              BlockCompositions.markdownText("*Type:*\nComputer or Laptop"),
              BlockCompositions
                  .markdownText("*Product Name:*\nMacbook pro 2021 13inch"),
              BlockCompositions.markdownText("*Status:*\nPending"),
              BlockCompositions.markdownText("*Assign to:*\n" + "@" + taskUserModel.getUsername()),
              BlockCompositions.markdownText("*Last Updated:*\n" + new Date().toString()),
              BlockCompositions.markdownText("*Reason:*\nBroken")
          ));
          return sectionBlockBuilder;
        })
    );
  }
}
