package com.customerdemo.resource;

import com.customerdemo.domain.Customer;
import com.customerdemo.service.MongoService;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
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

        mongoService.insertOne(collection, new Document(BasicDBObject.parse(json)));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer created successfully");
        return Response.ok(response).build();
    }

    @POST
    @Timed
    @Path("/createCustomers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomers(@NotNull final List<Customer> customers) {
        List<Document> customerDocuments = new ArrayList<>();
        Gson gson = new Gson();
        String json;
        for (Customer customer : customers) {
            json = gson.toJson(customer);
            customerDocuments.add(new Document(BasicDBObject.parse(json)));
        }
        mongoService.insertMany(collection, customerDocuments);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customers created successfully");
        return Response.ok(response).build();
    }

    @GET
    @Timed
    public Response getCustomers() {
        List<Document> documents = mongoService.find(collection);
        return Response.ok(documents).build();
    }

    @GET
    @Timed
    @Path("{name}")
    public Response getCustomer(@PathParam("name") final String name) {
        List<Document> documents = mongoService.findByKey(collection, "name", name);
        return Response.ok(documents).build();
    }

    @GET
    @Timed
    @Path("/salary/sort")
    public Response getCustomer() {
        List<Document> documents = mongoService.findByCriteria(collection, "salary",
                25000, 1000, 1);
        return Response.ok(documents).build();
    }

    @PUT
    @Timed
    public Response editCustomer(@NotNull @Valid final Customer customer) {
        mongoService.updateOneCustomer(collection, "name", "department", "salary", customer);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer with Name: " + customer.getFirstName()+ " updated successfully");
        return Response.ok(response).build();
    }

    @DELETE
    @Timed
    @Path("{name}")
    public Response deleteCustomer(@PathParam("name") final String name) {
        mongoService.deleteOne(collection, "name", name);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer with Name: " + name + " deleted successfully");
        return Response.ok(response).build();
    }
}

