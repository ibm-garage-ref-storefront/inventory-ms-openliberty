package dev.appsody.starter.service;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import dev.appsody.starter.model.Inventory;
import dev.appsody.starter.repository.InventoryRepository;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@ApplicationScoped
@Path("/inventory")
@OpenAPIDefinition(
        info = @Info(
                title = "Inventory Service",
                version = "0.0",
                description = "Inventory APIs",
                contact = @Contact(url = "https://github.com/ibm-cloud-architecture", name = "IBM CASE"),
                license = @License(name = "License",
                        url = "https://github.com/ibm-cloud-architecture/refarch-cloudnative-micro-inventory/blob/microprofile/inventory/LICENSE")
        )
)
public class InventoryService {

    @GET
    @Produces("application/json")
    @Retry(maxRetries = 2)
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "404",
                    description = "Inventory Not Found",
                    content = @Content(
                            mediaType = "text/plain"
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "text/plain"
                    )
            ),
            @APIResponse(
                    responseCode = "200",
                    description = "List of items from the Inventory",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Inventory.class)
                    )
            )
    }
    )

    @Operation(
            summary = "Get Inventory Items",
            description = "Retrieving all the available items from the inventory database"
    )
    @Timed(name = "Inventory.timer",
            absolute = true,
            displayName = "Inventory Timer",
            description = "Time taken by the Inventory",
            reusable = true)
    @Counted(name = "Inventory",
            absolute = true,
            displayName = "Inventory Call count",
            description = "Number of times the Inventory call happened.",
            reusable = true)
    @Metered(name = "InventoryMeter",
            displayName = "Inventory Call Frequency",
            description = "Rate of the calls made to Inventory",
            reusable = true)
    /**
     * Method is responsible for retrieving inventory details for all items.
     * @return a json object of inventory detail items
     */
    public String getInventoryDetails() {
        InventoryRepository inv = new InventoryRepository();
        Gson gson = new Gson();
        return gson.toJson(inv.getInventoryDetails());
    }
}
