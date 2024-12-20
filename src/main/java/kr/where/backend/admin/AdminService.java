package kr.where.backend.admin;

import java.util.List;
import java.util.Objects;
import kr.where.backend.admin.dto.RequestRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseAdminMembersDTO;
import kr.where.backend.admin.dto.ResponseCheckAdminDTO;
import kr.where.backend.admin.dto.ResponseRoleDTO;
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

    public ResponseCheckAdminDTO checkAdmin(final AuthUser authUser) {
        final Member member = memberRepository.findByIntraId(authUser.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        return ResponseCheckAdminDTO.of(Objects.equals(member.getRole(), ADMIN_ROLE));
    }

    @Transactional
    public ResponseRoleDTO changeAdminStatus(final RequestRoleStatusDTO requestRoleStatusDTO) {
        final Member targetMember = memberRepository.findByIntraName(requestRoleStatusDTO.getIntraName())
                .orElseThrow(MemberException.NoMemberException::new);
        if (!targetMember.getRole().equals(requestRoleStatusDTO.getRole()))
            targetMember.updateRole(requestRoleStatusDTO.getRole());
        return ResponseRoleDTO.of(targetMember.getIntraName(), targetMember.getRole());
    }

    public ResponseAdminMembersDTO getAllAdmin() {
        final List<Member> members = memberRepository.findAllByRole(ADMIN_ROLE);
        return ResponseAdminMembersDTO.of(members.stream().map(Member::getIntraName).toList());
    }
}
