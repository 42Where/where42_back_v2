package kr.where.backend.configuration;

import java.util.List;
import kr.where.backend.auth.filter.exception.CustomAccessDeniedHandler;
import kr.where.backend.auth.filter.JwtExceptionFilter;
import kr.where.backend.auth.filter.JwtFilter;
import kr.where.backend.auth.oauth2login.CustomOauth2UserService;
import kr.where.backend.auth.oauth2login.OAuth2FailureHandler;
import kr.where.backend.auth.oauth2login.OAuth2SuccessHandler;
import kr.where.backend.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Collections;

/**
 * spring security 설정 config
 * @author parksuhwan
 *
 * 42api를 통한 기존의 수동 로그인을 OAuth2을 통해 자동적으로 실행하는 oauth2 login 적용
 * session, cookie를 사용하지 않고 stateless하게 사용하기 위해 jwt 적용
 * flow : 모든 api 요청 -> security filter를 통한 인가, 인증 진행 -> http header에 jwt 가 있다면
 * custom filter 사용, 없다면 OAuth2 login 사용을 통한 token 발급
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    /**
     *
     * @param httpSecurity : security를 사용할 떄 옵션 적용. 인증인가 실행할때 예외 api 설정, filter 순서 설정을 위한 param
     * @param introspector : mvc를 사용하기에 MvcRequestMatcher를 사용하기 위한 param
     * @see #securityFilterChain(HttpSecurity, HandlerMappingIntrospector)
     * .csrf(custom -> custom.configurationSource(request....))
     *                 cors Error에 대한 설정을 해준다.
     *                 setAllowedOriginPatterns() Spring Security 6.x 이상 사용할 때는 setAllowedOrigin()은 사용하지 않는다.
     *                 : 접근 허용한 Origins URL을 set 해준다.
     *                 setAllowedMethods() : 허용 Method를 설정
     *                 setAllowCredentials() : 쿠키 사용한다면 true로 설정
     *                 setExposedHeaders() : clients에게 보여줄 header 설정
     *                 setAllowedHeaders() : 허용 header를 설정
     *                 setMaxAge() : preflight 요청 결과를 설정한 시간동안 캐시에 저장 (그 시간에는 prflight를 확인하지 않음)
     *  .formLogin(AbstractHttpConfigurer::disable)
     *                 custom한 jwtFilter를 적용하기 위해 기존의 formLogin은 사용하지 않는다
     *  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     *                 session은 사용하지 않는다.
     *  .authorizeHttpRequests()
     *                  authorizeHttpRequest ? -> http 요청에 대한 설정을 구성하는 것 이를 통해 다양한 인가 규칙 및 경로별 권한 설정 가능
     *                  requestmathcer : 특정 경로나 URL의 패턴에 대한 인가 규칙을 설정
     *                  permitAll : 특정 uri를 허용하는 함수, where42에서는 oAuth로그인 관련, swagger 관련만 열어 놓는다
     *                  anyRequest().authenticated() : 다른 접근은 다 인증을 필요로한다
     *  .oauth2Login()
     *                   OAuth 로그인을 실행
     *                  userInfoEndpoint을 실행 할때, 유저정보를 저장한다.
     *                  successHandler 로그인을 성공했을 때, 실행하는 로직
     *                  failureHandler 로그인 실패 했을 때, 실행하는 로직
     *  .logout(logout -> logout.clearAuthentication(true))
     *                 로그아웃을 하면 security context holder의 정보를 지운다
     *  .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
     *                  oauth 로그인을 하기전에 where42 service에서 인가인증 받은 token이 있다면, token으로 인가인증 실행 하는 filter
     *  .addFilterBefore(jwtExceptionFilter, JwtFilter.class);
     *                  jwtfilter를 실행할 때 token에 대한 예외 처리를 처리하는 filter
     *
     * @return HttpSecurity : custom한 security 설정 반환
     * @throws Exception : 예외 던짐
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity httpSecurity,
            final HandlerMappingIntrospector introspector) throws Exception {

        final MvcRequestMatcher.Builder requestMatcher = new MvcRequestMatcher.Builder(introspector);

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(custom -> custom.configurationSource(request -> {
                    final CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOriginPatterns(Collections.singletonList("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setExposedHeaders(List.of("x-amz-server-side-encryption", "x-amz-request-id", "x-amz-id-2"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(CorsUtils::isPreFlightRequest)
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/oauth2/**"))
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/swagger-ui/**"))
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/v3/api-docs/**"))
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/v3/token"))
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/actuator/prometheus"))
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/v3/jwt/reissue"))
                                .permitAll()
                                .requestMatchers(requestMatcher.pattern("/v3/announcement"))
                                .hasRole("ADMIN")
//                                .requestMatchers(requestMatcher.pattern("/v3/announcement"))
//                                .hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()

                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user.userService(customOauth2UserService))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                )
                .logout(logout -> logout.clearAuthentication(true))
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .exceptionHandling(exception-> exception.accessDeniedHandler(accessDeniedHandler))
                .build();
    }
}