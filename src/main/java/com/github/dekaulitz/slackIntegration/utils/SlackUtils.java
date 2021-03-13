package com.github.dekaulitz.slackIntegration.utils;

public class SlackUtils {

  public static String mentionTag(String user) {
    return "<@" + user + ">";
  }
}
