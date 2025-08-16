 # Product Service - Spring Security Project

This project demonstrates a Spring Boot application with Spring Security, custom authentication, and Thymeleaf integration.

## Features

- **Spring Security**: Secures endpoints with authentication and authorization.
- **Custom Authentication**: Implements a custom authentication provider.
- **Thymeleaf**: Renders secure, dynamic HTML pages.
- **Configuration**: Uses `application.properties` for environment-specific settings.

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL (or compatible database)
- Dependent libraries:
    - `spring-boot-starter-security`
    - `spring-boot-starter-thymeleaf`
    - `spring-boot-starter-web`
    - `spring-boot-starter-data-jpa`
    - `mysql-connector-java`

## API Endpoints

| Method | Endpoint                | Description                        | Auth Required | Roles         |
|--------|-------------------------|------------------------------------|--------------|--------------|
| GET    | `/`                     | Home page                          | Yes          | -            |
| POST   | `/login`                | Authenticate user                  | Yes           | -            |         |
| GET    | `/productapi/products/{id}`  | Get product by ID                | Yes          | USER, ADMIN |
| POST   | `/productapi/products`       | Create new product               | Yes          | ADMIN       |

All endpoints returning HTML use Thymeleaf templates for rendering.

## Getting Started

1. **Clone the repository**
2. **Configure `application.properties`**  
    Set your database, security, and other properties as needed.

    ```properties
    # Example properties
    spring.datasource.url=jdbc:mysql://localhost:3306/product_db
    spring.datasource.username=your_db_user
    spring.datasource.password=your_db_password

    ``` 

3. **Run the application**

    ```bash
    ./mvnw spring-boot:run
    ```

4. **Access the app**  
    Visit [http://localhost:9090](http://localhost:9090)

## Security Overview

- Custom authentication logic is implemented in a dedicated provider/service.
- Login and registration pages are rendered with Thymeleaf.
- User roles and permissions are enforced at the controller level.

## Folder Structure

```
src/
 └── main/
      ├── java/
      │    └── ... (controllers, services, security)
      └── resources/
             ├── templates/   # Thymeleaf HTML files
             └── application.properties
```

## Custom Authentication

- Implement a custom `UserDetailsService` to load user-specific data.
- Configure an `AuthenticationManager` to use authentication provider and custom user details service in `SecurityConfig.java`.



