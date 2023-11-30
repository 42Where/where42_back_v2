package kr.where.backend.member;

import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.location.LocationService;
import kr.where.backend.member.dto.DeleteMemberDto;
import kr.where.backend.member.dto.ResponseMemberDto;
import kr.where.backend.member.dto.UpdateMemberDto;
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
	public ResponseMemberDto createAgreeMember(final CadetPrivacy cadetPrivacy, final Hane hane) {

		// 1. if-else
//		Member member = memberRepository.findByIntraId(cadetPrivacy.getId()).orElse(null);
//
//		if (member != null && member.isAgree()) {
//			throw new MemberException.DuplicatedMemberException();
//		} else if (member != null && !member.isAgree()) {
//			member.setFlashToMember(cadetPrivacy, hane);
//		} else {
//			member = new Member(cadetPrivacy, hane);
//			memberRepository.save(member);
//			locationService.create(member, cadetPrivacy.getLocation());
//		}
//		ResponseGroupDto responseGroupDto = groupService.createGroup(new CreateGroupDto(member.getIntraId(), Group.DEFAULT_GROUP));
//		member.setDefaultGroupId(responseGroupDto.getGroupId());

		// 2. 람다식
		Member member = memberRepository.findByIntraId(cadetPrivacy.getId())
				.map(existingMember -> {
					if (existingMember.isAgree())
						throw new MemberException.DuplicatedMemberException();
					existingMember.setFlashToMember(cadetPrivacy, hane);
					return existingMember;
				})
				.orElseGet(() -> {
					Member newMember = new Member(cadetPrivacy, hane);
					memberRepository.save(newMember);
					locationService.create(newMember, cadetPrivacy.getLocation());
					return newMember;
				});

		ResponseGroupDto responseGroupDto = groupService.createGroup(new CreateGroupDto(member.getIntraId(), Group.DEFAULT_GROUP));
		member.setDefaultGroupId(responseGroupDto.getGroupId());

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();

		return responseMemberDto;
	}

	@Transactional
	public ResponseMemberDto createDisagreeMember(final CadetPrivacy cadetPrivacy) {
		final Member member = new Member(cadetPrivacy);
		memberRepository.save(member);
		locationService.create(member, cadetPrivacy.getLocation());

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();
		return responseMemberDto;
	}

	public List<ResponseMemberDto> findAll() {
		final List<Member> members = memberRepository.findAll();
		final List<ResponseMemberDto> responseMemberDtos = members.stream().map(member -> ResponseMemberDto.builder()
				.member(member).build()).toList();

		return responseMemberDtos;
	}

	@Transactional
	public ResponseMemberDto deleteMember(DeleteMemberDto deleteMemberDto) {
		final Member member = memberRepository.findByIntraId(deleteMemberDto.getIntraId())
				.orElseThrow(MemberException.NoMemberException::new);
		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();

		memberRepository.delete(member);

		return responseMemberDto;
	}

	@Transactional
	public ResponseMemberDto updateComment(final UpdateMemberDto updateMemberDto) {
		final Member member = memberRepository.findByIntraId(updateMemberDto.getIntraId())
				.orElseThrow(MemberException.NoMemberException::new);
		member.setComment(updateMemberDto.getComment());

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();
		return responseMemberDto;
	}

	public ResponseMemberDto findOneByIntraId(final Long intraId) {
		final Member member = memberRepository.findByIntraId(intraId).orElseThrow(MemberException.NoMemberException::new);

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().member(member).build();

		return responseMemberDto;
	}

	/**
	 * search에서 사용하는 로직입니다. 혹시 수정하면 수정하셔도 되요!
	 */
	public Optional<Member> findOne(final Long intraId) {
		return memberRepository.findByIntraId(intraId);
	}

	/**
	 * search 용도
	 */
	@Transactional
	public Member createDisagree(final CadetPrivacy cadetPrivacy) {
		System.out.println(cadetPrivacy);
		final Member member = new Member(cadetPrivacy);

		memberRepository.save(member);
		locationService.create(member, cadetPrivacy.getLocation());

		return member;
	}
}

