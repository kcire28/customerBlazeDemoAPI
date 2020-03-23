/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.customerdemo.resources;
import com.customerdemo.api.Customer;
import com.customerdemo.db.CustomerDAO;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Erick
 */
@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    CustomerDAO customerDao = new CustomerDAO();
    
    //get
    @GET
    public Response getAllCustomers(){
        return Response.ok(this.customerDao.getAllCustomers()).build();
    }
    /*
    @GET
    @Path("{id}")
    public Response findCustomer(@PathParam("id") int id){
        //Customer customer = new Customer(5, "Erick", "Urbano", "eurbano@znerg.com", "12345678");
        Customer response = customerDao.getById(id);
        
        if(response == null){
            throw new WebApplicationException("Customer not found");
        }
        
        return Response.ok(response).build();
    }
    
    @POST
    @Path("{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer){ 
        return Response.ok(customer).build();
    }
    
    @POST
    public Response createCustomer(Customer customer){
        Customer response = this.customerDao.insertCustomer(customer);
        return Response.ok(response).build();
        //Response.noContent().build();
    }
    */
}
