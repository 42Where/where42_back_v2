package kr.where.backend.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuthUser implements OAuth2User {
    private String name;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; // oauthId, name, email

    public OAuth2Attribute toOAuth2Attribute() {
        Map<String, Object> image = (Map<String, Object>) attributes.get("image");
        String smallUrl = "";
        if (image != null) {
            Map<String, String> versions = (Map<String, String>) image.get("versions");
            if (versions != null) {
                smallUrl = versions.get("small");
            }
        }

        return OAuth2Attribute.builder()
                .id((Integer) attributes.get("id"))
                .login((String) attributes.get("login"))
                .location((String) attributes.get("location"))
                .image(smallUrl)
                .active(attributes.get("active") != null && (boolean) attributes.get("active"))
                .created_at((String) attributes.get("created_at"))
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
