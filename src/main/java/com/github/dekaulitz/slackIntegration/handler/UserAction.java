package com.github.dekaulitz.slackIntegration.handler;

import com.github.dekaulitz.slackIntegration.helper.TemplateHelper;
import com.github.dekaulitz.slackIntegration.utils.SlackUtils;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.block.composition.BlockCompositions;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAction {

  public Response onApproveTask(BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    ctx.respond(actionResponseBuilder ->
        actionResponseBuilder
            .replaceOriginal(true)
            .blocks(TemplateHelper.getOnApprovedTaskTemplate(payloadRequest, new Date())));
    ctx.client().chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
        .channel(payloadRequest.getChannel().getId())
        .replyBroadcast(false)
        // create thread
        .threadTs(payloadRequest.getMessage().getTs())
        .token(ctx.getBotToken())
        .mrkdwn(true)
        .text(
            BlockCompositions.markdownText(
                SlackUtils.mentionTag(payloadRequest.getUser().getId())
                    + " approved the task please assign to user")
                .getText())
    );
    ctx.respond("You've approved the task");
    return ctx.ack();
  }

  public Response onAssignTask(
      BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    ctx.respond(actionResponseBuilder ->
        actionResponseBuilder
            .replaceOriginal(true)
            .blocks(TemplateHelper.getOnSelectedUserTaskTemplate(payloadRequest, new Date())));
    // Replaying on thread
    ctx.client().chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
        .channel(payloadRequest.getChannel().getId())
        .replyBroadcast(false)
        .threadTs(payloadRequest.getMessage().getTs())
        .token(ctx.getBotToken())
        .mrkdwn(true)
        .text(
            BlockCompositions.markdownText(
                "This task assign to " + SlackUtils
                    .mentionTag(payloadRequest.getActions().get(0).getSelectedUser()))
                .getText()));
    // Chat message to assigned user
    ctx.client()
        .chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
            .channel(payloadRequest.getActions().get(0).getSelectedUser())
            .text(
                "Hai " + SlackUtils.mentionTag(payloadRequest.getActions().get(0).getSelectedUser())
                    + " there is some task that assigned to you on" + payloadRequest.getChannel()
                    .getName())
        );
    return ctx.ack();
  }

  public Response onUpdateStatusTask(
      BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    final boolean isTaskDone = payloadRequest.getActions().get(0).getSelectedOption().getValue()
        .equalsIgnoreCase("DONE");
    if (isTaskDone) {
      ctx.respond(actionResponseBuilder ->
          actionResponseBuilder
              .replaceOriginal(true)
              .blocks(TemplateHelper.getOnTaskDoneTemplate(payloadRequest, new Date())));
    } else {
      ctx.respond(actionResponseBuilder ->
          actionResponseBuilder
              .replaceOriginal(true)
              .blocks(TemplateHelper.getOnUpdateStatusTaskTemplate(payloadRequest, new Date())));
    }
    // Replaying on Thread
    ctx.client().chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
        .channel(payloadRequest.getChannel().getId())
        .replyBroadcast(false)
        .threadTs(payloadRequest.getMessage().getTs())
        .token(ctx.getBotToken())
        .mrkdwn(true)
        .text(
            BlockCompositions.markdownText(
                SlackUtils
                    .mentionTag(payloadRequest.getUser().getId()) + " is updating status to "
                    + payloadRequest.getActions().get(0).getSelectedOption().getText().getText())
                .getText()));
    return ctx.ack();
  }

  public Response onDenyTask(BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException {
    ctx.respond(actionResponseBuilder ->
        actionResponseBuilder
            .replaceOriginal(true)
            .blocks(TemplateHelper.getOnDeclinedTaskTemplate(payloadRequest, new Date())));
    ctx.respond("You've declining the task");
    return ctx.ack();
  }
}
