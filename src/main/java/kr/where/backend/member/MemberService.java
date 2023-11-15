package kr.where.backend.member;

import kr.where.backend.group.GroupService;
import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.DeleteMemberDto;
import kr.where.backend.member.DTO.ResponseMemberDto;
import kr.where.backend.member.DTO.UpdateMemberDto;
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

	@Transactional
	public ResponseMemberDto createMember(final CreateMemberDto createMemberDto) {
		this.validateDuplicatedMember(createMemberDto.getIntraId());
		final Member member = new Member(createMemberDto);

		memberRepository.save(member);

		// groupService.createGroup();
		// groupCreateDto를 넘겨야하네...? 띠용...

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(member.getIntraId())
			.intraName(member.getIntraName()).grade(member.getGrade())
			.image(member.getImage()).build();

		return responseMemberDto;
	}

	public void validateDuplicatedMember(final Long intraId) {
		memberRepository.findByIntraId(intraId).ifPresent(present -> {
			throw new RuntimeException("이미 존재하는 멤버입니다.");
		});
	}

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
			.orElseThrow(RuntimeException::new);
		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder().intraId(member.getIntraId()).build();

		memberRepository.delete(member);

		return responseMemberDto;
	}

	@Transactional
	public ResponseMemberDto updateComment(final UpdateMemberDto updateMemberDto) {
		final Member member = memberRepository.findByIntraId(updateMemberDto.getIntraId())
			.orElseThrow(RuntimeException::new);
		member.updatePersonalMsg(updateMemberDto.getComment());

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder()
			.intraId(member.getIntraId())
			.comment(member.getComment())
			.build();

		return responseMemberDto;
	}

	@Transactional
	public ResponseMemberDto updateCustomLocation(final UpdateMemberDto updateMemberDto) {
		final Member member = memberRepository.findByIntraId(updateMemberDto.getIntraId())
			.orElseThrow(RuntimeException::new);
		member.updateCustomLocation(updateMemberDto.getCustomLocation());

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder()
			.intraId(member.getIntraId())
			.customLocation(member.getCustomLocation())
			.build();

		return responseMemberDto;
	}

	public ResponseMemberDto findOneByIntraId(final Long intraId) {
		final Member member = memberRepository.findByIntraId(intraId).orElseThrow(RuntimeException::new);

		final ResponseMemberDto responseMemberDto = ResponseMemberDto.builder()
			.intraId(member.getIntraId())
			.intraName(member.getIntraName())
			.grade(member.getGrade())
			.image(member.getImage())
			.comment(member.getComment())
			.customLocation(member.getCustomLocation())
			.inCluster(member.isInCluster())
			.imacLocation(member.getImacLocation())
			.build();

		return responseMemberDto;
	}
}
