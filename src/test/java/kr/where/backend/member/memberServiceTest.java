package kr.where.backend.member;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.group.GroupRepository;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.location.Location;
import kr.where.backend.location.LocationRepository;
import kr.where.backend.location.LocationService;
import kr.where.backend.member.dto.UpdateMemberCommentDTO;

import kr.where.backend.member.exception.MemberException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
public class memberServiceTest {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private LocationService locationService;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	private AuthUserInfo authUser;

	@BeforeEach
	public void setUp() {
		Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
		authUser = new AuthUserInfo(12345, "suhwpark", 1L);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
	}
	@Test
	public void create_agree_member_test() {

		//given
		CadetPrivacy cadetPrivacy = CadetPrivacy.create(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
		Hane hane = Hane.create("IN");

		//when
		Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());
		Optional<Group> group = groupRepository.findById(agreeMember.getDefaultGroupId());
		List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(agreeMember.getDefaultGroupId());

		//then
		assertThat(agreeMember.getIntraId()).isEqualTo(12345L);
		assertThat(agreeMember.getIntraName()).isEqualTo("suhwpark");
		assertThat(agreeMember.getImage()).isEqualTo("image");
		assertThat(agreeMember.getGrade()).isEqualTo("2022-10-31");
		assertThat(agreeMember.isAgree()).isEqualTo(true);

		assertThat(agreeMember.getLocation().getImacLocation()).isEqualTo("c1r1s1");
		assertThat(location.getImacLocation()).isEqualTo("c1r1s1");

		assertThat(group.get()).isNotNull();
		assertThat(group.get().getGroupName()).isEqualTo("default");

		assertThat(groupMembers).isNotNull();
		assertThat(groupMembers.get(0).getMember().getIntraId()).isEqualTo(12345L);
		assertThat(groupMembers.get(0).getIsOwner()).isEqualTo(true);

	}

	@Test
	public void create_disagree_member_test() {
		//given
		CadetPrivacy cadetPrivacy = CadetPrivacy.create(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31");

		//when
		Member disagreeMember = memberService.createDisagreeMember(cadetPrivacy);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());

		//then
		assertThat(disagreeMember.getIntraId()).isEqualTo(12345L);
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
		CadetPrivacy cadetPrivacy = CadetPrivacy.create(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
		memberService.createDisagreeMember(cadetPrivacy);
		Hane hane = Hane.create("IN");

		//when
		Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());
		Optional<Group> group = groupRepository.findById(agreeMember.getDefaultGroupId());
		List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(agreeMember.getDefaultGroupId());

		//then
		assertThat(agreeMember.getIntraId()).isEqualTo(12345L);
		assertThat(agreeMember.getIntraName()).isEqualTo("suhwpark");
		assertThat(agreeMember.getImage()).isEqualTo("image");
		assertThat(agreeMember.getGrade()).isEqualTo("2022-10-31");
		assertThat(agreeMember.isAgree()).isEqualTo(true);

		assertThat(agreeMember.getLocation().getImacLocation()).isEqualTo("c1r1s1");
		assertThat(location.getImacLocation()).isEqualTo("c1r1s1");

		assertThat(group.get()).isNotNull();
		assertThat(group.get().getGroupName()).isEqualTo("default");

		assertThat(groupMembers).isNotNull();
		assertThat(groupMembers.get(0).getMember().getIntraId()).isEqualTo(12345L);
		assertThat(groupMembers.get(0).getIsOwner()).isEqualTo(true);
	}

	@Test
	public void member_duplicate_test() {
		//given
		CadetPrivacy cadetPrivacy = CadetPrivacy.create(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
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
		CadetPrivacy cadetPrivacy = CadetPrivacy.create(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
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
}
