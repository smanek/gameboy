package com.goodorbad.gameboy.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.FNEG;

import java.util.Collection;
import java.util.List;

/**
 * Some utility methods for doing functional stuff.
 */
public class Functional {
  private void Functional() {
    // don't instantiate
  }

  // only returns items for which the predicate returns 'True'
  public static <A> List<A> filter(Function<A, Boolean> predicate, Collection<A> data) {
    List<A> res = Lists.newLinkedList();
    for (A a : data) {
      if (predicate.apply(a)) {
        res.add(a);
      }
    }

    return res;
  }

  public static <A, B> List<B> map(Function<A, B> fn, Collection<A> data) {
    List<B> res = Lists.newArrayListWithExpectedSize(data.size());

    for (A a : data) {
      res.add(fn.apply(a));
    }

    return res;
  }
}
