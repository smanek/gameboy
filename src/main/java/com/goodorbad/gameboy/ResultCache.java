package com.goodorbad.gameboy;

import com.goodorbad.gameboy.model.Thing;
import com.goodorbad.gameboy.model.User;
import com.google.common.base.Preconditions;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.GaugeMetric;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Just caches the results from the previous run of the stat updater.
 */
public class ResultCache {

  private static final ResultCache INSTANCE = new ResultCache();

  public static ResultCache getInstance() {
    return INSTANCE;
  }

  private final ReadWriteLock lock;
  private final AtomicBoolean initialized;
  private final AtomicLong lastUpdate;

  private Map<Long, User> users;
  private Map<Long, Thing> things;

  private ResultCache() {
    lock = new ReentrantReadWriteLock();
    users = null;
    things = null;
    initialized = new AtomicBoolean(false);
    lastUpdate = new AtomicLong(0);

    Metrics.newGauge(ResultCache.class, "initialized", new GaugeMetric<Boolean>() {
      @Override
      public Boolean value() {
        return initialized.get();
      }
    });

    Metrics.newGauge(ResultCache.class, "cacheAge", new GaugeMetric<Long>() {
      @Override
      public Long value() {
        return System.currentTimeMillis() - lastUpdate.get();
      }
    });

  }

  public void update(Map<Long, User> users, Map<Long, Thing> things) {
    Preconditions.checkNotNull(users);
    Preconditions.checkNotNull(things);

    lock.writeLock().lock();
    try {
      this.users = users;
      this.things = things;
    } finally {
      lock.writeLock().unlock();
    }
    initialized.set(true);
    lastUpdate.set(System.currentTimeMillis());
  }

  // the lock establishes a 'happens-before' relationship, users/things don't have to volatile.
  public Map<Long, User> getUsers() {
    lock.readLock().lock();
    try {
      return users;
    } finally {
      lock.readLock().unlock();
    }
  }

  public Map<Long, Thing> getThings() {
    lock.readLock().lock();
    try {
      return things;
    } finally {
      lock.readLock().unlock();
    }
  }
}
