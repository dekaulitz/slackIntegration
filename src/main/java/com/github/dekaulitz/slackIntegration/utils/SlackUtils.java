package com.github.dekaulitz.slackIntegration.utils;

public class SlackUtils {

  // mentioning user
  public static String mentionTag(String id) {
    return "<@" + id + ">";
  }

  // mentioning thread name
  public static String channelTag(String id) {
    return "<#" + id + ">";
  }
}
