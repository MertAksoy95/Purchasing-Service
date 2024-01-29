# Getting Started

### For Run

1. First, create a database named "purchasing" in your mysql database.

``` sql
CREATE DATABASE IF NOT EXISTS `purchasing` 
  DEFAULT CHARACTER SET = utf8mb4
  DEFAULT COLLATE = utf8mb4_turkish_ci;
}
```

2. Then, fill in the URL and credentials of your local mysql server through the application.yml file.

``` 
spring:
  datasource:
    url: jdbc:mysql://localhost/purchasing?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Istanbul
    username: root
    password: root
}
```

3. Open the project directory and run the ***mvn clean install*** command.

``` 
mvn clean install
``` 

4. Again, run the ***mvn spring-boot:run*** command in the project directory and let the application start.

``` 
mvn spring-boot:run
``` 

5. [You can access Swagger documentation here.](http://localhost:9100/purchasing/swagger-ui/index.html)
``` 
http://localhost:9100/purchasing/swagger-ui/index.html
```
