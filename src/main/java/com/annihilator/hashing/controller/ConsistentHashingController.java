package com.annihilator.hashing.controller;

import com.annihilator.hashing.configuration.ConsistentHashingConfiguration;
import com.annihilator.hashing.dto.ResponseStructure;
import com.annihilator.hashing.dto.Result;
import com.annihilator.hashing.hashing.ConsistentHashing;
import com.annihilator.hashing.hashing.Hashing;
import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/_hashing")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ConsistentHashingController {

  private static final Logger logger = LoggerFactory.getLogger(ConsistentHashingController.class);
  private static final Gson gson = new Gson();

  private ConsistentHashingConfiguration config;
  private final Hashing hashClient;

  public ConsistentHashingController(ConsistentHashingConfiguration config) {
    this.config = config;
    this.hashClient = ConsistentHashing.getInstance(config);
  }

  @POST
  @Path("/add_node")
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response addNode(@QueryParam("node") String node) {

    ResponseStructure resp = hashClient.addNode(node);

    if (resp.getResult().equals(Result.SUCCEEDED)) {

      return Response.ok(gson.toJson(resp)).build();
    } else {

      return Response.status(400).entity(gson.toJson(resp)).build();
    }
  }

  @DELETE
  @Path("/remove_node")
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response deleteNode(@QueryParam("node") String node) {

    ResponseStructure resp = hashClient.removeNode(node);

    if (resp.getResult().equals(Result.SUCCEEDED)) {

      return Response.ok(gson.toJson(resp)).build();
    } else {

      return Response.status(404).entity(gson.toJson(resp)).build();
    }
  }

  @GET
  @Path("/destination")
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response findDestination(@QueryParam("req_id") String reqId) {

    ResponseStructure resp = hashClient.getDestination(reqId);

    if (resp.getResult().equals(Result.SUCCEEDED)) {

      return Response.ok(gson.toJson(resp)).build();
    } else {

      return Response.status(404).entity(gson.toJson(resp)).build();
    }
  }
}
