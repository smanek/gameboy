package com.goodorbad.gameboy;

import com.goodorbad.gameboy.util.NamedThreadFactory;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.CounterMetric;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Loads information from Redis.
 */
public class DataLoader {

  private static final String REDIS_HOST = "localhost";
  private static final DataLoader INSTANCE = new DataLoader();

  private static final Log log = LogFactory.getLog(DataLoader.class);

  public static DataLoader getInstance() {
    return INSTANCE;
  }

  private final ScheduledExecutorService executorService;
  private final JedisPool connectionPool;
  private final AtomicBoolean started = new AtomicBoolean(false);
  private final CounterMetric loadErrors = Metrics.newCounter(DataLoader.class, "load_errors");

  private DataLoader() {
    final JedisPoolConfig redisConfig = new JedisPoolConfig();
    redisConfig.setTestOnBorrow(true);
    connectionPool = new JedisPool(redisConfig, REDIS_HOST);
    this.executorService = Executors.newScheduledThreadPool(2, new NamedThreadFactory("dataloader"));
  }

  public void start() {
    log.info("Starting the DataLoader");
    boolean alreadyStarted = started.getAndSet(true);
    if (alreadyStarted) {
      log.error("The DataLoader has already been started");
    } else {
      log.info("Scheduling data to be reloaded every 30 seconds");
      executorService.scheduleWithFixedDelay(getLoadJob(), 0, 30, TimeUnit.SECONDS);
    }

  }

  private Runnable getLoadJob() {
    return new Runnable() {
      @Override
      public void run() {
        log.info("Running iteration of updateStats");
        Jedis jedis = connectionPool.getResource();
        assert jedis != null;
        log.info("Got a jedis client");
        try {
          StatUpdater statUpdater = new StatUpdater(jedis);
          boolean success = false;
          while (!success) {
            success = statUpdater.update();
            if (!success) {
              log.error("Couldn't update stats");
              try {
                Thread.sleep(5000);
              } catch (InterruptedException e) {
                throw new RuntimeException("Error sleeping", e);
              }
            }
          }
        } catch (Throwable t) {
          log.error("Error updating stats", t);
          loadErrors.inc();
        } finally {
          log.info("Iteration of StatUpdater is finished");
          connectionPool.returnResource(jedis);
        }
      }
    };
  }

}
