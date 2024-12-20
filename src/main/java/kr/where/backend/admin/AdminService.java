package kr.where.backend.admin;

import java.util.List;
import kr.where.backend.admin.dto.RequestRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseRoleStatusListDTO;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private static final String ADMIN_ROLE = "ADMIN";
    private final MemberRepository memberRepository;

    public ResponseRoleStatusDTO getRoleStatus(final AuthUser authUser) {
        final Member member = memberRepository.findByIntraId(authUser.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        return ResponseRoleStatusDTO.of(member.getIntraName(), member.getRole());
    }

    @Transactional
    public ResponseRoleStatusDTO changeAdminStatus(final RequestRoleStatusDTO requestRoleStatusDTO) {
        Member targerMember = memberRepository.findByIntraName(requestRoleStatusDTO.getIntraName())
                .orElseThrow(MemberException.NoMemberException::new);
        if (!targerMember.getRole().equals(requestRoleStatusDTO.getRole()))
            targerMember.updateRole(requestRoleStatusDTO.getRole());
        return ResponseRoleStatusDTO.of(targerMember.getIntraName(), targerMember.getRole());
    }

    public ResponseRoleStatusListDTO getAllAdmin() {
        final List<Member> members = memberRepository.findAllByRole(ADMIN_ROLE);
        final List<ResponseRoleStatusDTO> statuses = members.
                stream().map(member -> ResponseRoleStatusDTO.of(member.getIntraName(), member.getRole())).toList();
        return ResponseRoleStatusListDTO.of(statuses);
    }
}
