package com.goodorbad.gameboy.model;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Help serialize Metastats.
 */
public class MetastatsDao {

  private final SummaryStatDao userUpvotes;
  private final SummaryStatDao userDownvotes;

  private final SummaryStatDao thingUpvotes;
  private final SummaryStatDao thingDownvotes;

  private final double noVoteThings;
  private final double noVoteUsers;

  @JsonCreator
  public MetastatsDao(@JsonProperty("userUpvotes") SummaryStatDao userUpvotes,
                      @JsonProperty("userDownvotes") SummaryStatDao userDownvotes,
                      @JsonProperty("thingUpvotes") SummaryStatDao thingUpvotes,
                      @JsonProperty("thingDownvotes") SummaryStatDao thingDownvotes,
                      @JsonProperty("noVoteThings") double noVoteThings,
                      @JsonProperty("noVoteUsers") double noVoteUsers) {
    this.userUpvotes = userUpvotes;
    this.userDownvotes = userDownvotes;
    this.thingUpvotes = thingUpvotes;
    this.thingDownvotes = thingDownvotes;
    this.noVoteThings = noVoteThings;
    this.noVoteUsers = noVoteUsers;
  }

  public static MetastatsDao from(Metastats m) {
    return new MetastatsDao(SummaryStatDao.from(m.getUserUpvotePercent()),
        SummaryStatDao.from(m.getUserDownvotePercent()),
        SummaryStatDao.from(m.getThingUpvotePercent()),
        SummaryStatDao.from(m.getThingDownvotePercent()),
        m.getNoVoteThings(),
        m.getNoVoteUsers());
  }

  public SummaryStatDao getUserUpvotes() {
    return userUpvotes;
  }

  public SummaryStatDao getUserDownvotes() {
    return userDownvotes;
  }

  public SummaryStatDao getThingUpvotes() {
    return thingUpvotes;
  }

  public SummaryStatDao getThingDownvotes() {
    return thingDownvotes;
  }

  public double getNoVoteThings() {
    return noVoteThings;
  }

  public double getNoVoteUsers() {
    return noVoteUsers;
  }

  public static class SummaryStatDao {
    private final double mean;
    private final double min;
    private final double max;
    private final double standardDeviation;

    public double getMean() {
      return mean;
    }

    public double getMin() {
      return min;
    }

    public double getMax() {
      return max;
    }

    public double getStandardDeviation() {
      return standardDeviation;
    }

    @JsonCreator
    public SummaryStatDao(@JsonProperty("mean") double mean,
                          @JsonProperty("min") double min,
                          @JsonProperty("max") double max,
                          @JsonProperty("standardDeviation") double standardDeviation) {
      this.mean = mean;
      this.min = min;
      this.max = max;
      this.standardDeviation = standardDeviation;
    }

    public static SummaryStatDao from(SummaryStatistics s) {
      return new SummaryStatDao(s.getMean(), s.getMin(), s.getMax(), s.getStandardDeviation());
    }
  }
}
