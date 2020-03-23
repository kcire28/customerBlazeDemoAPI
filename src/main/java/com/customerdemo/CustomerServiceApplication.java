package com.customerdemo;

import com.customerdemo.resources.CustomerResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CustomerServiceApplication extends Application<CustomerServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CustomerServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "CustomerService";
    }

    @Override
    public void initialize(final Bootstrap<CustomerServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final CustomerServiceConfiguration configuration,
                    final Environment environment) {
        
        //Resources
        CustomerResource customerResource = new CustomerResource();
        environment.jersey().register(customerResource);
    }

}
