package com.customerdemo.resource;

import com.customerdemo.domain.Customer;
import com.customerdemo.service.MongoService;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.wordnik.swagger.annotations.Api;
import org.bson.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "customer", description = "Customer Resource for performing CRUD operations on customers Collection")
public class CustomerResource {

    private MongoCollection<Document> collection;
    private final MongoService mongoService;

    public CustomerResource(MongoCollection<Document> collection, MongoService mongoService) {
        this.collection = collection;
        this.mongoService = mongoService;
    }

    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(@NotNull @Valid final Customer customer) {
        Gson gson = new Gson();
        String json = gson.toJson(customer);

        mongoService.insertCustomer(collection, new Document(BasicDBObject.parse(json)));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer created successfully");
        return Response.ok(response).build();
    }

    @GET
    @Timed
    public Response getCustomers() {
        
        List<Customer> documents = mongoService.find(collection);
        return Response.ok(documents).build();
    }
        
    @GET
    @Timed
    @Path("{id}")
    public Response getCustomer(@PathParam("id") final String id) {
        Customer customer = mongoService.findById(collection, id);
        return Response.ok(customer).build();
    }

    @PUT
    @Timed
    @Path("{id}")
    public Response editCustomer(@PathParam("id") final String id, @NotNull @Valid final Customer customer) {
        mongoService.updateCustomer(collection, id, customer);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer with Name: " + customer.getFirstName()+ " updated successfully");
        return Response.ok(response).build();
    }

    @DELETE
    @Timed
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") final String id) {
        mongoService.deleteOne(collection, id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer with ID: " + id + " was deleted successfully");
        return Response.ok(response).build();
    }
}

