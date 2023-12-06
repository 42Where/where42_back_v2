package kr.where.backend.configuration;

import kr.where.backend.jwt.JwtFilter;
import kr.where.backend.auth.oauth.CustomOauth2UserService;
import kr.where.backend.auth.oauth.OAuth2FailureHandler;
import kr.where.backend.auth.oauth.OAuth2SuccessHandler;
import kr.where.backend.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class SecurityConfig {


    private final JwtService jwtService;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/token/**").permitAll()
                                .requestMatchers("/oauth2/*").permitAll()
                                .requestMatchers("/join").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.userInfoEndpoint(user -> user.userService(customOauth2UserService))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler))
                .logout(logout -> logout.clearAuthentication(true))
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}