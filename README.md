# sidenisApp
App represents a RESTful API for money transfers between accounts, and managing accounts themselves. Data stored in memory.

Tech: Spring, JUnit4

Swagger can be found on standard /swagger-ui.html# endpoint

## Building

Run 'mvn clean package' to build an executable war file, then put in webapps folder inside Tomcat directory and run Tomcat. You can add '-DskipTests' for skipping tests.

## Endpoints

POST /account - creates a new account with name and initial amount of money
GET /account/{id} - provides detailed info about account with given ID
DELETE /account/{id} - marks account as inactive, thus remove from the money transfer processing
GET /accounts - returns a list of IDs of all active accounts
POST /transfer - processes money transfer from one user, to another

/swagger-ui.html - standard Swagger endpoint

## Testing

You can check MyControllerTest.java for API tests that shows functionality of an app.

Tests are:
* new account creation 
* account deletion
* list of all accounts in system
* detailed info about an account
* parallel money transfer between two accounts
