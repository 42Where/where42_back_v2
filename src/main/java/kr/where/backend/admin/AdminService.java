package kr.where.backend.admin;

import kr.where.backend.admin.dto.RequestAdminStatusDTO;
import kr.where.backend.admin.dto.ResponseAdminStatusDTO;
import kr.where.backend.admin.exception.AdminException;
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

        return new ResponseAdminStatusDTO(member.getRole());
    }

    public ResponseAdminStatusDTO changeAdminStatus(final RequestAdminStatusDTO requestAdminStatusDTO) {
        validateRole(requestAdminStatusDTO.getRole());

        Member targerMember = memberRepository.findByIntraName(requestAdminStatusDTO.getIntraName())
                .orElseThrow(MemberException.NoMemberException::new);
        if (!targerMember.getRole().equals(requestAdminStatusDTO.getRole()))
            targerMember.setRole(requestAdminStatusDTO.getRole());
        return new ResponseAdminStatusDTO(targerMember.getRole());
    }

    private void validateRole(final String role) {
        if (!role.equals(ADMIN_ROLE) && !role.equals(USER_ROLE)) {
            throw new RequestException.BadRequestException();
        }
    }
}
