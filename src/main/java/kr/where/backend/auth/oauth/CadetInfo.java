package kr.where.backend.auth.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CadetInfo {
    private Integer id;
    private String login;
    private String location;
    private String image;
    private boolean active;
    private String created_at;

    public static CadetInfo of(final Map<String, Object> attributes) {
        Map<String, Object> image = (Map<String, Object>) attributes.get("image");
        String smallUrl = "";
        if (image != null) {
            Map<String, String> versions = (Map<String, String>) image.get("versions");
            if (versions != null) {
                smallUrl = versions.get("small");
            }
        }

        return CadetInfo.builder()
                .id((Integer) attributes.get("id"))
                .login((String) attributes.get("login"))
                .location((String) attributes.get("location"))
                .image(smallUrl)
                .active(attributes.get("active") != null && (boolean) attributes.get("active"))
                .created_at((String) attributes.get("created_at"))
                .build();
    }
}
