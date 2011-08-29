package com.goodorbad.gameboy.model;

import com.goodorbad.gameboy.util.Functional;
import com.google.common.base.Function;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import java.util.Collection;

/**
 * Meta-statistics across many users/things.
 */
public class Metastats {
  private final SummaryStatistics userUpvotePercent;
  private final SummaryStatistics userDownvotePercent;

  private final SummaryStatistics thingUpvotePercent;
  private final SummaryStatistics thingDownvotePercent;

  private final double noVoteThings;
  private final double noVoteUsers;

  public Metastats(Collection<StandaloneUser> standaloneUsers, Collection<Thing> things) {
    userUpvotePercent = new SummaryStatistics();
    userDownvotePercent = new SummaryStatistics();

    long noVoteUserCount = 0;
    for (StandaloneUser standaloneUser : standaloneUsers) {
      final int totalVotes = standaloneUser.getVotes().size();
      final int upvotes = standaloneUser.getUpVotes();
      final int dowvotes = standaloneUser.getDownVotes();

      if (totalVotes > 0) {
        final double upvotePercent = (double) upvotes / (double) totalVotes;
        userUpvotePercent.addValue(upvotePercent);

        final double downvotePercent = (double) dowvotes / (double) totalVotes;
        userDownvotePercent.addValue(downvotePercent);
      } else {
        noVoteUserCount++;
      }
    }
    this.noVoteUsers = (double) noVoteUserCount / (double) standaloneUsers.size();


    thingUpvotePercent = new SummaryStatistics();
    thingDownvotePercent = new SummaryStatistics();

    long noVoteThingCount = 0;
    for (Thing thing : things) {
      if (thing.getTotalVotes() > 0) {
        final double upvotePercent = (double) thing.getUpVotes() / (double) thing.getTotalVotes();
        thingUpvotePercent.addValue(upvotePercent);

        final double downvotePercent = (double) thing.getDownVotes() / (double) thing.getTotalVotes();
        thingDownvotePercent.addValue(downvotePercent);
      } else {
        noVoteThingCount++;
      }
    }
    this.noVoteThings = (double) noVoteThingCount / (double) things.size();
  }

  public SummaryStatistics getUserUpvotePercent() {
    return userUpvotePercent;
  }

  public SummaryStatistics getUserDownvotePercent() {
    return userDownvotePercent;
  }

  public SummaryStatistics getThingUpvotePercent() {
    return thingUpvotePercent;
  }

  public SummaryStatistics getThingDownvotePercent() {
    return thingDownvotePercent;
  }

  public double getNoVoteThings() {
    return noVoteThings;
  }

  public double getNoVoteUsers() {
    return noVoteUsers;
  }
}
