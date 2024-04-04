package kr.where.backend.member;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.location.LocationService;
import kr.where.backend.member.dto.ResponseMemberDTO;
import kr.where.backend.member.dto.UpdateMemberCommentDTO;
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

	/**
	 * if (이미 존재하는 멤버)
	 *      throw duplicate_exception
	 * else if (비동의 멤버)
	 *      disagree -> agree
	 * else if (새로 생성해야 하는 상황)
	 *      new member
	 *      new location
	 *
	 * 멤버 생성후에는 new default group을 해준다
	 *
	 * @param cadetPrivacy : 42api에게 받아온 cadet info
	 * @param hane : hane api에게 받아온 inCluster 여부
	 * @return member
	 * @throws MemberException.DuplicatedMemberException 이미 존재하는 멤버입니다
	 */
	@Transactional
	public Member createAgreeMember(final CadetPrivacy cadetPrivacy, final Hane hane) {
		final AuthUser authUser = AuthUser.of();

		Member member = memberRepository.findByIntraId(authUser.getIntraId()).orElse(null);

		if (member != null && member.isAgree()) {
			throw new MemberException.DuplicatedMemberException();
		} else if (member != null && !member.isAgree()) {
			member.setDisagreeToAgree(hane);
		} else {
			member = new Member(cadetPrivacy, hane);
			memberRepository.save(member);
			locationService.create(member, cadetPrivacy.getLocation());
		}
		ResponseGroupDTO responseGroupDto = groupService.createGroup(new CreateGroupDTO(Group.DEFAULT_GROUP), authUser);
		member.setDefaultGroupId(responseGroupDto.getGroupId());

		return member;
	}

	/**
	 * hane의 inCluster 정보가 없는 disagree 멤버 생성
	 *
	 * @param cadetPrivacy : 42api에게 받아온 cadet info
	 * @return member
	 */
	@Transactional
	public Member createDisagreeMember(final CadetPrivacy cadetPrivacy) {
		final Member member = new Member(cadetPrivacy);
		memberRepository.save(member);
		locationService.create(member, cadetPrivacy.getLocation());

		return member;
	}

	/**
	 * find 전체 멤버 리스트
	 *
	 * @return responseMemberDTOList : member entity와 동일한 모양의 responseMemberDTO의 list
	 */
	public List<ResponseMemberDTO> findAll() {
		final List<Member> members = memberRepository.findAll();
		final List<ResponseMemberDTO> responseMemberDTOList = members.stream().map(member -> ResponseMemberDTO.builder()
			.member(member).build()).toList();

		return responseMemberDTOList;
	}

	/**
	 * 멤버 탈퇴
	 * accessToken에서 intraId를 얻어오므로 본인확인여부를 거치지 않는다
	 * jwt token과 member entity를 삭제
	 *
	 * @param authUser : accessToken 파싱한 정보
	 * @return responseMemberDto
	 * @throws MemberException.NoMemberException 존재하지 않는 멤버입니다
	 */
	@Transactional
	public ResponseMemberDTO deleteMember(final AuthUser authUser) {
		final Member member = memberRepository.findByIntraId(authUser.getIntraId())
			.orElseThrow(MemberException.NoMemberException::new);
		final ResponseMemberDTO responseMemberDto = ResponseMemberDTO.builder().member(member).build();

		memberRepository.delete(member);

		return responseMemberDto;
	}

	/**
	 * 존재하는 멤버인지 검사 후, 본인의 comment를 변경함
	 *
	 * @param updateMemberCommentDto : 변경할 comment
	 * @param authUser : accessToken 파싱한 정보
	 * @return responseMemberDTO
	 * @throws MemberException.NoMemberException 존재하지 않는 멤버입니다
	 */
	@Transactional
	public ResponseMemberDTO updateComment(
		final UpdateMemberCommentDTO updateMemberCommentDto,
		final AuthUser authUser) {
		final Member member = memberRepository.findByIntraId(authUser.getIntraId())
			.orElseThrow(MemberException.NoMemberException::new);
		member.setComment(updateMemberCommentDto.getComment());

		return ResponseMemberDTO.builder().member(member).build();
	}

	/**
	 * 존재하는 멤버인지 검사 후, 본인의 comment를 변경함
	 *
	 * @param authUser : accessToken 파싱한 정보
	 * @return responseMemberDTO
	 * @throws MemberException.NoMemberException 존재하지 않는 멤버입니다
	 */
	@Transactional
	public ResponseMemberDTO deleteComment(final AuthUser authUser) {
		final Member member = memberRepository.findByIntraId(authUser.getIntraId())
			.orElseThrow(MemberException.NoMemberException::new);
		member.setComment(null);

		return ResponseMemberDTO.builder().member(member).build();
	}

	/**
	 * intraId에 해당하는 member 한명을 찾음
	 * findOne API(in member)을 위한 service
	 *
	 * @param intraId
	 * @return responseMemberDTO
	 * @throws MemberException.NoMemberException 존재하지 않는 멤버입니다
	 */
	public ResponseMemberDTO findOneByIntraId(final Integer intraId) {
		final Member member = memberRepository.findByIntraId(intraId)
			.orElseThrow(MemberException.NoMemberException::new);

		return ResponseMemberDTO.builder().member(member).build();
	}

	/**
	 * intraId에 해당하는 member 한명을 찾음
	 * search API를 위한 service
	 *
	 * @param intraId
	 * @return Optional<Member>
	 */
	public Optional<Member> findOne(final Integer intraId) {
		return memberRepository.findByIntraId(intraId);
	}

	public Optional<List<Member>> findAgreeMembers() {
		return memberRepository.findAllByAgreeTrue();
	}
}

