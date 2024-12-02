package kr.where.backend.admin;

import java.util.List;
import kr.where.backend.admin.dto.RequestRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseRoleStatusListDTO;
import kr.where.backend.api.exception.RequestException;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    public ResponseRoleStatusDTO getRoleStatus(final AuthUser authUser) {
        final Member member = memberRepository.findByIntraId(authUser.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);

        return new ResponseRoleStatusDTO(member.getIntraName(), member.getRole());
    }

    public ResponseRoleStatusDTO changeAdminStatus(final RequestRoleStatusDTO requestRoleStatusDTO) {
        validateRole(requestRoleStatusDTO.getRole());

        Member targerMember = memberRepository.findByIntraName(requestRoleStatusDTO.getIntraName())
                .orElseThrow(MemberException.NoMemberException::new);
        if (!targerMember.getRole().equals(requestRoleStatusDTO.getRole()))
            targerMember.setRole(requestRoleStatusDTO.getRole());
        return new ResponseRoleStatusDTO(targerMember.getIntraName(), targerMember.getRole());
    }

    private void validateRole(final String role) {
        if (!role.equals(ADMIN_ROLE) && !role.equals(USER_ROLE)) {
            throw new RequestException.BadRequestException();
        }
    }

    public ResponseRoleStatusListDTO getAllAdmin() {
        final List<Member> members = memberRepository.findAllByRole("ADMIN");
        final List<ResponseRoleStatusDTO> statuses = members.
                stream().map(member -> ResponseRoleStatusDTO.of(member.getIntraName(), member.getRole())).toList();
        return ResponseRoleStatusListDTO.of(statuses);
    }
}
