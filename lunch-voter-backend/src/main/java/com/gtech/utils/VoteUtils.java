package com.gtech.utils;

import java.util.Random;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VoteUtils {

  public static final String TOPIC_VOTE = "/ws/topic/vote";
  public static final Random RANDOM = new Random();

  public static String generateCode() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }
}
