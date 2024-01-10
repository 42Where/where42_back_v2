package kr.where.backend.configuration;

import kr.where.backend.auth.exception.JwtAuthenticationEntryPoint;
import kr.where.backend.auth.filter.JwtFilter;
import kr.where.backend.auth.oauth2login.CustomOauth2UserService;
import kr.where.backend.auth.oauth2login.OAuth2FailureHandler;
import kr.where.backend.auth.oauth2login.OAuth2SuccessHandler;
import kr.where.backend.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity httpSecurity,
            final HandlerMappingIntrospector introspector) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(new MvcRequestMatcher(introspector, "/token/**"))
                                .permitAll()
                                .requestMatchers(new MvcRequestMatcher(introspector, "/oauth2/*"))
                                .permitAll()
                                .requestMatchers(new MvcRequestMatcher(introspector,"/swagger-ui/**"))
                                .permitAll()
                                .requestMatchers(new MvcRequestMatcher(introspector, "/v3/api-docs/**"))
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user.userService(customOauth2UserService))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                )
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
//                .logout(logout -> logout.clearAuthentication(true))

        return httpSecurity.build();
    }
}