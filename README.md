# CustomerService

How to start the CustomerService application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/CustomerService-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


Generate MongoDB test data
--

To generate the mongoDB data use the mgeneratejs package `https://www.npmjs.com/package/mgeneratejs`
The template.json file has the fields of the customer which will be generated
`{"firstName": "$first", "lastName": "$last", "email": "$email", "phoneNumber":"$phone"}`

1. Run `npm install -g mgeneratejs`
2. Replace the number of customers to generate, the host, the port, the db name and the collection name in the command
	`mgeneratejs template.json -n 10000 | mongoimport --uri mongodb://localhost:27017/dbName 	--collection collectionName --mode insert `
3. Run the command

