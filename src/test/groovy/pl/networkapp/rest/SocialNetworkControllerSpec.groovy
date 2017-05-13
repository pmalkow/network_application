package pl.networkapp.rest

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.servlet.HandlerExecutionChain
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import pl.networkapp.interceptors.PostRequestInterceptor
import pl.networkapp.interceptors.WallRequestInterceptor
import pl.networkapp.repository.UserRepository
import spock.lang.Specification

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocialNetworkControllerSpec extends Specification {

    @LocalServerPort int port
    @Autowired private UserRepository userRepository
    @Autowired private ApplicationContext applicationContext

    def userName = 'userName'
    def message1 = 'message'
    def message2 = 'another message'

    def appClient

    def setup() {
        appClient = new RESTClient("http://localhost:${port}")
        userRepository.delete(userName)
    }

    def 'should post messages and save them for a user'() {
        when: 'user sends message twice'
        def response1 = appClient.post(
                path: '/post',
                headers: ['UserId': userName],
                body: message1,
                requestContentType: ContentType.JSON
        )
        def response2 = appClient.post(
                path: '/post',
                headers: ['UserId': userName],
                body: message2,
                requestContentType: ContentType.JSON
        )

        then: 'messages are successfully saved'
        response1.status == 201
        response2.status == 201
        def user = userRepository.get(userName)
        user.isPresent()
        user.get().userName == userName
        user.get().messages.size() == 2
        user.get().messages.get(0).getMessage() == message1
        user.get().messages.get(1).getMessage() == message2
    }

    def 'should get feed of messages for one user'() {
        given: 'user with 2 messages posted'
        appClient.post(
                path: '/post',
                headers: ['UserId': userName],
                body: message1,
                requestContentType: ContentType.JSON
        )
        appClient.post(
                path: '/post',
                headers: ['UserId': userName],
                body: message2,
                requestContentType: ContentType.JSON
        )

        when: 'user tries to get his feed'
        def response = appClient.get(
                path: '/wall',
                headers: ['UserId': userName],
                requestContentType: ContentType.JSON
        )

        then: 'feeds are retrieved in reversed order'
        response.status == 200
        response.responseData.size() == 2
        response.responseData.get(0) == message2
        response.responseData.get(1) == message1

    }

    def 'should apply proper interceptors'(HttpMethod method, String path, Class[] interceptors) {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest(method.name(), path)
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class)

        when:
        HandlerExecutionChain chain = mapping.getHandler(request)

        then:
        chain.getInterceptors()
                .collect { interceptor -> interceptor.class }
                .containsAll(interceptors)

        where:
        method | path                            | interceptors
        POST   | '/post'                         | [PostRequestInterceptor]
        GET    | '/wall'                         | [WallRequestInterceptor]
    }

}
