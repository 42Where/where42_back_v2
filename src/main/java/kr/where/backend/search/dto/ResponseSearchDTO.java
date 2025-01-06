package kr.where.backend.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.where.backend.group.entity.Group;
import kr.where.backend.member.Member;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "http response dto")
public class ResponseSearchDTO {

    @Schema(description = "카뎃의 고유 id")
    private Integer intraId;
    @Schema(description = "카뎃의 이름")
    private String intraName;
    @Schema(description = "카뎃의 이미지")
    private String image;
    @Schema(description = "카뎃의 상태메시지")
    private String comment;
    @Schema(description = "카뎃의 위치")
    private String location;
    @Schema(description = "카뎃의 incluster 상태")
    private boolean inOrOut;
    @Schema(description = "검색한 맴버와의 친구 여부")
    private boolean isFriend;
    @Schema(description = "서비스 동의 여부")
    private boolean isAgree;

    /**
     * @param searched 검색한 맴버에 대한 client 용 dto로 만듬 기본적인 고유 아이디, 이름, 이미지, 위치를 넣어주고,
     *                 만약 서비스 이용에 동의한 카뎃이라면, 나머지 목록들 첨부 동의 하지 않았다면, null로 내보내기.
     * @param group 검색 맴버 결과가 나와 친구인지 판별하기 위한 entity
     */
    @Builder
    public ResponseSearchDTO(final Group group, final Member searched) {
        this.intraId = searched.getIntraId();
        this.intraName = searched.getIntraName();
        this.image = searched.getImage();
        this.location = searched.getLocation().getLocation();
        this.isAgree = searched.isAgree();
        this.isFriend = false;

        if (this.isAgree) {
            this.comment = searched.getComment();
            this.inOrOut = searched.isInCluster();
            this.isFriend = group.isInGroup(searched);
        }
    }
}
