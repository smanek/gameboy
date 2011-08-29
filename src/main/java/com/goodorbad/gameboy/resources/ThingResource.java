package com.goodorbad.gameboy.resources;

import com.goodorbad.gameboy.model.RelativeThing;
import com.goodorbad.gameboy.model.StandaloneThing;
import com.goodorbad.gameboy.model.ThingSort;
import com.goodorbad.gameboy.util.Functional;
import com.goodorbad.gameboy.util.ThingSortParam;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Access 'things' over http/json.
 */
@Path("/thing")
public class ThingResource extends AbstractResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Long> listAllThings(@DefaultValue("none") @QueryParam("sortBy") ThingSortParam sort) {
    checkIfInitialized();
    List<StandaloneThing> things = Lists.newArrayList(rs.getThings().values());

    if (sort.getValue() != ThingSort.NONE) {
      Collections.sort(things, sort.getValue().getSorter());
    }

    return Functional.map(
        new Function<StandaloneThing, Long>() {
          @Override
          public Long apply(StandaloneThing standaloneThing) {
            return standaloneThing.getId();
          }
        },
        things);
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
