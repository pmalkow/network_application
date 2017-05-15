package pl.networkapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.networkapp.interceptors.PostRequestInterceptor;
import pl.networkapp.interceptors.ValidateUserInterceptor;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

	public static final String USER_ID_HEADER = "UserId";

	@Autowired private PostRequestInterceptor postRequestInterceptor;
	@Autowired private ValidateUserInterceptor validateUserInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(postRequestInterceptor).addPathPatterns("/post");
		registry.addInterceptor(validateUserInterceptor).addPathPatterns("/wall", "/followingFeed", "/follow/*");
	}

}
