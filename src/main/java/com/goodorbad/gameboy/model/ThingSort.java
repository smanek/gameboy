package com.goodorbad.gameboy.model;

import com.google.common.collect.ImmutableMap;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: smanek
 * Date: 8/28/11
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ThingSort {
  NONE(new Comparator<StandaloneThing>() {
    @Override
    public int compare(StandaloneThing StandaloneThing, StandaloneThing StandaloneThing1) {
      return 0;
    }
  }),
  ID(new Comparator<StandaloneThing>() {
    @Override
    public int compare(StandaloneThing a, StandaloneThing b) {
      return new Long(a.getId()).compareTo(b.getId());
    }
  }),
  CONTENTION(new Comparator<StandaloneThing>() {
    @Override
    public int compare(StandaloneThing a, StandaloneThing b) {
      return new Double(b.getContention()).compareTo(a.getContention());
    }
  });

  private final Comparator<StandaloneThing> sorter;

  public Comparator<StandaloneThing> getSorter() {
    return sorter;
  }

  ThingSort(Comparator<StandaloneThing> sorter) {
    this.sorter = sorter;
  }

  private static String normalizeName(String rawName) {
    return rawName.trim().toLowerCase();
  }

  private static final Map<String, ThingSort> normalizedNames;

  static {
    ImmutableMap.Builder<String, ThingSort> builder = ImmutableMap.builder();
    for (ThingSort thingSort : ThingSort.values()) {
      builder.put(normalizeName(thingSort.name()), thingSort);
    }
    normalizedNames = builder.build();
  }

  public static ThingSort get(String name) {
    return normalizedNames.get(normalizeName(name));
  }
}
