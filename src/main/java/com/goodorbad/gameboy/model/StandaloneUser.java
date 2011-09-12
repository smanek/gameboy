package com.goodorbad.gameboy.model;

import com.goodorbad.gameboy.util.Functional;
import com.google.common.base.Function;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Data model that represents a user.
 */
public class StandaloneUser {
  private final long id;
  private final List<Vote> votes;
  private final int upVotes;
  private final int downVotes;
  private final int abstainVotes;

  @JsonCreator
  public StandaloneUser(@JsonProperty("id") long id, @JsonProperty("votes") List<Vote> votes) {
    this.id = id;
    this.votes = votes;

    // TODO: make not stupidly slow, if it ends up mattering
    this.upVotes = Functional.filter(new Function<Vote, Boolean>() {
      @Override
      public Boolean apply(Vote v) {
        return v.getVote() > 0;
      }
    }, getVotes()).size();

    this.downVotes = Functional.filter(new Function<Vote, Boolean>() {
      @Override
      public Boolean apply(Vote v) {
        return v.getVote() < 0;
      }
    }, getVotes()).size();

    this.abstainVotes = Functional.filter(new Function<Vote, Boolean>() {
      @Override
      public Boolean apply(Vote v) {
        return v.getVote() == 0;
      }
    }, getVotes()).size();


  }

  public StandaloneUser(StandaloneUser src) {
    this.id = src.id;
    this.votes = null;//src.getVotes();
    this.upVotes = src.upVotes;
    this.downVotes = src.downVotes;
    this.abstainVotes = src.abstainVotes;
  }


  public long getId() {
    return id;
  }

  public List<Vote> getVotes() {
    return votes;
  }

  public int getUpVotes() {
    return upVotes;
  }

  public int getDownVotes() {
    return downVotes;
  }

  public int getAbstainVotes() {
    return abstainVotes;
  }

  public RelativeUser relativeTo(Metastats metastats) {
    return new RelativeUser(this, metastats);
  }
}
