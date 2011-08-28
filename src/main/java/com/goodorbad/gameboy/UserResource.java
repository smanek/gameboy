package com.goodorbad.gameboy;

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
 * Share Users.
 */
@Path("/user")
public class UserResource {

  private final Map<Long, User> users = ResultCache.getInstance().getUsers();

  private void checkIfInitialized() {
    if (users == null) {
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
  public Set<Long> listAllUsers() {
    checkIfInitialized();
    return users.keySet();
  }

  @GET
  @Path("/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUser(@PathParam("userId") long userId) {
    checkIfInitialized();
    User u = users.get(userId);
    if (u == null) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } else {
      return u;
    }
  }
}
