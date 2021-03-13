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

  /**
   * <pre> On button click approve action</pre>
   * @param payloadRequest
   * @param ctx
   * @return
   * @throws IOException
   * @throws SlackApiException
   */
  public Response onApproveTask(BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    log.info("{}", payloadRequest);
    // rendering template with replacing the original message
    ctx.respond(actionResponseBuilder ->
        actionResponseBuilder
            .replaceOriginal(true)
            .blocks(TemplateHelper.getOnApprovedTaskTemplate(payloadRequest, new Date())));
    // post message to thread
    ctx.client().chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
        .channel(payloadRequest.getChannel().getId())
        // post message into related threads
        .threadTs(payloadRequest.getMessage().getTs())
        .token(ctx.getBotToken())
        .mrkdwn(true)
        // post message as block with markdown text
        .text(
            BlockCompositions.markdownText(
                SlackUtils.mentionTag(payloadRequest.getUser().getId())
                    + " approved the task please assign to user")
                .getText())
    );
    // add additional message after that only visible to user that doing some action
    ctx.respond("You've approved the task");
    return ctx.ack();
  }

  /**
   * <pre> On selecting user action</pre>
   * @param payloadRequest
   * @param ctx
   * @return
   * @throws IOException
   * @throws SlackApiException
   */
  public Response onAssignTask(
      BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    log.info("{}", payloadRequest);
    // rendering template and replacing the original message
    ctx.respond(actionResponseBuilder ->
        actionResponseBuilder
            .replaceOriginal(true)
            .blocks(TemplateHelper.getOnSelectedUserTaskTemplate(payloadRequest, new Date())));
    // replaying on thread
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
    // chat message to assigned user
    ctx.client()
        .chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
            .channel(payloadRequest.getActions().get(0).getSelectedUser())
            .text(
                "Hai " + SlackUtils.mentionTag(payloadRequest.getActions().get(0).getSelectedUser())
                    + " there is some task that assigned to you on " + SlackUtils
                    .channelTag(payloadRequest.getChannel()
                        .getId()))
        );
    return ctx.ack();
  }

  /**
   * <pre> On selecting update status action</pre>
   *
   * @param payloadRequest
   * @param ctx
   * @return
   * @throws IOException
   * @throws SlackApiException
   */
  public Response onUpdateStatusTask(
      BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    log.info("{}", payloadRequest);
    // get latest status from payload request from slack
    // every request from slack action it will sending the blocks as list we can get the property from there
    // as is we are getting the action and get the value from blocks
    final boolean isTaskDone = payloadRequest.getActions().get(0).getSelectedOption().getValue()
        .equalsIgnoreCase("DONE");
    if (isTaskDone) {
      // rendering task when latest status is DONE from slack
      ctx.respond(actionResponseBuilder ->
          actionResponseBuilder
              .replaceOriginal(true)
              .blocks(TemplateHelper.getOnTaskDoneTemplate(payloadRequest, new Date())));
    } else {
      // rendering default template and replacing original thread
      ctx.respond(actionResponseBuilder ->
          actionResponseBuilder
              .replaceOriginal(true)
              .blocks(TemplateHelper.getOnUpdateStatusTaskTemplate(payloadRequest, new Date())));
    }
    // replaying on Thread
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

  /**
   * <pre> on click deny button action </pre>
   * @param payloadRequest
   * @param ctx
   * @return
   * @throws IOException
   */
  public Response onDenyTask(BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException {
    log.info("{}", payloadRequest);
    // rendering template and replacing the original
    ctx.respond(actionResponseBuilder ->
        actionResponseBuilder
            .replaceOriginal(true)
            .blocks(TemplateHelper.getOnDeclinedTaskTemplate(payloadRequest, new Date())));
    // add additional message after that only visible to user that doing some action
    ctx.respond("You've declining the task");
    return ctx.ack();
  }
}
