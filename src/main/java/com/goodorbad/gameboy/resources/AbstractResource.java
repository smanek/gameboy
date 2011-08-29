package com.goodorbad.gameboy.resources;

import com.goodorbad.gameboy.ResultCache;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Some common methods for all REST resources.
 */
public class AbstractResource {
  protected final ResultCache rs = ResultCache.getInstance();

  protected void checkIfInitialized() {
    if (!rs.isInitialized()) {
      throw new WebApplicationException(
          Response
              .status(Response.Status.SERVICE_UNAVAILABLE)
              .entity("Server hasn't finished initial load of data yet")
              .build()
      );
    }
  }
}
