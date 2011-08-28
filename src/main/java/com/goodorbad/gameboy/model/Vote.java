package com.goodorbad.gameboy.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a single user's vote on a single thing.
 */
public class Vote {
  private final Thing thing;
  private final short vote;

  @JsonCreator
  public Vote(@JsonProperty("thing") Thing thing,
              @JsonProperty("vote") short vote) {
    this.thing = thing;
    this.vote = vote;
  }

  public Thing getThing() {
    return thing;
  }

  public short getVote() {
    return vote;
  }
}
