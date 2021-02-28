# codechallenge - be

Build:
````
./mvnw clean package
````
Run:
````
java -jar target/codechallenge-be-1.0.0.jar
````
Docker:
````
docker build -t codechallenge:1.0.0 .
docker run --name codechallenge -p 8080:8080 codechallenge:1.0.0 -d
````
Verify that service is running:
````
http://localhost:8080
````
