package com.webnorm.prototypever1.config;

import com.webnorm.prototypever1.security.filter.ExceptionHandlerFilter;
import com.webnorm.prototypever1.security.filter.JwtAuthenticationFilter;
import com.webnorm.prototypever1.security.handler.AccessDeniedHandler;
import com.webnorm.prototypever1.security.handler.CustomAuthenticationEntryPoint;
import com.webnorm.prototypever1.security.handler.OAuthLoginSuccessHandler;
import com.webnorm.prototypever1.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 보안 설정
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // OAuth 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuthLoginSuccessHandler)
                )
                // api request 설정
                .authorizeHttpRequests(request -> request
                        // 두줄은 삭제 예정
                        .requestMatchers(HttpMethod.GET, "/", "/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/members/signup", "/members/loginPage", "/addressPage").permitAll()
                        // 전체 공개
                        .requestMatchers(HttpMethod.POST, "/api/members", "/api/members/login").permitAll()     // 회원가입, 로그인
                        .requestMatchers(HttpMethod.GET, "/api/members/reissue").permitAll()                    // 토큰 재발급
                        .requestMatchers(HttpMethod.POST, "/email/password/**").permitAll()                     // 임시 비밀번호 메일 발송
                        .requestMatchers(HttpMethod.GET, "/collections/**").permitAll()                         // 상품 목록 조회
                        .requestMatchers(HttpMethod.POST, "/addresses").permitAll()                             // 주소 추가
                        // 관리자만 허용
                        .requestMatchers(HttpMethod.GET, "/api/members").hasAuthority("ADMIN")                  // 회원 목록 조회
                        .requestMatchers(HttpMethod.POST, "/collections").hasAuthority("ADMIN")                 // 카테고리 추가
                        .requestMatchers(HttpMethod.PUT, "/collections/**").hasAuthority("ADMIN")               // 카테고리 수정
                        .requestMatchers(HttpMethod.DELETE, "collections/**").hasAuthority("ADMIN")             // 카테고리 삭제
                        .requestMatchers(HttpMethod.POST, "/collections/**/products").hasAnyAuthority("ADMIN")  // 상품 추가
                        .requestMatchers(HttpMethod.POST, "/collections/products/img").hasAnyAuthority("ADMIN") // 상품 이미지 추가
                        // 관리자, 사용자 허용
                        .anyRequest().hasAnyAuthority("ADMIN", "USER")
                )
                // filter, handler 설정
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
