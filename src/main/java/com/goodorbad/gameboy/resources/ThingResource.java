package com.goodorbad.gameboy.resources;

import com.goodorbad.gameboy.model.RelativeThing;
import com.goodorbad.gameboy.model.StandaloneThing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Access 'things' over http/json.
 */
@Path("/thing")
public class ThingResource extends AbstractResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<Long> listAllThings() {
    checkIfInitialized();
    return rs.getThings().keySet();
  }

  @GET
  @Path("/{thingId}")
  @Produces(MediaType.APPLICATION_JSON)
  public RelativeThing getUser(@PathParam("thingId") long thingId) {
    checkIfInitialized();
    StandaloneThing t = rs.getThings().get(thingId);
    if (t == null) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } else {
      return t.relativeTo(rs.getMetastats());
    }
  }
}
