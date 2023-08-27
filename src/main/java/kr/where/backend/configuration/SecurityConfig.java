package kr.where.backend.configuration;

import kr.where.backend.auth.JwtToken.JwtFilter;
import kr.where.backend.auth.JwtToken.TokenProvider;
import kr.where.backend.auth.oauth.CustomOauth2UserService;
import kr.where.backend.auth.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class SecurityConfig {


    private final TokenProvider tokenProvider;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf()
                .disable()
                .formLogin()
                .disable();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/token").permitAll()
                        .requestMatchers("/oauth2/*").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        .requestMatchers("/login").permitAll()
//                                .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2Login()
                .successHandler(successHandler).userInfoEndpoint().userService(customOauth2UserService);
//                .successHandler(oAuth2AuthenticationSuccessHandler)
//                .failureHandler(oAuth2AuthenticationFailureHandler);
//        httpSecurity.logout()
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID");
//        httpSecurity.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}