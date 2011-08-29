package com.goodorbad.gameboy.model;

/**
 *  A thing, relative to some body of things (has stats about how this thing compares to others).
 */
public class RelativeThing extends StandaloneThing {

  // how this thing's upvote % compares to the population as a whole
  // if the mean thing has 70% upvotes, with a .1 standard deviation,
  // but this thing has 80% upvotes, then this thing's 'upvoteStandardDeviationsFromMean' is 1.0
  // if this thing has 90% upvotes, then this thing's 'upvoteStandardDeviationFromMean' is 2.0
  // etc.
  private final double upvoteStandardDeviationsFromMean;

  // similar to upvoteStandardDeviationsFromMean (but for downvotes).
  private final double downvoteStandardDeviationsFromMean;

  public RelativeThing(StandaloneThing thing, Metastats m) {
    super(thing);
    final double totalVotes = thing.getTotalVotes();

    if (totalVotes > 0) {
      final double upvotePercent = (double) thing.getUpVotes() / totalVotes;
      final double upvotePercentDelta = upvotePercent - m.getThingUpvotePercent().getMean();
      upvoteStandardDeviationsFromMean = upvotePercentDelta / m.getThingUpvotePercent().getStandardDeviation();

      final double downvotePercent = (double) thing.getDownVotes() / totalVotes;
      final double downvotePercentDelta = downvotePercent - m.getThingDownvotePercent().getMean();
      this.downvoteStandardDeviationsFromMean = downvotePercentDelta
          / m.getThingDownvotePercent().getStandardDeviation();
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
