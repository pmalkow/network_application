package pl.networkapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.networkapp.interceptors.FollowRequestInterceptor;
import pl.networkapp.interceptors.PostRequestInterceptor;
import pl.networkapp.interceptors.WallRequestInterceptor;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

	public static final String USER_ID_HEADER = "UserId";
	public static final String USER_TO_FOLLOW_PARAMETER = "user";

	@Autowired private PostRequestInterceptor postRequestInterceptor;
	@Autowired private WallRequestInterceptor wallRequestInterceptor;
	@Autowired private FollowRequestInterceptor followRequestInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(postRequestInterceptor).addPathPatterns("/post");
		registry.addInterceptor(wallRequestInterceptor).addPathPatterns("/wall");
		registry.addInterceptor(followRequestInterceptor).addPathPatterns("/follow/*");
	}

}
