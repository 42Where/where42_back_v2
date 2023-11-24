package kr.where.backend.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.where.backend.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "http response dto")
public class ResponseSearch {

    @Schema(description = "카뎃의 고유 id")
    private Long intraId;
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
     * @param member 검색한 맴버에 대한 client 용 dto로 만듬 기본적인 고유 아이디, 이름, 이미지, 위치를 넣어주고, 만약 서비스 이용에 동의한 카뎃이라면, 나머지 목록들 첨부 동의 하지
     *               않았다면, null로 내보내기.
     */
    @Builder
    public ResponseSearch(final Member member) {
        this.intraId = member.getIntraId();
        this.intraName = member.getIntraName();
        this.image = member.getImage();
        this.location = member.getLocation().getLocation();
        this.isAgree = member.isAgree();

        if (this.isAgree) {
            this.comment = member.getComment();
            this.inOrOut = member.isInCluster();
            //이거는 메서드 만들어보자
            this.isFriend = friend;
        }
    }

    /**
     * 두번째 방식, 아에 서비스 동의한 사람과 아닌 사람에 대한 생성자를 나누기!
     */
    public static ResponseSearch of(final Member searched) {
        if (searched.isAgree()) {
            return agreedMember(searched);
        }

        final ResponseSearch responseSearch = new ResponseSearch();

        responseSearch.intraId = searched.getIntraId();
        responseSearch.intraName = searched.getIntraName();
        responseSearch.image = searched.getImage();
        responseSearch.location = searched.getLocation();

        return responseSearch;
    }

    private static ResponseSearch agreedMember(final Member searched) {
        final ResponseSearch responseSearch = new ResponseSearch();

        responseSearch.intraId = searched.getIntraId();
        responseSearch.intraName = searched.getIntraName();
        responseSearch.image = searched.getImage();
        responseSearch.location = searched.getLocation();
        responseSearch.comment = searched.getComment();
        responseSearch.inOrOut = searched.isInCluster();
        responseSearch.isFriend = isfriend;

        return responseSearch;
    }
}