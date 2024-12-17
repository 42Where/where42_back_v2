package kr.where.backend.auth.oauth2login;

import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberErrorCode;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    @Override
    public UserProfile loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        final Map<String, Object> attributes = oAuth2User.getAttributes();
        final String registrationId = userRequest.getClientRegistration().getRegistrationId();

        final Integer intraId = (Integer) attributes.get("id");
        final Optional<Member> member = memberRepository.findByIntraId(intraId);

        if (member.isPresent()) {
            return new UserProfile(
                    registrationId,
                    Stream.of("ROLE_" + member.get().getRole())
                            .map(SimpleGrantedAuthority::new)
                            .toList(),
                    attributes
            );
        }

        // UserProfile 객체 반환
        return new UserProfile(
                registrationId,
                Stream.of("ROLE_DISAGREE_USER")
                        .map(SimpleGrantedAuthority::new)
                        .toList(),
                attributes
        );
    }
}
