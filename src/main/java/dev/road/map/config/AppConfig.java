package dev.road.map.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class AppConfig {
    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}

// 스프링 시큐리티는 root 컨텍스트에서 초기화 되고 mvc는 자식 컨텍스트에서 초기화 되므로 스프링 시큐리티가 자식놈인 mvc를 알 수 있는 방법이 없다.그래서 중간다리 역할을 해주는 HandlerMappingIntrospector가 필요 