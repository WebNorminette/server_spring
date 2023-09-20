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
                        .requestMatchers(HttpMethod.GET, "/home", "/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/members/signup", "/members/loginPage").permitAll()
                        // 위에 두줄은 삭제 예정
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/members/reissue").permitAll()
                        .anyRequest().authenticated()
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
