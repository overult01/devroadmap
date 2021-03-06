package dev.road.map.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.road.map.config.CorsConfig;
import dev.road.map.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsUtils;

@SuppressWarnings("deprecation")
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터를 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    
    @Autowired
	private CorsConfig corsConfig;

//    @Autowired
//    CorsFilter corsFilter;
    
	@Value("${frontDomain}")
	String frontDomain;
    
	@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	// login 페이지 변경
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(corsConfig.corsFilter());
//		http.addFilter(corsFilter);

//		.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userRepository, tokenProvider), UsernamePasswordAuthenticationFilter.class);

		http.csrf().disable()
				.exceptionHandling().accessDeniedPage("/err/denied-page"); // 접근 불가 페이지
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS: 세션 미사용 설정 
		.and()
		.cors()
		
		.and()
		.formLogin().disable() // formLogin 미사용
		.httpBasic().disable(); // 기본 로그인 방식 미사용
        
		http.formLogin()
		.loginPage(frontDomain + "/signin")
        .defaultSuccessUrl(frontDomain);
		// jwt사용시 여기까지는 기본 

		http.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**/*").permitAll() // preflight
			.antMatchers("/jwt/**", "/signup/**", "/signin/**", "/", "/main/**", "/static/**", "/logout/**").permitAll()
//			.antMatchers("/user/details/**", "/mail/**", "/history/**", "/friend/**", "").permitAll()
			.antMatchers(HttpMethod.GET, "/**/*").permitAll()
				.antMatchers(HttpMethod.POST, "/**/*").permitAll()
			.anyRequest().authenticated();
			// preflight
//			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

//		http.logout().logoutSuccessUrl("/");

//		http.oauth2Login().defaultSuccessUrl("http://localhost:3000/") // oauth2 로그인
//				.userInfoEndpoint() // oauth2Login 성공 이후의 설정을 시작
//				.userService(customOAuth2UserService);
//
//		http.sessionManagement().invalidSessionUrl("/") // 유효하지 않은 세션 접근시 보낼 URL
//				.maximumSessions(1) // 중복 로그인 방지
//				.maxSessionsPreventsLogin(false);
//
		http.sessionManagement().sessionFixation().migrateSession(); // 인증이 됐을 때 새로운 세션을 생성한뒤, 기존 세션의 attribute들을 복사
	}

	// 정적 파일 열기
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico", "/static/**", "/error", "/lib/**").mvcMatchers("/static/**")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

}
