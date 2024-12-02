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

    public ResponseAdminStatusDTO getAdminStatus(final AuthUser authUser) {
        final Member member = memberRepository.findByIntraId(authUser.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);

        return new ResponseAdminStatusDTO(member.getRole());
    }
}
