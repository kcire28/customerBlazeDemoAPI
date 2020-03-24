package com.customerdemo.domain;

import org.bson.Document;

public class CustomerMapper {

	public static Customer map(final Document customerDocument) {
		final Customer customer = new Customer();
		customer.setId(customerDocument.getObjectId("_id"));
		customer.setFirstName(customerDocument.getString("firstName"));
		customer.setLastName(customerDocument.getString("lastName"));
		customer.setEmail(customerDocument.getString("email"));
		customer.setPhoneNumber(customerDocument.getString("phoneNumber"));
		return customer;
	}
}
