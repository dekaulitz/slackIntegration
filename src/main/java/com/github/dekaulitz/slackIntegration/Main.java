package com.github.dekaulitz.slackIntegration;

import static com.slack.api.model.block.Blocks.actions;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.button;

import com.github.dekaulitz.slackIntegration.handler.UserSelectHandler;
import com.github.dekaulitz.slackIntegration.helper.UserProfileHandler;
import com.github.dekaulitz.slackIntegration.model.TaskUserModel;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.methods.response.users.profile.UsersProfileGetResponse;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.element.BlockElements;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import com.slack.api.model.view.Views;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {


  public static void main(String[] args) throws Exception {
    // App expects env variables (SLACK_BOT_TOKEN, SLACK_SIGNING_SECRET)
    App app = new App();
    // Home page application
    app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
      View appHomeView = Views.view(view -> view
          .type("home")
          .blocks(Blocks.asBlocks(
              section(section -> section
                  .text(markdownText(mt -> mt.text("*Welcome to your _App's Home_* :tada:")))),
              Blocks.divider(),
              section(section -> section.text(markdownText(mt -> mt.text(
                  "This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
              actions(actions -> actions
                  .elements(BlockElements.asElements(
                      button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1")
                          .actionId("button_1"))
                  ))
              )
          ))
      );
      ctx.client()
          .viewsPublish(r -> r
              .userId(payload.getEvent().getUser())
              .view(appHomeView)
          );

      return ctx.ack();
    });
    app.command("/hello", (req, ctx) -> {
      return ctx.ack(":wave: @here Hello!");
    });

    app.blockAction("users_select_on_task-action", (req, ctx) -> {
      BlockActionPayload payloadRequest = req.getPayload();
      ctx.respond(
          actionResponseBuilder -> UserSelectHandler
              .assignTask(actionResponseBuilder, payloadRequest,ctx));
      return ctx.ack();
    });

    // when a user clicks a button in the actions block
    app.blockAction("button-action", (req, ctx) -> {
      BlockActionPayload payload = req
          .getPayload();
      log.info("payload:{}", payload);
      String value = req.getPayload().getActions().get(0).getValue(); // "button's value"
      if (req.getPayload().getResponseUrl() != null) {
        // Post a message to the same channel if it's a block in a message
        ctx.respond(actionResponseBuilder -> actionResponseBuilder.deleteOriginal(true)
            .text("ok noted"));
        ctx.respond("You've sent " + value + " by clicking the button!");
      }
      return ctx.ack();
    });
    SlackAppServer server = new SlackAppServer(app);
    server.start(); // http://localhost:3030
  }
}
