package kr.where.backend.member;

import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.group.GroupRepository;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.location.Location;
import kr.where.backend.location.LocationRepository;
import kr.where.backend.location.LocationService;
import kr.where.backend.member.dto.ResponseMemberDto;
import kr.where.backend.member.dto.UpdateMemberDto;

import kr.where.backend.member.exception.MemberException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class memberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	LocationRepository locationRepository;
	@Autowired
	LocationService locationService;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	GroupMemberRepository groupMemberRepository;

	@Test
	public void create_agree_member_test() {
		//given
		CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(12345L, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
		Hane hane = Hane.createForTest("IN");

		//when
		ResponseMemberDto responseMemberDto = memberService.createAgreeMember(cadetPrivacy, hane);

		Optional<Member> member = memberRepository.findByIntraId(cadetPrivacy.getId());

		Location location = locationRepository.findByMember(member.get());
		Optional<Group> group = groupRepository.findById(responseMemberDto.getDefaultGroupId());
		List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(responseMemberDto.getDefaultGroupId());

		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
		assertThat(responseMemberDto.getImage()).isEqualTo("image");
		assertThat(responseMemberDto.getGrade()).isEqualTo("2022-10-31");
		assertThat(responseMemberDto.isAgree()).isEqualTo(true);

		assertThat(responseMemberDto.getLocation().getImacLocation()).isEqualTo("c1r1s1");
		assertThat(location.getImacLocation()).isEqualTo("c1r1s1");

		assertThat(group.get()).isNotNull();
		assertThat(group.get().getGroupName()).isEqualTo("default");

		assertThat(groupMembers).isNotNull();
		assertThat(groupMembers.get(0).getMember().getIntraId()).isEqualTo(12345L);
		assertThat(groupMembers.get(0).getIsOwner()).isEqualTo(true);

	}

//	@Test
//	public void 플래시_멤버_생성_테스트() {
//		//given
//		CreateMemberDto createFlashMemberDto = CreateMemberDto.createFlash(12345L, "suhwpark");
//
//		//when
//		ResponseMemberDto responseMemberDto = memberService.createDisagreeMember(createFlashMemberDto);
//
//		//then
//		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
//		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
//		assertThat(responseMemberDto.isAgree()).isEqualTo(false);
//	}
//
//	@Test
//	public void 플래시멤버_To_멤버_테스트() {
//		//given
//		CreateMemberDto createFlashMemberDto = CreateMemberDto.createFlash(12345L, "suhwpark");
//
//		memberService.createDisagreeMember(createFlashMemberDto);
//
//		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");
//
//		//when
//		ResponseMemberDto responseMemberDto = memberService.createAgreeMember(createMemberDto);
//		//then
//		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
//		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
//		assertThat(responseMemberDto.getGrade()).isEqualTo(1);
//		assertThat(responseMemberDto.getImage()).isEqualTo("image");
//		assertThat(responseMemberDto.isAgree()).isEqualTo(true);
//	}
//
//	@Test
//	public void 맴버_중복_테스트() {
//		//given
//		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");
//
//		//when
//		memberService.createAgreeMember(createMemberDto);
//
//		//then
//		assertThatThrownBy(() -> memberService.createAgreeMember(createMemberDto))
//			.isInstanceOf(MemberException.class);
//	}
//
//	@Test
//	public void 모든_맴버_목록_조회_테스트() {
//		//given
//		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
//		CreateMemberDto jnam = CreateMemberDto.create(2L, "jnam", 1, "image");
//		CreateMemberDto hjeong = CreateMemberDto.create(3L, "hjeong", 1, "image");
//		CreateMemberDto jonhan = CreateMemberDto.create(4L, "jonhan", 1, "image");
//
//		ResponseMemberDto suhwparkDto = memberService.createAgreeMember(suhwpark);
//		ResponseMemberDto jnamDto = memberService.createAgreeMember(jnam);
//		ResponseMemberDto hjeongDto = memberService.createAgreeMember(hjeong);
//		ResponseMemberDto jonhanDto = memberService.createAgreeMember(jonhan);
//
//		//when
//		List<ResponseMemberDto> responseMemberDto = memberService.findAll();
//
//		//then
//		assertThat(responseMemberDto.size()).isEqualTo(4);
//		assertThat(responseMemberDto.get(0).getIntraName()).isEqualTo(suhwparkDto.getIntraName());
//		assertThat(responseMemberDto.get(1).getIntraName()).isEqualTo(jnamDto.getIntraName());
//		assertThat(responseMemberDto.get(2).getIntraName()).isEqualTo(hjeongDto.getIntraName());
//		assertThat(responseMemberDto.get(3).getIntraName()).isEqualTo(jonhanDto.getIntraName());
//	}
//
//	@Test
//	public void 멤버_한명_조회_테스트() {
//		//given
//		CreateMemberDto jnam = CreateMemberDto.create(1L, "jnam", 5, "image");
//		ResponseMemberDto jnamDto = memberService.createAgreeMember(jnam);
//
//		Member jnamEntity = memberRepository.findByIntraId(1L).orElseThrow(MemberException.NoMemberException::new);
//		jnamEntity.setOtherInformation("comment", false);
//
//		memberRepository.save(jnamEntity);
//
//		//when
//		ResponseMemberDto responseMemberDto = memberService.findOneByIntraId(jnam.getIntraId());
//
//		//then
//		assertThat(responseMemberDto.getIntraId()).isEqualTo(jnamDto.getIntraId());
//		assertThat(responseMemberDto.getIntraName()).isEqualTo(jnamDto.getIntraName());
//		assertThat(responseMemberDto.getGrade()).isEqualTo(jnamDto.getGrade());
//		assertThat(responseMemberDto.getImage()).isEqualTo(jnamDto.getImage());
//		assertThat(responseMemberDto.getComment()).isEqualTo(jnamEntity.getComment());
//		assertThat(responseMemberDto.isInCluster()).isEqualTo(jnamEntity.isInCluster());
//	}
//
//	@Test
//	public void 멤버_한명_조회_예외_테스트() {
//		//then
//		assertThatThrownBy(() -> memberService.findOneByIntraId(1L)).isInstanceOf(MemberException.class);
//	}
//
////	@Test
////	public void 맴버_삭제_테스트() {
////		//given
////		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
////		memberService.signUp(suhwpark);
////		DeleteMemberDto deleteMemberDto = new DeleteMemberDto();
////		deleteMemberDto.setId(1L);
////
////		//when
////		memberService.deleteMember(deleteMemberDto);
////		Member member = memberRepository.findById(1L).orElse(null);
////
////		//then
////		assertThat(member).isEqualTo(null);
////	}
//
//
//	@Test
//	public void 맴버_개인_메시지_설정_테스트() {
//		//given
//		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
//		memberService.createAgreeMember(suhwpark);
//		Member member = memberRepository.findByIntraId(1L).get();
//		String beforeComment = member.getComment();
//
//		//when
//		UpdateMemberDto updateMemberDto = new UpdateMemberDto();
//		updateMemberDto.setComment("안녕");
//		updateMemberDto.setIntraId(1L);
//		memberService.updateComment(updateMemberDto);
//		String afterComment = member.getComment();
//
//		//then
//		assertThat(beforeComment).isEqualTo(null);
//		assertThat(afterComment).isEqualTo("안녕");
//	}
//
//    @Test
//    public void 멤버_생성_전_location_생성_테스트() {
//        //given
//		Location location = new Location("c1r1s1");
//		locationRepository.save(location);
//
//		CreateMemberDto createMemberDto = CreateMemberDto.createWithLocation(2L, "jnam", "1", "image", location);
//		Member member = new Member(createMemberDto);
//		memberRepository.save(member);
//
//		location.setMember(member);
//
//		// when
//		Optional<Member> memberResult = memberRepository.findByIntraId(createMemberDto.getIntraId());
//		Location locationResult = locationRepository.findByMember(member);
//
//		// then
//		assertThat(memberResult.get().getLocation()).isNotNull();
//		assertThat(memberResult.get().getLocation().getImacLocation()).isEqualTo(location.getImacLocation());
//		assertThat(locationResult.getMember()).isNotNull();
//		assertThat(locationResult.getMember().getIntraId()).isEqualTo(member.getIntraId());
//    }
//
//	@Test
//	public void 멤버_생성_후_location_생성_테스트() {
//		//given
//		CreateMemberDto createMemberDto = CreateMemberDto.create(2L, "jnam", 1, "image");
//		Member member = new Member(createMemberDto);
//		memberRepository.save(member);
//
//		Location location = new Location(member, "c1r1s1");
//		locationRepository.save(location);
//		member.setLocation(location);
//
//		// when
//		Optional<Member> memberResult = memberRepository.findByIntraId(createMemberDto.getIntraId());
//		Location locationResult = locationRepository.findByMember(member);
//
//		// then
//		assertThat(memberResult.get().getLocation()).isNotNull();
//		assertThat(memberResult.get().getLocation().getImacLocation()).isEqualTo(location.getImacLocation());
//		assertThat(locationResult.getMember()).isNotNull();
//		assertThat(locationResult.getMember().getIntraId()).isEqualTo(member.getIntraId());
//	}
//
////	@Test
////	public void 맴버_수동_자리_설정_테스트() {
////		//given
////		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
////		memberService.signUp(suhwpark);
////		Member member = memberRepository.findById(1L).get();
////		String beforeLocation = member.getCustomLocation();
////
////		//when
////		UpdateMemberDto updateMemberDto = new UpdateMemberDto();
////		updateMemberDto.setCustomLocation("pingpong");
////		updateMemberDto.setId(1L);
////		memberService.updateCustomLocation(updateMemberDto);
////		String afterLocation = member.getCustomLocation();
////		//then
////		assertThat(beforeLocation).isEqualTo(null);
////		assertThat(afterLocation).isEqualTo("pingpong");
////	}

}
