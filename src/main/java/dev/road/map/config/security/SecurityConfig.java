package dev.road.map.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dev.road.map.config.security.jwt.JwtAuthFilter;
import dev.road.map.config.security.oauth.CustomOAuth2UserService;
import dev.road.map.config.security.oauth.OAuth2SuccessHandler;
import dev.road.map.service.TokenService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터를 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;
    
	// login 페이지 변경
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.exceptionHandling().accessDeniedPage("/err/denied-page"); // 접근 불가 페이지
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS: 세션 미사용 설정 
		.and()
		
		.formLogin().disable() // formLogin 미사용
		.httpBasic().disable() // 기본 로그인 방식 미사용
		
        .oauth2Login()
        // .loginPage("token/expire")
        .defaultSuccessUrl("http://localhost:3000/")
        .successHandler(successHandler)
		.userInfoEndpoint() // oauth2Login 성공 이후의 설정을 시작
		.userService(customOAuth2UserService);
		
        http
//        .addFilterBefore(new JwtExceptionFilter(), OAuth2LoginAuthenticationFilter.class) // 모든 요청은 이 필터를 거친다.(CorsConfig에서 설정한 corsFilter)
        .addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
		
//		.addFilter(new JwtAuthenticationFilter(authenticationManager())) // UsernamePasswordAuthenticationFilter 재활성화
//		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository));

		// jwt사용시 여기까지는 기본 

		http.authorizeRequests().antMatchers("token/**", "/", "/main/**", "/login/**", "/static/**", "/logout/**").permitAll()
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 로그인한 admin만 접근 가능
				.antMatchers("/mail**").permitAll()
				.anyRequest().authenticated();

		http.logout().logoutSuccessUrl("/");

//		http.oauth2Login().defaultSuccessUrl("http://localhost:3000/") // oauth2 로그인
//				.userInfoEndpoint() // oauth2Login 성공 이후의 설정을 시작
//				.userService(customOAuth2UserService);
//
//		http.sessionManagement().invalidSessionUrl("/") // 유효하지 않은 세션 접근시 보낼 URL
//				.maximumSessions(1) // 중복 로그인 방지
//				.maxSessionsPreventsLogin(false);
//
//		http.sessionManagement().sessionFixation().migrateSession(); // 인증이 됐을 때 새로운 세션을 생성한뒤, 기존 세션의 attribute들을 복사
	}

	// 정적 파일 열기
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico", "/static/**", "/error", "/lib/**").mvcMatchers("/static/**")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

//	// 세션 변경
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

}
