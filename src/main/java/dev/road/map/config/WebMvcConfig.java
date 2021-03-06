package dev.road.map.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// cors설정(리액트 연동)

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	private final long MAX_AGE_SECS = 3600;
	
	@Value("${frontDomain}")
	String frontDomain;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
	 System.out.println("WebMvcConfig");
		// 모든 경로 대상
		registry.addMapping("/**")
		// Origin이 http://localhost:3000에 대해서 get,put,delete,.. 메서드를 허용
		.allowedOrigins(frontDomain)
		.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
		.exposedHeaders("*") 
		// 모든 헤더, 인증정보도 허용 
		.allowedHeaders("*") 
		.allowCredentials(true)
		.maxAge(MAX_AGE_SECS); // // 3600초 동안 preflight 결과를 캐시에 저장
	}
}
