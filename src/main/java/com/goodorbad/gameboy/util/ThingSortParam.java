package com.goodorbad.gameboy.util;

import com.goodorbad.gameboy.model.ThingSort;
import com.google.common.base.Preconditions;

import javax.ws.rs.WebApplicationException;

/**
 * How to sort things.
 */
public class ThingSortParam extends AbstractParam<ThingSort> {

  public ThingSortParam(String param) throws WebApplicationException {
    super(param);
  }

  @Override
  protected ThingSort parse(String param) throws Throwable {
    final ThingSort res = ThingSort.get(param);
    Preconditions.checkNotNull(res);
    return res;
  }
}
