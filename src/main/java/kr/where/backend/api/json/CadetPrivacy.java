package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "42seoul opnen API에 요청한 카뎃 정보")
@AllArgsConstructor
public class CadetPrivacy {
    private final static Integer SEOUL_CAMPUS_ID = 29;
    @Schema(description = "카뎃의 고유 intra id")
    private Integer id;
    @Schema(description = "카뎃 Intra 아이디")
    private String login;
    @Schema(description = "카뎃의 클러스터 위치")
    private String location;
    @Schema(description = "카뎃 이미지 URL")
    private Image image;
    @JsonProperty("active?")
    @Schema(description = "카뎃의 블랙홀 상태")
    private boolean active;
    @Schema(description = "42서울 등록일")
    private String created_at;
    @Schema(description = "캠퍼스 소속")
    private Integer campus;
    protected CadetPrivacy() {}

    @Builder
    public CadetPrivacy(
            final Integer id, final String login, final String location,
            final String small_image, final boolean active, final String created_at,
            final Integer campusId
    ) {
        this.id = id;
        this.login = login;
        this.location = location;
        this.image = Image.create(Versions.create(small_image));
        this.active = active;
        this.created_at = created_at;
        this.campus = campusId;
    }

    public static CadetPrivacy of(final Map<String, Object> attributes) {
        Map<String, Object> image = (Map<String, Object>) attributes.get("image");
        String smallUrl = "";
        if (image != null) {
            Map<String, String> versions = (Map<String, String>) image.get("versions");
            if (versions != null) {
                smallUrl = versions.get("small");
            }
        }
        List<Map<String, Object>> campus = (List<Map<String, Object>>) attributes.get("campus");

        return CadetPrivacy.builder()
                .id((Integer) attributes.get("id"))
                .login((String) attributes.get("login"))
                .location((String) attributes.get("location"))
                .small_image(smallUrl)
                .active(attributes.get("active") != null && (boolean) attributes.get("active"))
                .created_at((String) attributes.get("created_at"))
                .campusId((Integer) campus.get(0).get("id"))
                .build();
    }

    public void setSeoulCampus() {
        this.campus = SEOUL_CAMPUS_ID;
    }
}
