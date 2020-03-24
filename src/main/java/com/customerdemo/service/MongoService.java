package com.customerdemo.service;

import com.customerdemo.domain.Customer;
import com.customerdemo.domain.CustomerMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import org.bson.types.ObjectId;

public class MongoService {

    public void insertCustomer(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    public List<Customer> find(MongoCollection<Document> collection) {
        final MongoCursor<Document> customers = collection.find().iterator();
		final List<Customer> customersFind = new ArrayList<>();
		
		try {
            while (customers.hasNext()) {
                final Document customer = customers.next();
                
                customersFind.add(CustomerMapper.map(customer));
            }
        } finally {
        	customers.close();
        }
        return customersFind;
    }

    public Customer findById(MongoCollection<Document> collection, String value) {
        Optional<Document> customerFind = Optional.ofNullable(collection.find(eq("_id", new ObjectId(value))).first());
        return customerFind.isPresent() ? CustomerMapper.map(customerFind.get()) : null;
    }
    
    public void updateCustomer(MongoCollection<Document> collection,String id,  Customer customer) {
        
        collection.updateOne(new Document("_id",new ObjectId(id)),
                new Document("$set", new Document("firstName", customer.getFirstName())
                .append("lastName", customer.getLastName())
                .append("email", customer.getEmail())
                .append("phoneNumber", customer.getPhoneNumber()))
        );
    }

    public void deleteOne(MongoCollection<Document> collection, String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
}
