package team3.boogie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**")
			.addResourceLocations("classpath:/templates/css/");

		registry.addResourceHandler("/js/**")
			.addResourceLocations("classpath:/static/");

		registry.addResourceHandler("/img/**")
			.addResourceLocations("classpath:/templates/img/");
	}
}
