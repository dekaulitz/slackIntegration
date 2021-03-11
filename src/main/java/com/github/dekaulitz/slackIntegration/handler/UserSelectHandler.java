package com.github.dekaulitz.slackIntegration.handler;


import com.github.dekaulitz.slackIntegration.helper.TemplateHelper;
import com.github.dekaulitz.slackIntegration.helper.UserProfileHandler;
import com.github.dekaulitz.slackIntegration.model.TaskUserModel;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.app_backend.interactive_components.response.ActionResponse.ActionResponseBuilder;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.profile.UsersProfileGetResponse;
import java.io.IOException;

public class UserSelectHandler {

  public static ActionResponseBuilder assignTask(ActionResponseBuilder actionResponseBuilder,
      BlockActionPayload actionPayload, ActionContext ctx) {
    try {
      final UsersProfileGetResponse userResponse = UserProfileHandler
          .getUserProfile(actionPayload, ctx);

      TaskUserModel taskUserModel = TaskUserModel.builder()
          .username("<@"+actionPayload.getActions().get(0).getSelectedUser()+">")
          .statusRequest("something")
          .build();

      ChatPostMessageResponse chatResponse = ctx.client()
          .chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
              .channel(actionPayload.getActions().get(0).getSelectedUser())
              .text("something"));
      ctx.client()
          .chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
              .channel(actionPayload.getActions().get(0).getSelectedUser())
              .text("<@"+actionPayload.getActions().get(0).getSelectedUser()+">something"));
      return actionResponseBuilder
          .replaceOriginal(true)
          .blocks(TemplateHelper.getBlockTemplate(taskUserModel));
    } catch (IOException | SlackApiException e) {
      e.printStackTrace();
    }
    return null;
  }
}
