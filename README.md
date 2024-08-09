# ✅ Jakarta EE Web POS System

**Java Version:** 17  
**Frontend:** HTML, CSS, JavaScript, AJAX, Bootstrap, jQuery  
**Backend:** Jakarta EE, Gson, Yasson, Lombok, MySQL Connector, Jakarta Servlet, HTTP Methods, Tomcat 10

## Overview

The ✅ Jakarta EE Web POS System is a robust Point of Sale (POS) application specifically designed for a flower shop. This system streamlines the management of customer orders, inventory, and transactions, ensuring efficient operations. The frontend is implemented with modern web technologies like HTML, CSS, JavaScript, and Bootstrap to provide a responsive and user-friendly interface. The backend is powered by Jakarta EE, utilizing the power of enterprise-level Java to manage business logic, database interactions, and server communication.

## Project Structure

The project is structured to follow best practices in software development, employing a layered architecture to ensure a clean separation of concerns. This architecture facilitates maintainability, scalability, and ease of understanding by dividing the system into distinct layers, each responsible for a specific aspect of the application, such as presentation, business logic, and data access.

### Key Directories:
- **src/main/java:** Contains the core Java code, including DAO, DTO, service, and servlet layers.
- **src/main/webapp:** Includes configuration files like `web.xml` and `context.xml`.

## Running the Project

1. **Clone the Repository:**
   
   ```bash
    https://github.com/sandeeparavindi/Web-POS-Backend-.git
   
3. **Configure the Database:**
- Create a MySQL database named flower.
- Update the src/main/webapp/META-INF/context.xml with your database credentials.
  
3. **Build and Deploy:**
- Use Maven to build the project
- Deploy the war file to Tomcat 10

4. **Access the Application:**
- Open your web browser and navigate to http://localhost:8080/Flowers_war_exploded.

## Database Configuration

- Database: [Your Database Name]
- Username: [Your Database Username]
- Password: [Your Database Password]

## Important Notes

- Ensure you are using Java version 17 and Tomcat 10 for compatibility.
- Use Lombok to reduce boilerplate code in DTOs and services.
- Properly handle exceptions and errors to ensure a smooth user experience.

## License

### [MIT](./LICENSE) License 

### [API Documentation](https://documenter.getpostman.com/view/35385607/2sA3s3GAYx)

## Acknowledgments

- Jakarta EE: For providing a powerful platform for enterprise-level applications.
- Bootstrap: For the responsive and sleek frontend design.
- MySQL: For reliable and efficient database management.

## Copyright

Copyright (c) 2024 SANDEEPA RAVINDI. All rights reserved.
