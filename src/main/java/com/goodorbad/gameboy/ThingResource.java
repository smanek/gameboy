package com.goodorbad.gameboy;

import com.goodorbad.gameboy.model.Thing;
import com.goodorbad.gameboy.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Set;

/**
 * Access 'things' over http/json.
 */
@Path("/user")
public class ThingResource {

  private final Map<Long, Thing> things = ResultCache.getInstance().getThings();

  private void checkIfInitialized() {
    if (things == null) {
      throw new WebApplicationException(
          Response
              .status(Response.Status.SERVICE_UNAVAILABLE)
              .entity("Server hasn't finished initial load of data yet")
              .build()
      );
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<Long> listAllThings() {
    checkIfInitialized();
    return things.keySet();
  }

  @GET
  @Path("/{thingId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Thing getUser(@PathParam("thingId") long thingId) {
    checkIfInitialized();
    Thing t = things.get(thingId);
    if (t == null) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } else {
      return t;
    }
  }
}
