package com.customerdemo;

import com.customerdemo.resource.CustomerServiceHealthCheckResource;
import com.customerdemo.resource.CustomerResource;
import com.customerdemo.service.MongoService;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.bson.Document;
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
        MongoClient mongoClient = new MongoClient(config.getMongoHost(), config.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        env.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(config.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(config.getCollectionName());
        
        // REGISTER APIS 
        env.jersey().register(new CustomerResource(collection, new MongoService()));
        env.healthChecks().register("CustomerServiceHealthCheck",
                new CustomerServiceHealthCheckResource(mongoClient));
    }
}
