Front End of this project: https://github.com/Veshon/AAD-Assignment-1-FrontEnd

Project Description

Overview
This project is a Point of Sale (POS) system designed for small to medium-sized retail business. The system is built using Java and Jakarta EE, leveraging APIs, AJAX for asynchronous communication, and Tomcat as the web server. The application interfaces with a MySQL database using JNDI for database connection management. The key components of the system include customer management, item catalog management, and order processing.

Features

Customer Management

The system allows users to create, update, delete, and view customer details.
Customer data includes basic information such as name, contact details, and customer ID.
The customer form is designed to interact with the backend via AJAX, providing a seamless and responsive user experience.

Item Management

Users can manage items available for sale, including adding new items, updating existing item details, and removing items from the catalog.
Each item has associated details such as item ID, name, description, price, and stock quantity.
AJAX is used to dynamically update item information on the frontend without reloading the page.

Order Processing

The order form facilitates the creation and management of sales orders.
Users can select items from the catalog, specify quantities, and generate orders for customers.
The system calculates total order amounts, applying any necessary taxes or discounts.
AJAX ensures that order details are dynamically updated as users interact with the form.
Technical Stack
Java and Jakarta EE: The core logic of the application is built using Java, with Jakarta EE providing the necessary enterprise features like dependency injection, RESTful web services, and persistence.

Tomcat: Apache Tomcat serves as the web server and servlet container, hosting the Jakarta EE application.

AJAX: Asynchronous JavaScript and XML (AJAX) are used to handle user interactions with the forms without requiring full-page reloads, improving the user experience.

JNDI and MySQL: Java Naming and Directory Interface (JNDI) is used for database connectivity. The application connects to a MySQL database where all customer, item, and order data is stored. JNDI provides a flexible and scalable way to manage database connections.

Conclusion
This POS system is a robust, web-based solution for managing customers, items, and orders in a retail environment. By leveraging Jakarta EE, AJAX, and Tomcat, the application provides a responsive and scalable platform for business operations, ensuring that users can manage their sales processes efficiently and effectively.
