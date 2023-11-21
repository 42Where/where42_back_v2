package kr.where.backend.member;

import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.DeleteMemberDto;
import kr.where.backend.member.DTO.ResponseMemberDto;
import kr.where.backend.member.DTO.UpdateMemberDto;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;

import kr.where.backend.member.exception.MemberException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class memberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;

	@Test
	public void 맴버_생성_테스트() {
		//given
		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");

		//when
		ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);

		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
		assertThat(responseMemberDto.getGrade()).isEqualTo(1);
		assertThat(responseMemberDto.getImage()).isEqualTo("image");
	}

	@Test
	public void 맴버_중복_테스트() {
		//given
		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");

		//when
		memberService.createMember(createMemberDto);

		//then
		assertThatThrownBy(() -> memberService.validateDuplicatedMember(12345L))
			.isInstanceOf(MemberException.class);
	}

	@Test
	public void 모든_맴버_목록_조회_테스트() {
		//given
		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
		CreateMemberDto jnam = CreateMemberDto.create(2L, "jnam", 1, "image");
		CreateMemberDto hjeong = CreateMemberDto.create(3L, "hjeong", 1, "image");
		CreateMemberDto jonhan = CreateMemberDto.create(4L, "jonhan", 1, "image");

		ResponseMemberDto suhwparkDto = memberService.createMember(suhwpark);
		ResponseMemberDto jnamDto = memberService.createMember(jnam);
		ResponseMemberDto hjeongDto = memberService.createMember(hjeong);
		ResponseMemberDto jonhanDto = memberService.createMember(jonhan);

		//when
		List<ResponseMemberDto> responseMemberDto = memberService.findAll();

		//then
		assertThat(responseMemberDto.size()).isEqualTo(4);
		assertThat(responseMemberDto.get(0).getIntraName()).isEqualTo(suhwparkDto.getIntraName());
		assertThat(responseMemberDto.get(1).getIntraName()).isEqualTo(jnamDto.getIntraName());
		assertThat(responseMemberDto.get(2).getIntraName()).isEqualTo(hjeongDto.getIntraName());
		assertThat(responseMemberDto.get(3).getIntraName()).isEqualTo(jonhanDto.getIntraName());
	}

	@Test
	public void 멤버_한명_조회_테스트() {
		//given
		CreateMemberDto jnam = CreateMemberDto.create(1L, "jnam", 5, "image");
		ResponseMemberDto jnamDto = memberService.createMember(jnam);

		Member jnamEntity = memberRepository.findByIntraId(1L).orElseThrow(MemberException.NoMemberException::new);
		jnamEntity.setOtherinfomation("comment", "개포시장 떡볶이", false, "자리 없음");

		memberRepository.save(jnamEntity);

		//when
		ResponseMemberDto responseMemberDto = memberService.findOneByIntraId(jnam.getIntraId());

		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(jnamDto.getIntraId());
		assertThat(responseMemberDto.getIntraName()).isEqualTo(jnamDto.getIntraName());
		assertThat(responseMemberDto.getGrade()).isEqualTo(jnamDto.getGrade());
		assertThat(responseMemberDto.getImage()).isEqualTo(jnamDto.getImage());
		assertThat(responseMemberDto.getComment()).isEqualTo(jnamEntity.getComment());
		assertThat(responseMemberDto.getCustomLocation()).isEqualTo(jnamEntity.getCustomLocation());
		assertThat(responseMemberDto.getImacLocation()).isEqualTo(jnamEntity.getImacLocation());
		assertThat(responseMemberDto.isInCluster()).isEqualTo(jnamEntity.isInCluster());
	}

	@Test
	public void 멤버_한명_조회_예외_테스트() {
		//then
		assertThatThrownBy(() -> memberService.findOneByIntraId(1L)).isInstanceOf(MemberException.class);
	}

	@Test
	public void 맴버_삭제_테스트() {
		//given
		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
		memberService.createMember(suhwpark);
		DeleteMemberDto deleteMemberDto = new DeleteMemberDto();
		deleteMemberDto.setIntraId(1L);

		//when
		memberService.deleteMember(deleteMemberDto);
		Member member = memberRepository.findByIntraId(1L).orElse(null);

		//then
		assertThat(member).isEqualTo(null);
	}

	@Test
	public void 맴버_개인_메시지_설정_테스트() {
		//given
		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
		memberService.createMember(suhwpark);
		Member member = memberRepository.findByIntraId(1L).get();
		String beforeComment = member.getComment();

		//when
		UpdateMemberDto updateMemberDto = new UpdateMemberDto();
		updateMemberDto.setComment("안녕");
		updateMemberDto.setIntraId(1L);
		memberService.updateComment(updateMemberDto);
		String afterComment = member.getComment();

		//then
		assertThat(beforeComment).isEqualTo(null);
		assertThat(afterComment).isEqualTo("안녕");
	}

	@Test
	public void 맴버_수동_자리_설정_테스트() {
		//given
		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
		memberService.createMember(suhwpark);
		Member member = memberRepository.findByIntraId(1L).get();
		String beforeLocation = member.getCustomLocation();

		//when
		UpdateMemberDto updateMemberDto = new UpdateMemberDto();
		updateMemberDto.setCustomLocation("pingpong");
		updateMemberDto.setIntraId(1L);
		memberService.updateCustomLocation(updateMemberDto);
		String afterLocation = member.getCustomLocation();
		//then
		assertThat(beforeLocation).isEqualTo(null);
		assertThat(afterLocation).isEqualTo("pingpong");
	}
}
