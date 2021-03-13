package com.github.dekaulitz.slackIntegration;

import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

import com.github.dekaulitz.slackIntegration.handler.UserAction;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import com.slack.api.model.view.Views;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {


  public static void main(String[] args) throws Exception {
    UserAction userAction = new UserAction();
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
                  "This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on "
                      + "<https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>."))))
          ))
      );
      ctx.client()
          .viewsPublish(r -> r
              .userId(payload.getEvent().getUser())
              .view(appHomeView)
          );
      return ctx.ack();
    });
    // Health check application
    app.command("/ping",
        (slashCommandRequest, context) -> context.ack(":+1: Hai, there we are alive"));
    // When user selecting user
    app.blockAction("select_user-action", (req, ctx) -> {
      BlockActionPayload payloadRequest = req.getPayload();
      return userAction.onAssignTask(payloadRequest, ctx);
    });

    app.blockAction("select_status-action", (req, ctx) -> {
      BlockActionPayload payloadRequest = req.getPayload();
      return userAction.onUpdateStatusTask(payloadRequest, ctx);
    });
    // When a user clicks a button in the actions block
    app.blockAction("approve-action", (req, ctx) -> {
      BlockActionPayload request = req
          .getPayload();
      return userAction.onApproveTask(request, ctx);
    });
    // When a user clicks a button in the actions block
    app.blockAction("deny-action", (req, ctx) -> {
      BlockActionPayload request = req
          .getPayload();
      return userAction.onDenyTask(request, ctx);
    });

    int port = Integer.parseInt(System.getenv("PORT"));
    SlackAppServer server = new SlackAppServer(app,port);
    server.start(); // http://localhost:3030
  }
}
