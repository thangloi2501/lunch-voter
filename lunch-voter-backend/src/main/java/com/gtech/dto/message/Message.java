package com.gtech.dto.message;

public interface Message {
  enum Type {
    VOTE_INFO, USER_INFO;
  }

  Type getType();
}
