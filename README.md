# Event-Ticket-Booking

**Application architecture**

![Design](https://github.com/user-attachments/assets/728378e9-88e3-4ca6-b841-941c5dd6d2d1)

**Swagger UI**
<br/><br/>
User Service	        http://localhost:9000/swagger-ui/index.html
<br/>
Event Service	        http://localhost:8000/swagger-ui/index.html
<br/>
Notification Service	http://localhost:7000/swagger-ui/index.html
<br/>
<br/>


**Import Postman API Specifications to POSTMAN application and follow below steps to launch and test the applications.**

**Step 1 : Launch Eureka Server <br/>**
Eureka server provides server registry to discover and locate application instances.

**Step 2 : Launch Api Gateway <br/>**
Api Gateway provides gateway interface for API calls. Using API Gateway we can implement cross cutting concerns security,logging etc.
Sample generated emails

**Step 3 : Launch User Service <br/>**
User service acts as identity and access management. User Service provides features to search,create,update users.
Also it provides features to retreive and cancel the user's tickets.

**Step 4 : Launch Event Service <br/>**
Event Service manages the events by providing the features to search,update,create events.
Also it provides features to create tickets for the users and to cancel events.

**Step 4 : Launch Notification Service <br/>**
Notification Service manages notifications of users by sending email to users post ticket purchased/cancelled .

**Step 5 : Testing Application <br/>**
Execute below API in postman to test the flow for registering user, creating event followed by booking ticket. Post ticket creation , email will be sent as notification to customer.

Registers new user  <br/>
**POST API** : http://localhost:8765/user-service/user    
 Creates new event <br/> 
**POST API** : http://localhost:8765/event-service/event <br/>
Purchases the tickets for the user and email will be sent as notification <br/>
**POST API** : http://localhost:8765/event-service/event/1/tickets   
<br/>
![Ticket Booking Email](https://github.com/user-attachments/assets/c5c138f1-d7e8-4216-92b5-98f303d7762b)

Execute below API in postman to test the flow for cancelling the tickets by user and event cancellation . Post ticket/event cancellation , email will be sent as notification to customer.

Ticket cancellation by user   <br/>
**DELETE API** : http://localhost:9000/user/1/event/1/tickets  
Event cancellation    <br/>
**DELETE API** : http://localhost:8765/event-service/event/1?userId=1
<br/>


![Ticket Cancelling Email](https://github.com/user-attachments/assets/101adf1f-a1fd-461d-ade3-67b214c395cc)














