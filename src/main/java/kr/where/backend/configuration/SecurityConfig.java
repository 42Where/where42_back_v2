package kr.where.backend.configuration;

import kr.where.backend.auth.JwtToken.JwtFilter;
import kr.where.backend.auth.JwtToken.TokenProvider;
import kr.where.backend.auth.oauth.CustomOauth2UserService;
import kr.where.backend.auth.oauth.OAuth2FailureHandler;
import kr.where.backend.auth.oauth.OAuth2SuccessHandler;
import kr.where.backend.jwt.JsonWebTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class SecurityConfig {


    private final JsonWebTokenService jsonWebTokenService;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity httpSecurity,
            final HandlerMappingIntrospector introspector) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
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
                                .requestMatchers(new MvcRequestMatcher(introspector, "/v3/**"))
                                .permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.userInfoEndpoint(user -> user.userService(customOauth2UserService))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler))
                .logout(logout -> logout.clearAuthentication(true))
                .addFilterBefore(new JwtFilter(jsonWebTokenService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}