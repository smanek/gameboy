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

  public Metastats(Collection<User> users, Collection<Thing> things) {
    userUpvotePercent = new SummaryStatistics();
    userDownvotePercent = new SummaryStatistics();

    long noVoteUserCount = 0;
    for (User user : users) {
      final int totalVotes = user.getVotes().size();

      final int upvotes = Functional.filter(new Function<Vote, Boolean>() {
        @Override
        public Boolean apply(Vote v) {
          return v.getVote() > 0;
        }
      }, user.getVotes()).size();

      final int dowvotes = Functional.filter(new Function<Vote, Boolean>() {
        @Override
        public Boolean apply(Vote v) {
          return v.getVote() < 0;
        }
      }, user.getVotes()).size();

      if (totalVotes > 0) {
        final double upvotePercent = (double) upvotes / (double) totalVotes;
        userUpvotePercent.addValue(upvotePercent);

        final double downvotePercent = (double) dowvotes / (double) totalVotes;
        userDownvotePercent.addValue(downvotePercent);
      } else {
        noVoteUserCount++;
      }
    }
    this.noVoteUsers = (double) noVoteUserCount / (double) users.size();


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
