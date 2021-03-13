package com.github.dekaulitz.slackIntegration.helper;

import com.github.dekaulitz.slackIntegration.utils.SlackUtils;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.element.StaticSelectElement;
import com.slack.api.model.block.element.UsersSelectElement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TemplateHelper {

  public static List<LayoutBlock> getOnSelectedUserTaskTemplate(
      BlockActionPayload payloadRequest,
      Date lastUpdate) {
    SectionBlock approverBlock = (SectionBlock) payloadRequest.getMessage().getBlocks().get(3);
    return Blocks.asBlocks(Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText(
                "You have a new request:\n*Dekaulitz Device Replacement Request*"))),
        Blocks.divider(),
        Blocks.section(sectionBlockBuilder -> {
          sectionBlockBuilder.fields(Arrays.asList(
              BlockCompositions.markdownText("*Type:*\nComputer or Laptop"),
              BlockCompositions
                  .markdownText("*Product Name:*\nMacbook pro 2021 13inch"),
              BlockCompositions.markdownText("*Status:*\nOPEN"),
              BlockCompositions.markdownText("*Assigned to:*\n" + SlackUtils
                  .mentionTag(payloadRequest.getActions().get(0).getSelectedUser())),
              BlockCompositions.markdownText("*Reason:*\nBroken"),
              BlockCompositions.markdownText("*Last update :*\n" + lastUpdate.toString())
          ));
          return sectionBlockBuilder;
        }),
        approverBlock,
        Blocks.section(sectionBlockBuilder ->
            sectionBlockBuilder
                .text(BlockCompositions.markdownText("*Update Status Task*"))
                .accessory(StaticSelectElement.builder()
                    .placeholder(BlockCompositions.plainText("Update status task"))
                    .actionId("select_status-action")
                    .options(Arrays.asList(
                        OptionObject.builder()
                            .text(BlockCompositions.plainText("OPEN"))
                            .value("OPEN")
                            .build(),
                        OptionObject.builder()
                            .text(BlockCompositions.plainText("In Progress"))
                            .value("IN_PROGRESS")
                            .build(),
                        OptionObject.builder()
                            .text(BlockCompositions.plainText("Done"))
                            .value("DONE")
                            .build()
                    ))
                    .build()
                )
        )
    );
  }

  public static List<LayoutBlock> getOnApprovedTaskTemplate(
      BlockActionPayload payloadRequest,
      Date lastUpdate) {
    return Blocks.asBlocks(Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText(
                "You have a new request:\n*Dekaulitz Device Replacement Request*"))),
        Blocks.divider(),
        Blocks.section(sectionBlockBuilder -> {
          sectionBlockBuilder.fields(Arrays.asList(
              BlockCompositions.markdownText("*Type:*\nComputer or Laptop"),
              BlockCompositions
                  .markdownText("*Product Name:*\nMacbook pro 2021 13inch"),
              BlockCompositions.markdownText("*Last Updated:*\n" + new Date().toString()),
              BlockCompositions.markdownText("*Reason:*\nBroken"),
              BlockCompositions.markdownText("*Last update :*\n" + lastUpdate.toString())
          ));
          return sectionBlockBuilder;
        }),
        Blocks.section(sectionBlockBuilder ->
            sectionBlockBuilder.text(BlockCompositions
                .markdownText(SlackUtils.mentionTag(payloadRequest.getUser().getId())
                    + " approved this task"))),
        Blocks.section(sectionBlockBuilder ->
            sectionBlockBuilder
                .text(BlockCompositions.markdownText("*Assigning task to*"))
                .accessory(UsersSelectElement.builder()
                    .placeholder(BlockCompositions.plainText("Pick the user"))
                    .actionId("select_user-action").build())
        )
    );
  }

  public static List<LayoutBlock> getOnUpdateStatusTaskTemplate(BlockActionPayload payloadRequest,
      Date date) {
    SectionBlock selectedUserBlock = (SectionBlock) payloadRequest.getMessage().getBlocks().get(2);
    SectionBlock approverBlock = (SectionBlock) payloadRequest.getMessage().getBlocks().get(3);
    return Blocks.asBlocks(
        Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText(
                "You have a new request:\n*Dekaulitz Device Replacement Request*"))),
        Blocks.divider(),
        Blocks.section(sectionBlockBuilder -> {
          sectionBlockBuilder.fields(Arrays.asList(
              BlockCompositions.markdownText("*Type:*\nComputer or Laptop"),
              BlockCompositions
                  .markdownText("*Product Name:*\nMacbook pro 2021 13inch"),
              BlockCompositions.markdownText(
                  "*Status:*\n" + payloadRequest.getActions().get(0).getSelectedOption().getText()
                      .getText()),
              selectedUserBlock.getFields().get(3),
              BlockCompositions.markdownText("*Reason:*\nBroken"),
              BlockCompositions.markdownText("*Last update :*\n" + date.toString())
          ));
          return sectionBlockBuilder;
        }),
        approverBlock,
        Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText("*Update Status Task*"))
            .accessory(StaticSelectElement.builder()
                .placeholder(BlockCompositions.plainText("Update status task"))
                .actionId("select_status-action")
                .options(Arrays.asList(
                    OptionObject.builder()
                        .text(BlockCompositions.plainText("Open"))
                        .value("OPEN")
                        .build(),
                    OptionObject.builder()
                        .text(BlockCompositions.plainText("In Progress"))
                        .value("IN_PROGRESS")
                        .build(),
                    OptionObject.builder()
                        .text(BlockCompositions.plainText("Done"))
                        .value("DONE")
                        .build()
                ))
                .build()
            )
        )
    );
  }

  public static List<LayoutBlock> getOnTaskDoneTemplate(BlockActionPayload payloadRequest,
      Date date) {
    SectionBlock selectedUser = (SectionBlock) payloadRequest.getMessage().getBlocks().get(2);
    return Blocks.asBlocks(
        Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText(
                "You have a new request:\n*Dekaulitz Device Replacement Request*"))),
        Blocks.divider(),
        Blocks.section(sectionBlockBuilder -> {
          sectionBlockBuilder.fields(Arrays.asList(
              BlockCompositions.markdownText("*Type:*\nComputer or Laptop"),
              BlockCompositions
                  .markdownText("*Product Name:*\nMacbook pro 2021 13inch"),
              BlockCompositions.markdownText(
                  "*Status:*\n" + payloadRequest.getActions().get(0).getSelectedOption().getText()
                      .getText()),
              selectedUser.getFields().get(3),
              BlockCompositions.markdownText("*Reason:*\nBroken"),
              BlockCompositions.markdownText("*Last update :*\n" + date.toString())
          ));
          return sectionBlockBuilder;
        }),
        Blocks.section(sectionBlockBuilder ->
            sectionBlockBuilder.text(BlockCompositions
                .markdownText(SlackUtils.mentionTag(payloadRequest.getUser().getId())
                    + " approved this task")))
    );
  }

  public static List<LayoutBlock> getOnDeclinedTaskTemplate(BlockActionPayload payloadRequest,
      Date date) {
    return Blocks.asBlocks(
        Blocks.section(sectionBlockBuilder -> sectionBlockBuilder
            .text(BlockCompositions.markdownText(
                "You have a new request:\n*Dekaulitz Device Replacement Request*"))),
        Blocks.divider(),
        Blocks.section(sectionBlockBuilder -> {
          sectionBlockBuilder.fields(Arrays.asList(
              BlockCompositions.markdownText("*Type:*\nComputer or Laptop"),
              BlockCompositions
                  .markdownText("*Product Name:*\nMacbook pro 2021 13inch"),
              BlockCompositions.markdownText(
                  "*Status:*\n Declined"),
              BlockCompositions.markdownText("*Reason:*\nBroken"),
              BlockCompositions.markdownText("*Last update :*\n" + date.toString())
          ));
          return sectionBlockBuilder;
        }),
        Blocks.section(sectionBlockBuilder ->
            sectionBlockBuilder.text(BlockCompositions
                .markdownText(SlackUtils.mentionTag(payloadRequest.getUser().getId())
                    + " declining this task")))
    );
  }
}



