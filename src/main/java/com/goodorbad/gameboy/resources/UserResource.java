package com.goodorbad.gameboy.resources;

import com.goodorbad.gameboy.model.StandaloneUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Share Users.
 */
@Path("/user")
public class UserResource extends AbstractResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<Long> listAllUsers() {
    checkIfInitialized();
    return rs.getUsers().keySet();
  }

  @GET
  @Path("/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public StandaloneUser getUser(@PathParam("userId") long userId) {
    checkIfInitialized();
    StandaloneUser u = rs.getUsers().get(userId);
    if (u == null) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } else {
      return u.relativeTo(rs.getMetastats());
    }
  }
}
