package com.goodorbad.gameboy.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Data model that represents a user.
 */
public class User {
  private final long id;
  private final List<Vote> votes;

  @JsonCreator
  public User(@JsonProperty("id") long id, @JsonProperty("votes") List<Vote> votes) {
    this.id = id;
    this.votes = votes;
  }

  public long getId() {
    return id;
  }

  public List<Vote> getVotes() {
    return votes;
  }
}
