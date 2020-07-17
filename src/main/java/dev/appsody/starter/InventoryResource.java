package dev.appsody.starter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resource")
public class InventoryResource {

    @GET
    public String getRequest() {
        return "StarterResource response";
    }
}
