package com.goodorbad.gameboy.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * A user, relative to some body of users (has stats about how this user compares to others).
 */
public class RelativeUser extends StandaloneUser {

  // how this user's upvote % compares to the population as a whole
  // if the mean user has 70% upvotes, with a 10% standard deviation,
  // but this user has 80% upvotes, then their 'upvoteStandardDeviationsFromMean' is 1.0
  // if this user has 90% upvotes, then their 'upvoteStandardDeviation' is 2.0
  // etc.
  private final double upvoteStandardDeviationsFromMean;

  // similar to upvoteStandardDeviation (but for downvotes).
  private final double downvoteStandardDeviationsFromMean;

  public RelativeUser(StandaloneUser s, Metastats m) {
    super(s);
    final double totalVotes = s.getVotes().size();

    if (totalVotes > 0) {
      final double upvotePercent = (double) s.getUpVotes() / totalVotes;
      final double upvotePercentDelta = upvotePercent - m.getUserUpvotePercent().getMean();
      upvoteStandardDeviationsFromMean = upvotePercentDelta / m.getUserUpvotePercent().getStandardDeviation();

      final double downvotePercent = (double) s.getDownVotes() / totalVotes;
      final double downvotePercentDelta = downvotePercent - m.getUserDownvotePercent().getMean();
      this.downvoteStandardDeviationsFromMean = downvotePercentDelta
          / m.getUserDownvotePercent().getStandardDeviation();
    } else {
      this.upvoteStandardDeviationsFromMean = Double.NaN;
      this.downvoteStandardDeviationsFromMean = Double.NaN;
    }
  }

  public double getUpvoteStandardDeviationsFromMean() {
    return upvoteStandardDeviationsFromMean;
  }

  public double getDownvoteStandardDeviationsFromMean() {
    return downvoteStandardDeviationsFromMean;
  }
}
