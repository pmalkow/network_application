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
import pl.networkapp.repository.UserRepository
import spock.lang.Specification

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
    }

    def 'should post messages and save them for a user'() {
        when:
        def response1 = appClient.post(
                path: '/post',
                headers: ['UserId': userName],
                body: ['message' : message1],
                requestContentType: ContentType.JSON
        )
        def response2 = appClient.post(
                path: '/post',
                headers: ['UserId': userName],
                body: ['message' : message2],
                requestContentType: ContentType.JSON
        )

        then:
        response1.status == 200
        response2.status == 200
        def user = userRepository.get(userName)
        user.isPresent()
        user.get().userName == userName
        user.get().messages.size() == 2
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
    }
}
