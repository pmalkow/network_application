# Simple social media application

Solution covers 4 APIs:

- post a message with maximum 140 characters
- load all messages posted by specified user
- follow another user
- load all messages posted by all the users one user follows

Assumptions:
- all data is stored in memory- as there is a generic interface for repository, switching storage should not affect functionality
- in case of posting a message if user does not exists, it is created automatically without any notification to the user

Technologies:
- application runs on SpringBoot
- unit tests are written in JUnit with Mockito framework
- system tests are written in Groovy with Spock framework- for high level understanding of this app please refer to SocianNetworkControllerSpec - given/when/then sections are documented and reflect business scenarios

API`s:
- /post - POST method; required header:'UserId'; body: message to post - if proper header is not found or is empty, BAD_REQUEST is returned; if user does not exists it is created automatically; if message is shorted the 1 character or longer then 140 characters, BAD_REQUEST is returned; success HTTP response code: CREATED
- /wall - GET method; required header:'UserId' - if proper header is not found or is empty, BAD_REQUEST is returned; if user to find feeds from does not exist, NOT_FOUND is returned; response contains all user`s messages in reversed chronological order; success HTTP response code: OK
- /follow/{user} - POST method; required header:'UserId'; parameter user: user to follow - if proper header is not found or is empty, BAD_REQUEST is returned; if user does not exists, NOT_FOUND is returned; if user to follow does not exist, NOT_FOUND is returned; success HTTP response code: CREATED
- /followingFeed - GET method; required header:'UserId' - if proper header is not found or is empty, BAD_REQUEST is returned; if user to find feeds from does not exist, NOT_FOUND is returned; response contains all messages from followed users in reversed chronological order; success HTTP response code: OK

Deployment/testing:
- application can be started by running main method in ServerApplication class
- all APIs are available within default port 8080
- testing can be done manually by submitting proper GET/POST request or by running SocialNetworkContollerSpec - it contains all business scenarios
- as it is a Gradle build application it is possible to execute verification task 'test' to run proper tests within app