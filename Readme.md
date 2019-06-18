## Spring Boot File Upload / Download Rest API Example

**Tutorial**: [])

## Steps to Setup

**1. Clone the repository** 

```bash
git clone https://github.com/arun406/hackathon.git
```

**2. Specify the file uploads directory**

Open `src/main/resources/application.properties` file and change the property `file.upload-dir` to the path where you want the uploaded files to be stored.

```
file.upload-dir=/Users/mercator/hackathon/images
```

**3. Change the mongoDb and ports in the *.property files.

```
spring.data.mongodb.uri: mongodb://airline_user:123@localhost:27017/airline
server.port=8080

```
**4. build the app using maven**

```bash
./mvnw clean packagRe -DskipTests
```
**5. Run airline app

```
   java -jar hackathon-0.0.1-SNAPSHOT.jar -Dspring.config.location=./airline.properties

```

**6. run the gha app

java -jar hackathon-0.0.1-SNAPSHOT.jar -Dspring.config.location=./gha.properties


That's it! The airline application can be accessed at `http://localhost:8080`.

And The gha application can be accessed at `http://localhost:9090`.
