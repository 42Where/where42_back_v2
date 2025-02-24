package kr.where.backend.group.dto.group;

import kr.where.backend.group.dto.groupmember.ResponseGroupMemberListDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResponseOwnGroupMemberDTO {
    private final ResponseGroupMemberListDTO defaultGroup;
    private final List<ResponseGroupMemberListDTO> groups;

    public static ResponseOwnGroupMemberDTO of(final ResponseGroupMemberListDTO defaultGroup,
                                               final List<ResponseGroupMemberListDTO> groups) {
        return new ResponseOwnGroupMemberDTO(defaultGroup, groups);
    }
}
