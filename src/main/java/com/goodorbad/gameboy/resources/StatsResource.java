package com.goodorbad.gameboy.resources;

import com.goodorbad.gameboy.model.MetastatsDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Present some aggregate statistics that might be interesting.
 */
@Path("/stats")
public class StatsResource extends AbstractResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public MetastatsDao getStats() {
    checkIfInitialized();
    return MetastatsDao.from(rs.getMetastats());
  }


}
