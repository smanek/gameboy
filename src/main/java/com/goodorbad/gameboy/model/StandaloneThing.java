package com.goodorbad.gameboy.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model of things.
 */
public class StandaloneThing {
  private final long id;

  private final long upVotes;
  private final long downVotes;
  private final long abstainVotes;

  private final long totalVotes;
  private final long netVote;
  private final long uniqueUserVotes;

  @JsonCreator
  public StandaloneThing(@JsonProperty("id") long id,
                         @JsonProperty("upVotes") long upVotes,
                         @JsonProperty("downVotes") long downVotes,
                         @JsonProperty("abstainVotes") long abstainVotes,
                         @JsonProperty("totalVotes") long totalVotes,
                         @JsonProperty("newVote") long netVote,
                         @JsonProperty("uniqueUserVotes") long uniqueUserVotes) {
    this.id = id;
    this.upVotes = upVotes;
    this.downVotes = downVotes;
    this.abstainVotes = abstainVotes;
    this.totalVotes = totalVotes;
    this.netVote = netVote;
    this.uniqueUserVotes = uniqueUserVotes;
  }

  public StandaloneThing(StandaloneThing src) {
    this(src.id, src.upVotes, src.downVotes, src.abstainVotes,
        src.totalVotes, src.netVote, src.uniqueUserVotes);
  }
  public long getId() {
    return id;
  }

  public long getUpVotes() {
    return upVotes;
  }

  public long getDownVotes() {
    return downVotes;
  }

  public long getAbstainVotes() {
    return abstainVotes;
  }

  public long getTotalVotes() {
    return totalVotes;
  }

  public long getNetVote() {
    return netVote;
  }

  public long getUniqueUserVotes() {
    return uniqueUserVotes;
  }

  @Override
  public String toString() {
    return "StandaloneThing{" +
        "id=" + id +
        ", upVotes=" + upVotes +
        ", downVotes=" + downVotes +
        ", abstainVotes=" + abstainVotes +
        ", totalVotes=" + totalVotes +
        ", netVote=" + netVote +
        ", uniqueUserVotes=" + uniqueUserVotes +
        '}';
  }

  public RelativeThing relativeTo(Metastats metastats) {
    return new RelativeThing(this, metastats);
  }
}
