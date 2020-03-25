package com.customerdemo;

import com.customerdemo.resource.CustomerServiceHealthCheckResource;
import com.customerdemo.resource.CustomerResource;
import com.customerdemo.service.MongoService;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.bson.Document;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomerServiceApplication extends Application<CustomerServiceConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceApplication.class);

    public static void main(String[] args) throws Exception {
        new CustomerServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<CustomerServiceConfiguration> b) {
    }

    @Override
    public void run(CustomerServiceConfiguration config, Environment env)
            throws Exception {
        
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://"+config.getMongoUserName()+":"+config.getMongoPassword()+"@"+config.getMongoHost()+":"+config.getMongoPort()+"/"+config.getMongoDB()));
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        env.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(config.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(config.getCollectionName());

        // REGISTER APIS 
        env.jersey().register(new CustomerResource(collection, new MongoService()));
        
        //enable cors:
        final FilterRegistration.Dynamic cors =
            env.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        //cors.setInitParameter("allowedMethods", "*");
        
         // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        env.healthChecks().register("CustomerServiceHealthCheck",
                new CustomerServiceHealthCheckResource(mongoClient));
    }
}
