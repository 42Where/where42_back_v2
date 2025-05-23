package kr.where.backend.member;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.api.json.hane.HaneRequestDto;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.group.GroupRepository;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.location.Location;
import kr.where.backend.location.LocationRepository;
import kr.where.backend.member.dto.ResponseMemberDTO;
import kr.where.backend.member.dto.UpdateMemberCommentDTO;
import org.springframework.test.util.ReflectionTestUtils;

import kr.where.backend.member.exception.MemberException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class MemberServiceTest {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	private AuthUser authUser;

	private final static Integer CAMPUS_ID = 29;

	@BeforeEach
	public void setUp() {
		Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
		authUser = new AuthUser(135436, "suhwpark", 1L);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
	}
	@Test
	public void create_agree_member_test() {

		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
		Hane hane = Hane.create("IN");

		//when
		Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());
		Optional<Group> group = groupRepository.findById(agreeMember.getDefaultGroupId());
		List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(agreeMember.getDefaultGroupId());

		//then
		assertThat(agreeMember.getIntraId()).isEqualTo(135436L);
		assertThat(agreeMember.getIntraName()).isEqualTo("suhwpark");
		assertThat(agreeMember.getImage()).isEqualTo("image");
		assertThat(agreeMember.getGrade()).isEqualTo("2022-10-31");
		assertThat(agreeMember.isAgree()).isEqualTo(true);

		assertThat(agreeMember.getLocation().getImacLocation()).isEqualTo("c1r1s1");
		assertThat(location.getImacLocation()).isEqualTo("c1r1s1");

		assertThat(group.get()).isNotNull();
		assertThat(group.get().getGroupName()).isEqualTo("default");

		assertThat(groupMembers).isNotNull();
		assertThat(groupMembers.get(0).getMember().getIntraId()).isEqualTo(135436L);
		assertThat(groupMembers.get(0).getIsOwner()).isEqualTo(true);

	}

	@Test
	public void create_disagree_member_test() {
		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);

		//when
		Member disagreeMember = memberService.createDisagreeMember(cadetPrivacy);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());

		//then
		assertThat(disagreeMember.getIntraId()).isEqualTo(135436L);
		assertThat(disagreeMember.getIntraName()).isEqualTo("suhwpark");
		assertThat(disagreeMember.getImage()).isEqualTo("image");
		assertThat(disagreeMember.getGrade()).isEqualTo("2022-10-31");
		assertThat(disagreeMember.isAgree()).isEqualTo(false);

		assertThat(disagreeMember.getLocation().getImacLocation()).isEqualTo("c1r1s1");
		assertThat(location.getImacLocation()).isEqualTo("c1r1s1");
	}

	@Test
	public void disagree_to_agree_test() {
		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
		memberService.createDisagreeMember(cadetPrivacy);
		Hane hane = Hane.create("IN");

		//when
		Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());
		Optional<Group> group = groupRepository.findById(agreeMember.getDefaultGroupId());
		List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(agreeMember.getDefaultGroupId());

		//then
		assertThat(agreeMember.getIntraId()).isEqualTo(135436L);
		assertThat(agreeMember.getIntraName()).isEqualTo("suhwpark");
		assertThat(agreeMember.getImage()).isEqualTo("image");
		assertThat(agreeMember.getGrade()).isEqualTo("2022-10-31");
		assertThat(agreeMember.isAgree()).isEqualTo(true);

		assertThat(agreeMember.getLocation().getImacLocation()).isEqualTo("c1r1s1");
		assertThat(location.getImacLocation()).isEqualTo("c1r1s1");

		assertThat(group.get()).isNotNull();
		assertThat(group.get().getGroupName()).isEqualTo("default");

		assertThat(groupMembers).isNotNull();
		assertThat(groupMembers.get(0).getMember().getIntraId()).isEqualTo(135436L);
		assertThat(groupMembers.get(0).getIsOwner()).isEqualTo(true);
	}

	@Test
	public void member_duplicate_test() {
		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
		Hane hane = Hane.create("IN");

		//when
		memberService.createAgreeMember(cadetPrivacy, hane);

		//then
		assertThatThrownBy(() -> memberService.createAgreeMember(cadetPrivacy, hane))
			.isInstanceOf(MemberException.class);
	}

	@Test
	public void update_comment_test() {
		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
		Hane hane = Hane.create("IN");
		memberService.createAgreeMember(cadetPrivacy, hane);

		Member member = memberRepository.findByIntraId(cadetPrivacy.getId()).orElse(null);
		String beforeComment = member.getComment();

		//when
		UpdateMemberCommentDTO updateMemberCommentDto = new UpdateMemberCommentDTO();
		updateMemberCommentDto.setComment("new comment");

		memberService.updateComment(updateMemberCommentDto, authUser);
		String afterComment = member.getComment();

		//then
		assertThat(beforeComment).isEqualTo(null);
		assertThat(afterComment).isEqualTo("new comment");
	}

	@Test
	public void delete_comment_test() {
		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
		Hane hane = Hane.create("IN");
		memberService.createAgreeMember(cadetPrivacy, hane);

		Member member = memberRepository.findByIntraId(cadetPrivacy.getId()).orElse(null);

		UpdateMemberCommentDTO updateMemberCommentDto = new UpdateMemberCommentDTO();
		updateMemberCommentDto.setComment("new comment");
		memberService.updateComment(updateMemberCommentDto, authUser);
		String beforeComment = member.getComment();

		//when
		memberService.deleteComment(authUser);
		String afterComment = member.getComment();

		//then
		assertThat(beforeComment).isEqualTo("new comment");
		assertThat(afterComment).isEqualTo(null);
	}

	@Test
	public void invalidCampusId() throws MemberException {
	    //given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(
				135436, "suhwpark", "c1r1s1",
				"image", true, "2022-10-31", 30
		);

		//then
		assertThatThrownBy(() -> memberService.isSeoulCampus(cadetPrivacy))
				.isInstanceOf(MemberException.NotFromSeoulCampus.class);
	}

	@Test
	public void anotherCampusTryToCreateDisagreeMember() throws Exception {
	    //given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(
				135436, "suhwpark", "c1r1s1",
				"image", true, "2022-10-31", 30
		);
	    //then
		assertThatThrownBy(() -> memberService.createDisagreeMember(cadetPrivacy))
				.isInstanceOf(MemberException.NotFromSeoulCampus.class);
	}

	@Test
	public void findOneByIntraId() {
		//given
		CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "", "image", true, "2022-10-31", CAMPUS_ID);
		Hane hane = Hane.create("IN");
		Member createMember = memberService.createAgreeMember(cadetPrivacy, hane);

		//when
		// createMember.setInClusterUpdatedAtForTest();
		ResponseMemberDTO findMember = memberService.findOneByIntraId(135436);
		Member member = memberRepository.findByIntraId(135436).orElse(null);

		//then
		System.out.println(createMember.getInClusterUpdatedAt());
		System.out.println(member.getInClusterUpdatedAt());
		assertThat(findMember.getIntraId()).isEqualTo(135436L);
		assertThat(findMember.getIntraName()).isEqualTo("suhwpark");
		assertThat(findMember.isInCluster()).isEqualTo(true);

	}

	@Test
	@DisplayName("멤버가 클러스터에 없어도 imacLocation이 유지 되는지 확인하는 테스트")
	public void memberSetInClusterTest() {
		// given
		AuthUser authUser = new AuthUser(123456, "suhwpark", 2L);
		memberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser);
		Member member = memberRepository.findByIntraId(123456).get();


		member.setInCluster(Hane.create("IN"));
		assertThat(member.getLocation().getImacLocation()).isEqualTo("c1r1s1");

		member.setInCluster(Hane.create("OUT"));
		assertThat(member.getLocation().getImacLocation()).isEqualTo("c1r1s1");
	}

	@Test
	@DisplayName("동의 멤버 조회 Test")
	public void findAgreeMembersTest() {
		AuthUser authUser1 = new AuthUser(111111, "jonhan", 1L);
		memberCreateAndSave(111111, "jonhan", "c1r2s1", "IN", authUser1);
		AuthUser authUser2 = new AuthUser(222222, "suhwparkk", 2L);
		memberCreateAndSave(222222, "suhwparkk", "c1r2s2", "IN", authUser2);
		AuthUser authUser3 = new AuthUser(333333, "soohlee", 3L);
		memberCreateAndSave(333333, "soohlee", "c1r2s3", "IN", authUser3);

		List<HaneRequestDto> members = memberService.findAgreeMembers().orElseThrow();
		assertThat(members.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("업데이트 가능한 동의 멤버 조회 Test")
	public void findUpdatableAgreeMembersTest() {
		//given
		AuthUser authUser1 = new AuthUser(111111, "jonhan", 1L);
		memberCreateAndSave(111111, "jonhan", "c1r2s1", "IN", authUser1);
		AuthUser authUser2 = new AuthUser(222222, "suhwparkk", 2L);
		memberCreateAndSave(222222, "suhwparkk", "c1r2s2", "IN", authUser2);
		AuthUser authUser3 = new AuthUser(333333, "soohlee", 3L);
		memberCreateAndSave(333333, "soohlee", "c1r2s3", "IN", authUser3);


		Member member = memberRepository.findByIntraId(111111).orElseThrow();
		ReflectionTestUtils.setField(member, "inClusterUpdatedAt", LocalDateTime.now().minusMinutes(10));
		//ReflectionTestUtils을 통해 private필드도 변경가능

		//when
		List<Member> members = memberService.findUpdatableAgreeMembers().orElseThrow();

		//then
		assertThat(members.size()).isEqualTo(1);
	}

	//멤버를 생성,저장하는 공통 메소드
	private void memberCreateAndSave(int intraId, String intraName, String location, String haneInOut, AuthUser authUser) {
		Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
		CadetPrivacy cadetPrivacy = new CadetPrivacy(intraId, intraName, location, "image", true, "2022-10-31", CAMPUS_ID);
		Hane hane = Hane.create(haneInOut);
		memberService.createAgreeMember(cadetPrivacy, hane);
	}

}
