Live Poll Application
A Spring Boot application for real-time polling using PostgreSQL, RabbitMQ, and WebSocket. Users can create polls, cast votes via REST API, and receive real-time vote tallies via WebSocket. Votes are processed asynchronously through RabbitMQ.
Features

Create polls with questions, options, and scheduled start times.
Cast votes via REST API, validated for poll start time and option validity.
Asynchronous vote processing with RabbitMQ.
Real-time vote tally updates via WebSocket.
PostgreSQL database for persistence.

Prerequisites

Java: 17 or higher
Maven: 3.6+
PostgreSQL: 13+ (running on localhost:5432)
RabbitMQ: 3.9+ (running on localhost:5672 with management plugin enabled on localhost:15672)
Node.js: 18+ (for wscat)
Operating System: Tested on macOS

Setup Instructions

Clone the Repository:
git clone https://github.com/InnocentOmenka/live-poll.git
cd live-poll


Install PostgreSQL:

Install via Homebrew (macOS):brew install postgresql@14
brew services start postgresql@14


Create database:psql -U postgres
CREATE DATABASE pollingdb;




Install RabbitMQ:

Install via Homebrew:brew install rabbitmq
brew services start rabbitmq


Enable management plugin:rabbitmq-plugins enable rabbitmq_management


Access at http://localhost:15672 (login: guest/guest).


Install Node.js and wscat:
brew install node
npm install -g wscat
wscat --version


Configure Application:

Edit src/main/resources/application.properties:spring.datasource.url=jdbc:postgresql://localhost:5432/pollingdb
spring.datasource.username=
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
logging.level.org.springframework=DEBUG
logging.level.com.innocodes.live_poll=DEBUG
logging.level.org.springframework.amqp=DEBUG
logging.level.org.springframework.web.socket=DEBUG
logging.file.name=/Users/<your-username>/logs/spring-boot.log


Create logs directory:mkdir logs




Build and Run:
mvn clean install
mvn spring-boot:run


Application runs on http://localhost:8080.



Assumptions

No authentication is required for APIs or WebSocket (no SecurityConfig or JWT).
WebSocket subscription uses {"pollId": "<id>"} format.
RabbitMQ uses vote-queue, vote-exchange, and routing key vote.
Database schema is auto-generated (spring.jpa.hibernate.ddl-auto=update).
Timezone is WAT (West Africa Time).

How to Test APIs & WebSockets
1. Create a Poll
   curl -X POST http://localhost:8080/polls \
   -H "Content-Type: application/json" \
   -d '{"question": "Best OS?", "options": ["Windows", "macOS", "Linux"], "scheduledStartTime": "2025-09-24T08:00:00Z"}'


Response: 201 Created with JSON containing id.
Verify: Check poll and poll_options tables:psql -U postgres -d pollingdb
SELECT * FROM poll;
SELECT * FROM poll_options;



2. Connect to WebSocket
   wscat -c ws://localhost:8080/ws


Subscribe to a poll:{"pollId": "<poll-id-from-step-1>"}


Verify: Check logs:cat /Users/<your-username>/logs/spring-boot.log | grep "WebSocket"



3. Cast a Vote
   curl -X POST http://localhost:8080/polls/<poll-id>/vote \
   -H "Content-Type: application/json" \
   -d '{"username": "user1", "option": "Windows"}'


Response: 202 Accepted.
Verify:
Database:psql -U postgres -d pollingdb
SELECT * FROM vote WHERE poll_id = '<poll-id>';


RabbitMQ: Open http://localhost:15672 (login: guest/guest), check vote-queue for messages.
WebSocket: In wscat, expect:{"id":"<poll-id>","question":"Best OS?","tally":{"Windows":1,"macOS":0,"Linux":0}}


WebSocket Disconnects:
Check logs for errors:cat /Users/<your-username>/logs/spring-boot.log | grep "ERROR"


Inspect PollWebSocketHandler.java and WebSocketService.java.


No RabbitMQ Messages:
Pause @RabbitListener in VoteService.java, cast vote, and check vote-queue.

Contributing

Fork the repository.
Create a feature branch (git checkout -b feature/<name>).
Commit changes (git commit -m "Add feature").
Push to GitHub (git push origin feature/<name>).
Open a pull request.
