package kr.where.backend.auth.oauth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Builder
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeId;

    private Integer id;
    private String login;
    private String location;
    private LinkedHashMap image;
    private boolean active;
    private String created_at;
    public static OAuth2Attribute of(Map<String, Object> attributes) {

        return OAuth2Attribute.builder()
                .id((Integer) attributes.get("id"))
                .login((String) attributes.get("login"))
                .location((String) attributes.get("location"))
                .image((LinkedHashMap) attributes.get("image"))
                .active(attributes.get("active") != null && (boolean) attributes.get("active"))
                .created_at((String) attributes.get("created_at"))
                .build();
    }
}
