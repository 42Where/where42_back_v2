package kr.where.backend.admin.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseAdminMembersDTO {
    private final List<String> members;

    public static ResponseAdminMembersDTO of(final List<String> members) {
        return new ResponseAdminMembersDTO(members);
    }
}
