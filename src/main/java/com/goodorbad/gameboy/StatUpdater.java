package com.goodorbad.gameboy;

import com.goodorbad.gameboy.model.Thing;
import com.goodorbad.gameboy.model.User;
import com.goodorbad.gameboy.model.Vote;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.TimerMetric;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Actually update the stats we care about.
 */
public class StatUpdater {

  private final Jedis redis;
  private static final Log log = LogFactory.getLog(StatUpdater.class);

  private static final TimerMetric TOTAL_UPDATE_TIME = Metrics.newTimer(StatUpdater.class, "total_update_time",
      TimeUnit.MILLISECONDS, TimeUnit.HOURS);
  private static final TimerMetric THING_LOAD_TIME = Metrics.newTimer(StatUpdater.class, "thing_load_time",
      TimeUnit.MILLISECONDS, TimeUnit.HOURS);
  private static final TimerMetric USER_LOAD_TIME = Metrics.newTimer(StatUpdater.class, "user_load_time",
      TimeUnit.MILLISECONDS, TimeUnit.HOURS);
  private static final TimerMetric META_STAT_TIME = Metrics.newTimer(StatUpdater.class, "meta_stat_time",
      TimeUnit.MILLISECONDS, TimeUnit.HOURS);


  private static final TimerMetric PER_USER_LOAD_TIME = Metrics.newTimer(StatUpdater.class, "per_user_load_time",
      TimeUnit.MILLISECONDS, TimeUnit.HOURS);

  private static final TimerMetric PER_THING_LOAD_TIME = Metrics.newTimer(StatUpdater.class, "per_thing_load_time",
      TimeUnit.MILLISECONDS, TimeUnit.HOURS);

  private final ResultCache resultCache;

  public StatUpdater(Jedis r) {
    this.redis = r;
    this.resultCache = ResultCache.getInstance();
  }


  public boolean update() {
    log.info("StatUpdater is doing an update");
    final long startTime = System.currentTimeMillis();

    Map<Long, Thing> things = loadThings();
    Map<Long, User> users = loadUsers(things);
    computeMetaStatistics(users, things);

    final long endTime = System.currentTimeMillis();
    TOTAL_UPDATE_TIME.update(endTime - startTime, TimeUnit.MILLISECONDS);

    resultCache.update(users, things);

    return true;
  }

  private void computeMetaStatistics(Map<Long, User> users, Map<Long, Thing> things) {
    final long startTime = System.currentTimeMillis();

    final long endTime = System.currentTimeMillis();
    META_STAT_TIME.update(endTime - startTime, TimeUnit.MILLISECONDS);
  }

  private Map<Long, User> loadUsers(Map<Long, Thing> things) {
    final long startTime = System.currentTimeMillis();

    ImmutableMap.Builder<Long, User> builder = ImmutableMap.builder();
    Set<String> users = redis.smembers("user");
    assert things.size() > 0;

    for (String userIdStr : users) {
      final long userLoadStart = System.currentTimeMillis();

      log.info("Loading user " + userIdStr);
      long userId = Long.parseLong(userIdStr);

      Set<String> votes = redis.smembers("user:" + userIdStr + ":voted");
      List<Vote> userVotes = Lists.newArrayListWithExpectedSize(votes.size());
      for (String thingVotedOnStr : votes) {
        final long thingVotedOn = Long.valueOf(thingVotedOnStr);

        final String voteValStr = redis.get("user:" + userIdStr + ":thing:" + thingVotedOnStr + ":vote");
        final short voteVal = Short.valueOf(voteValStr);
        userVotes.add(new Vote(things.get(thingVotedOn), voteVal));
      }

      User u = new User(userId, userVotes);
      PER_USER_LOAD_TIME.update(System.currentTimeMillis() - userLoadStart, TimeUnit.MILLISECONDS);
      builder.put(userId, u);
    }

    USER_LOAD_TIME.update(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
    return builder.build();
  }

  private Map<Long, Thing> loadThings() {
    final long startTime = System.currentTimeMillis();

    ImmutableMap.Builder<Long, Thing> builder = ImmutableMap.builder();
    Set<String> things = redis.smembers("thing");
    assert things.size() > 0;

    for (String thingIdStr : things) {
      final long thingStartTime = System.currentTimeMillis();
      log.info("Loading thing " + thingIdStr);

      final long id = Long.valueOf(thingIdStr);
      final long upVotes = getLongVal("thing:" + thingIdStr + ":ballot:1", 0);
      final long downVotes = getLongVal("thing:" + thingIdStr + ":ballot:-1", 0);
      final long abstainVotes = getLongVal("thing:" + thingIdStr + ":ballot:0", 0);
      final long totalVotes = getLongVal("thing:" + thingIdStr + ":ballot",
          upVotes + downVotes + abstainVotes);
      final long netVotes = getLongVal("thing:" + thingIdStr + ":tally", upVotes - downVotes);
      final long uniqueUserVotes = redis.scard("thing:" + thingIdStr + ":user");
      final Thing t = new Thing(id, upVotes, downVotes, abstainVotes, totalVotes, netVotes, uniqueUserVotes);

      PER_THING_LOAD_TIME.update(System.currentTimeMillis() - thingStartTime, TimeUnit.MILLISECONDS);
      builder.put(id, t);
    }

    final long endTime = System.currentTimeMillis();
    THING_LOAD_TIME.update(endTime - startTime, TimeUnit.MILLISECONDS);

    return builder.build();
  }

  private long getLongVal(String key, long defaultVal) {
    String val = redis.get(key);

    return val == null ? defaultVal : Long.parseLong(val);
  }
}
