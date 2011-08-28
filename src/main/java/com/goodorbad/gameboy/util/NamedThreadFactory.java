package com.goodorbad.gameboy.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Name threads - makes debugging easier.
 */
public class NamedThreadFactory implements ThreadFactory {
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  private final String namePrefix;

  public NamedThreadFactory(String name) {
    this.namePrefix = name + "-thread-";
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
    return t;
  }
}
