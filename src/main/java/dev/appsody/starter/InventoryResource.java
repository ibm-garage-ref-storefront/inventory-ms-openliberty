package dev.appsody.starter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.opentracing.Traced;

@Path("/resource")
public class InventoryResource {

    @GET
    @Traced
    public String getRequest() {
        return "StarterResource response";
    }
}
