package kr.where.backend.member;

import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.DeleteMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        memberRepository.findByIntraId(intraId).ifPresent(present -> {
            throw new RuntimeException("이미 존재하는 멤버입니다.");
        });
    }

    public List<Member> findAll() {
        final List<Member> members = memberRepository.findAll();
        return members;
    }

    public void deleteMember(DeleteMemberDto deleteMemberDto) {
        Member member = memberRepository.findByIntraId(deleteMemberDto.getIntraId())
                .orElseThrow(RuntimeException::new);

        this.memberRepository.delete(member);
    }
}
