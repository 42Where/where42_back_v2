package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "42seoul opnen API에 요청한 카뎃 정보")
public class CadetPrivacy {
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

    //create for Test
    public static CadetPrivacy createForTest(Integer id, String login, String location, String small_image, boolean active, String craeated_at) {
        CadetPrivacy cadetPrivacy = new CadetPrivacy();

        cadetPrivacy.id = id;
        cadetPrivacy.login = login;
        cadetPrivacy.image = Image.createForTest(Versions.createForTest(small_image));
        cadetPrivacy.location = location;
        cadetPrivacy.active = active;
        cadetPrivacy.created_at = craeated_at;

        return cadetPrivacy;
    }
}
