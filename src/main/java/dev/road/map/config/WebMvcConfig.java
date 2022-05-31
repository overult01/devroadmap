package dev.road.map.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// cors설정(리액트 연동)

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	private final long MAX_AGE_SECS = 3600;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		// 모든 경로 대상
		registry.addMapping("/**")
		// Origin이 http://localhost:3000에 대해서 get,put,delete,.. 메서드를 허용
		.allowedOrigins("http://localhost:3000")
		.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
		// 모든 헤더, 인증정보도 허용 
		.allowedHeaders("*") 
		.allowCredentials(true)
		.maxAge(MAX_AGE_SECS);
	}
}
