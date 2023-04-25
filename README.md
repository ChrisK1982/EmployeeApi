# Employee API Proxy in Springboot POC

> ## Technologies Used
> 
> - [Kotlin](https://kotlinlang.org/docs/home.html)
> - [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
> - [Springboot - Web, Security, Integrations, Dev Tools](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
> - [JUNIT](https://junit.org/junit5/docs/current/user-guide/)
> - [JPA H2 In-Memory DB](https://www.h2database.com/html/main.html)

This is a simple proxy API listening on `http:/localhost:8080`. The application can be started by running the
[Main Class](src/main/kotlin/com/example/employee_api/EmployeeApiApplication.kt) from within an IDE or of course
from cli, though I would recommend running the command below to build the jar first and then running the jar that is 
produced:

>   ```shell
>   # From repository root
>   $ sh gradlew clean build
>   ```

Here are a few sample commands to try once the server is running. You'll notice that basic auth credentials are needed:

```shell
# List all employees
curl -u user:password -v http://localhost:8080/api/employees

# Add an employee
curl -u 'user:password' -v http://localhost:8080/api/employees/create \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "Chris",
        "lastName": "Kraus",
        "email": "ckraus200982@gmail.com",
        "hireDate": "2023-04-23T17:44:11.137967",
        "department": "IT",
        "jobTitle": "Cloud Architect",
        "salary": 5500.00
      }'

# Add a batch of employees
curl -u 'user:password' -v http://localhost:8080/api/employees/batch/create \
  -H "Content-Type: application/json" \
  -d '[
        {
          "firstName": "Chris",
          "lastName": "Kraus",
          "email": "ckraus200982@gmail.com",
          "hireDate": "2023-04-23T17:44:11.137967",
          "department": "IT",
          "jobTitle": "Cloud Architect",
          "salary": 5500.00
        },
        {
          "firstName": "Bob",
          "lastName": "Dylan",
          "email": "beeDee@gmail.com",
          "hireDate": "2023-04-23T17:44:11.137967",
          "department": "ET",
          "jobTitle": "Musician",
          "salary": 10436.00
        }
      ]'

# Update a batch of employees
curl -u 'user:password' -v http://localhost:8080/api/employees/batch/update \
  -X PATCH \
  -H "Content-Type: application/json" \
  -d '[
        {
          "id": 1,
          "department": "Engineering",
          "jobTitle": "Data Engineer"
        },
        {
          "id": 2,
          "firstName": "Merl",
          "lastName": "Haggard"
        }
      ]'

# Patch an employee
curl -u 'user:password' -v http://localhost:8080/api/employees/update/1 \
  -H "Content-Type: application/json" \
  -X PATCH \
  -d '{
        "firstName": "James"
      }'

# BAD Add an employee due to the trailing json comma
curl -u 'user:password' -v http://localhost:8080/api/employees/create \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "Chris",
        "lastName": "Kraus",
        "email": "ckraus200982@gmail.com",
        "hireDate": "2023-04-23T17:44:11.137967",
        "department": "IT",
        "jobTitle": "Cloud Architect",
      }'

# Find an employee by id
curl -u user:password -v http://localhost:8080/api/employees/1
#Delete an employee by id
curl -u user:password -v http://localhost:8080/api/employees/1 -X DELETE
#Delete batch of employees
curl -u user:password -v http://localhost:8080/api/employees -X DELETE \
  -H "Content-Type: application/json" \
  -d '[1, 2]'
```

For further examples, see the various 
[Integration Tests](src/test/kotlin/com/example/employee_api/routers/AbstractEmployeeRouteControllerTest.kt)

For the purposes of demonstration, the API has general happy path integration tests. There are most likely still some
bugs to be worked out and given a bit of time, I would do a few things differently to help with downstream consumers and
to assure that data is persisted even when anomalies occur. With that said, this answers the requirements plus some
additional functionality.
