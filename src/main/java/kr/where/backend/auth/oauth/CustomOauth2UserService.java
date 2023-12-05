package kr.where.backend.auth.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        final Map<String, Object> attributes = oAuth2User.getAttributes();

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();

        final OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(attributes);

        final Map<String, Object> map = mapChanger(oAuth2Attribute);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                map, "login");
    }

    private Map<String, Object> mapChanger(final OAuth2Attribute oAuth2Attribute) {
        Map<String, Object> result = new HashMap<>();

        result.put("login", oAuth2Attribute.getLogin());
        result.put("location", oAuth2Attribute.getLocation());
        result.put("image", oAuth2Attribute.getImage());
        result.put("create_at", oAuth2Attribute.getCreated_at());
        result.put("active", oAuth2Attribute.isActive());

        return result;
    }
}
