package kr.where.backend.member;

import kr.where.backend.member.dto.CreateMemberDto;
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

	@Test
	public void 맴버_생성_테스트() {
		//given
		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");

		//when
		ResponseMemberDto responseMemberDto = memberService.signUp(createMemberDto);

		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
		assertThat(responseMemberDto.getGrade()).isEqualTo(1);
		assertThat(responseMemberDto.getImage()).isEqualTo("image");
	}

	@Test
	public void 플래시_멤버_생성_테스트() {
		//given
		CreateMemberDto createFlashMemberDto = CreateMemberDto.create_flash(12345L, "suhwpark");

		//when
		ResponseMemberDto responseMemberDto = memberService.createFlashMember(createFlashMemberDto);

		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
		assertThat(responseMemberDto.isAgree()).isEqualTo(false);
	}

	@Test
	public void 플래시멤버_To_멤버_테스트() {
		//given
		CreateMemberDto createFlashMemberDto = CreateMemberDto.create_flash(12345L, "suhwpark");

		memberService.createFlashMember(createFlashMemberDto);

		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");

		//when
		ResponseMemberDto responseMemberDto = memberService.signUp(createMemberDto);
		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(12345L);
		assertThat(responseMemberDto.getIntraName()).isEqualTo("suhwpark");
		assertThat(responseMemberDto.getGrade()).isEqualTo(1);
		assertThat(responseMemberDto.getImage()).isEqualTo("image");
		assertThat(responseMemberDto.isAgree()).isEqualTo(true);
	}

	@Test
	public void 맴버_중복_테스트() {
		//given
		CreateMemberDto createMemberDto = CreateMemberDto.create(12345L, "suhwpark", 1, "image");

		//when
		memberService.signUp(createMemberDto);

		//then
		assertThatThrownBy(() -> memberService.signUp(createMemberDto))
			.isInstanceOf(MemberException.class);
	}

	@Test
	public void 모든_맴버_목록_조회_테스트() {
		//given
		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
		CreateMemberDto jnam = CreateMemberDto.create(2L, "jnam", 1, "image");
		CreateMemberDto hjeong = CreateMemberDto.create(3L, "hjeong", 1, "image");
		CreateMemberDto jonhan = CreateMemberDto.create(4L, "jonhan", 1, "image");

		ResponseMemberDto suhwparkDto = memberService.signUp(suhwpark);
		ResponseMemberDto jnamDto = memberService.signUp(jnam);
		ResponseMemberDto hjeongDto = memberService.signUp(hjeong);
		ResponseMemberDto jonhanDto = memberService.signUp(jonhan);

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
		ResponseMemberDto jnamDto = memberService.signUp(jnam);

		Member jnamEntity = memberRepository.findByIntraId(1L).orElseThrow(MemberException.NoMemberException::new);
		jnamEntity.setOtherInformation("comment", false);

		memberRepository.save(jnamEntity);

		//when
		ResponseMemberDto responseMemberDto = memberService.findOneByIntraId(jnam.getIntraId());

		//then
		assertThat(responseMemberDto.getIntraId()).isEqualTo(jnamDto.getIntraId());
		assertThat(responseMemberDto.getIntraName()).isEqualTo(jnamDto.getIntraName());
		assertThat(responseMemberDto.getGrade()).isEqualTo(jnamDto.getGrade());
		assertThat(responseMemberDto.getImage()).isEqualTo(jnamDto.getImage());
		assertThat(responseMemberDto.getComment()).isEqualTo(jnamEntity.getComment());
//		assertThat(responseMemberDto.getCustomLocation()).isEqualTo(jnamEntity.getCustomLocation());
//		assertThat(responseMemberDto.getImacLocation()).isEqualTo(jnamEntity.getImacLocation());
		assertThat(responseMemberDto.isInCluster()).isEqualTo(jnamEntity.isInCluster());
	}

	@Test
	public void 멤버_한명_조회_예외_테스트() {
		//then
		assertThatThrownBy(() -> memberService.findOneByIntraId(1L)).isInstanceOf(MemberException.class);
	}

//	@Test
//	public void 맴버_삭제_테스트() {
//		//given
//		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
//		memberService.signUp(suhwpark);
//		DeleteMemberDto deleteMemberDto = new DeleteMemberDto();
//		deleteMemberDto.setId(1L);
//
//		//when
//		memberService.deleteMember(deleteMemberDto);
//		Member member = memberRepository.findById(1L).orElse(null);
//
//		//then
//		assertThat(member).isEqualTo(null);
//	}


	@Test
	public void 맴버_개인_메시지_설정_테스트() {
		//given
		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
		memberService.signUp(suhwpark);
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

//	@Test
//	public void 맴버_수동_자리_설정_테스트() {
//		//given
//		CreateMemberDto suhwpark = CreateMemberDto.create(1L, "suhwpark", 1, "image");
//		memberService.signUp(suhwpark);
//		Member member = memberRepository.findById(1L).get();
//		String beforeLocation = member.getCustomLocation();
//
//		//when
//		UpdateMemberDto updateMemberDto = new UpdateMemberDto();
//		updateMemberDto.setCustomLocation("pingpong");
//		updateMemberDto.setId(1L);
//		memberService.updateCustomLocation(updateMemberDto);
//		String afterLocation = member.getCustomLocation();
//		//then
//		assertThat(beforeLocation).isEqualTo(null);
//		assertThat(afterLocation).isEqualTo("pingpong");
//	}

}
