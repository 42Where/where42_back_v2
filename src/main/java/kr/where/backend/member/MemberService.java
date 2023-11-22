package kr.where.backend.member;

import kr.where.backend.group.GroupMemberService;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.groupmember.CreateGroupMemberDTO;
import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.DeleteMemberDto;
import kr.where.backend.member.DTO.ResponseMemberDto;
import kr.where.backend.member.DTO.UpdateMemberDto;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final GroupService groupService;

	private final GroupMemberService groupMemberService;

	@Transactional
	public ResponseMemberDto signUp(final CreateMemberDto createMemberDto) {

		Member member = memberRepository.findByIntraId(createMemberDto.getIntraId()).orElse(null);

		if (member != null && member.isAgree()) {
			throw new MemberException.DuplicatedMemberException();
		} else if (member != null && !member.isAgree()) {
			member.setFlashToMember(createMemberDto);
		} else {
			member = new Member(createMemberDto);
			memberRepository.save(member);
		}

		if (member.isAgree()) {
			ResponseGroupDto responseGroupDto = groupService.createGroup(new CreateGroupDto(member.getIntraId(), "default"));
			member.setDefaultGroupId(responseGroupDto.getGroupId());
		}
		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(member.getIntraId())
				.intraName(member.getIntraName()).grade(member.getGrade())
				.image(member.getImage()).agree(member.isAgree()).build();

		return responseMemberDto;
	}

//	@Transactional
//	public ResponseMemberDto updateFlashToMember(Member alreadyExistMember, final CreateMemberDto createMemberDto) {
//		alreadyExistMember.setFlashToMember(createMemberDto);
//
//		ResponseGroupDto responseGroupDto = groupService.createGroup(new CreateGroupDto(alreadyExistMember.getIntraId(), "default"));
//		alreadyExistMember.setDefaultGroupId(responseGroupDto.getGroupId());
//
//		groupMemberService.createGroupMember(new CreateGroupMemberDTO(alreadyExistMember.getIntraId(),
//				alreadyExistMember.getDefaultGroupId(), responseGroupDto.getGroupName(), true));
//
//
//		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(alreadyExistMember.getIntraId())
//				.intraName(alreadyExistMember.getIntraName()).grade(alreadyExistMember.getGrade())
//				.image(alreadyExistMember.getImage()).agree(alreadyExistMember.isAgree()).build();
//
//		return responseMemberDto;
//	}

//	@Transactional
//	public Member createMember(final CreateMemberDto createMemberDto) {
//		final Member member = new Member(createMemberDto);
//		memberRepository.save(member);
//
////		ResponseGroupDto responseGroupDto = groupService.createGroup(new CreateGroupDto(member.getIntraId(), "default"));
////		member.setDefaultGroupId(responseGroupDto.getGroupId());
////
////		groupMemberService.createGroupMember(new CreateGroupMemberDTO(member.getIntraId(),
////			member.getDefaultGroupId(), responseGroupDto.getGroupName(), true));
////
////		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(member.getIntraId())
////			.intraName(member.getIntraName()).grade(member.getGrade())
////			.image(member.getImage()).agree(member.isAgree()).build();
//
//		return member;
//	}

	@Transactional
	public ResponseMemberDto createFlashMember(final CreateMemberDto createFlashMember) {
		final Member member = new Member(createFlashMember);
		memberRepository.save(member);

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(member.getIntraId())
				.intraName(member.getIntraName()).agree(member.isAgree()).build();

		return responseMemberDto;
	}

//	public void validateDuplicatedMember(final Long intraId) {
//		memberRepository.findByIntraId(intraId).ifPresent(present -> {
//			if(present.isAgree())
//				throw new MemberException.DuplicatedMemberException();;
//		});
//	}

	public List<ResponseMemberDto> findAll() {
		final List<Member> members = memberRepository.findAll();
		final List<ResponseMemberDto> responseMemberDtos = members.stream().map(member -> ResponseMemberDto.builder()
				.intraId(member.getIntraId())
				.intraName(member.getIntraName()).grade(member.getGrade())
				.image(member.getImage()).build()).toList();

		return responseMemberDtos;
	}

	@Transactional
	public ResponseMemberDto deleteMember(DeleteMemberDto deleteMemberDto) {
		final Member member = memberRepository.findByIntraId(deleteMemberDto.getIntraId())
				.orElseThrow(MemberException.NoMemberException::new);
		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(member.getIntraId()).build();

		memberRepository.delete(member);

		return responseMemberDto;
	}

	@Transactional
	public ResponseMemberDto updateComment(final UpdateMemberDto updateMemberDto) {
		final Member member = memberRepository.findByIntraId(updateMemberDto.getIntraId())
				.orElseThrow(MemberException.NoMemberException::new);
		member.updatePersonalMsg(updateMemberDto.getComment());

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder()
				.intraId(member.getIntraId())
				.comment(member.getComment())
				.build();

		return responseMemberDto;
	}

//	@Transactional
//	public ResponseMemberDto updateCustomLocation(final UpdateMemberDto updateMemberDto) {
//		final Member member = memberRepository.findByIntraId(updateMemberDto.getIntraId())
//			.orElseThrow(MemberException.NoMemberException::new);
//		member.updateCustomLocation(updateMemberDto.getCustomLocation());
//
//		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder()
//			.intraId(member.getIntraId())
//			.customLocation(member.getCustomLocation())
//			.build();
//
//		return responseMemberDto;
//	}

	public ResponseMemberDto findOneByIntraId(final Long intraId) {
		final Member member = memberRepository.findByIntraId(intraId).orElseThrow(MemberException.NoMemberException::new);

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder()
				.intraId(member.getIntraId())
				.intraName(member.getIntraName())
				.grade(member.getGrade())
				.image(member.getImage())
				.comment(member.getComment())
//			.customLocation(member.getCustomLocation())
				.inCluster(member.isInCluster())
//			.imacLocation(member.getImacLocation())
				.build();

		return responseMemberDto;
	}
}

