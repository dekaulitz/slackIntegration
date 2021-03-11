package com.github.dekaulitz.slackIntegration.helper;

import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.profile.UsersProfileGetResponse;
import java.io.IOException;

public class UserProfileHandler {

  public static UsersProfileGetResponse getUserProfile(
      BlockActionPayload payloadRequest,
      ActionContext ctx) throws IOException, SlackApiException {
    return ctx.client().usersProfileGet(usersProfileGetRequestBuilder -> {
      usersProfileGetRequestBuilder
          .user(payloadRequest.getActions().get(0).getSelectedUser())
          .token(System.getenv("SLACK_BOT_TOKEN")).build();
      return usersProfileGetRequestBuilder;
    });
  }

}
