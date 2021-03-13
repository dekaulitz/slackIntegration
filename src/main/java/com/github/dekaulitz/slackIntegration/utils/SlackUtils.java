package com.github.dekaulitz.slackIntegration.utils;

public class SlackUtils {

  public static String mentionTag(String id) {
    return "<@" + id + ">";
  }

  public static String channelTag(String id) {
    return "<#" + id + ">";
  }
}
