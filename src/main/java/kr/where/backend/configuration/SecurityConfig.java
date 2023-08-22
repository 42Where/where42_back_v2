package kr.where.backend.configuration;

import kr.where.backend.auth.JwtToken.JwtFilter;
import kr.where.backend.auth.JwtToken.TokenProvider;
import kr.where.backend.auth.oauth.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//    private final JwtFilter jwtFilter;
//
    private final TokenProvider tokenProvider;
    private final CustomOauth2UserService customOauth2UserService;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf()
//                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                .requestMatchers("login").permitAll()
//                .requestMatchers("/favicon.ico").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//
//    }
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
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/oauth2/*").permitAll()
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated());

        httpSecurity.oauth2Login().userInfoEndpoint().userService(customOauth2UserService);
//                .successHandler(oAuth2AuthenticationSuccessHandler)
//                .failureHandler(oAuth2AuthenticationFailureHandler);
//        httpSecurity.logout()
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID");
//        httpSecurity.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
//        return httpSecurity
//                .authorizeHttpRequests(
//                        authorize -> authorize
//                                .requestMatchers("/login").permitAll()
////                                .requestMatchers("/login").permitAll()
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
////                                .requestMatchers("/**").permitAll()
//                        .anyRequest()
//                        .authenticated()
////                ).exceptionHandling((exceptions) -> exceptions
////                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
////                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
////                )
//                )
////                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
    }
}