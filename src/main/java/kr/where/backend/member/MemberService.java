package kr.where.backend.member;

import kr.where.backend.member.DTO.CreateMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member createMember(final CreateMemberDto createMemberDto) {
        this.validateDuplicatedMember(createMemberDto.getIntraId());
        Member member = new Member(createMemberDto);

        memberRepository.save(member);
        return member;
    }

    public void validateDuplicatedMember(final Long intraId) {
        Member member = memberRepository.findByIntraId(intraId).orElse(null);
        if (member != null) {
            throw new RuntimeException("중복된 회원입니다");
        }
    }
}
