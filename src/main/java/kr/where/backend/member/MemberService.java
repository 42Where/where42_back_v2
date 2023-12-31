package kr.where.backend.member;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.location.LocationService;
import kr.where.backend.member.dto.ResponseMemberDto;
import kr.where.backend.member.dto.UpdateMemberCommentDto;
import kr.where.backend.group.entity.Group;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroupService groupService;
    private final LocationService locationService;

    @Transactional
    public Member createAgreeMember(final CadetPrivacy cadetPrivacy, final Hane hane) {

        Member member = memberRepository.findByIntraId(cadetPrivacy.getId()).orElse(null);

        if (member != null && member.isAgree()) {
            throw new MemberException.DuplicatedMemberException();
        } else if (member != null && !member.isAgree()) {
            member.setFlashToMember(cadetPrivacy, hane);
        } else {
            member = new Member(cadetPrivacy, hane);
            memberRepository.save(member);
            locationService.create(member, cadetPrivacy.getLocation());
        }
        ResponseGroupDto responseGroupDto = groupService.createGroup(new CreateGroupDto(member.getIntraId(), Group.DEFAULT_GROUP));
        member.setDefaultGroupId(responseGroupDto.getGroupId());

//        final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();

//        return responseMemberDto;

        return member;
    }

    @Transactional
    public Member createDisagreeMember(final CadetPrivacy cadetPrivacy) {
        final Member member = new Member(cadetPrivacy);
        memberRepository.save(member);
        locationService.create(member, cadetPrivacy.getLocation());

//        final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();
//        return responseMemberDto;
        return member;
    }

    public List<ResponseMemberDto> findAll() {
        final List<Member> members = memberRepository.findAll();
        final List<ResponseMemberDto> responseMemberDtoList = members.stream().map(member -> ResponseMemberDto.builder()
                .member(member).build()).toList();

        return responseMemberDtoList;
    }

    @Transactional
    public ResponseMemberDto deleteMember(Integer intraId) {
        final Member member = memberRepository.findByIntraId(intraId)
                .orElseThrow(MemberException.NoMemberException::new);
        final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();

        memberRepository.delete(member);

        return responseMemberDto;
    }

    @Transactional
    public ResponseMemberDto updateComment(final UpdateMemberCommentDto updateMemberCommentDto) {
        final Member member = memberRepository.findByIntraId(updateMemberCommentDto.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        member.setComment(updateMemberCommentDto.getComment());

        final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();
        return responseMemberDto;
    }

    public ResponseMemberDto findOneByIntraId(final Integer intraId) {
        final Member member = memberRepository.findByIntraId(intraId).orElseThrow(MemberException.NoMemberException::new);

        final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();

        return responseMemberDto;
    }

    // 위의 findOne이랑 중복! 안쓰면 지우자!
    public Optional<Member> findOne(final Integer intraId) {
        return memberRepository.findByIntraId(intraId);
    }
}

