package kr.where.backend.location;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

	private final LocationRepository locationRepository;
	private final MemberRepository memberRepository;

	/**
	 * member의 imac 정보를 set
	 * member와 location은 one-to-one mappling 되어있음
	 *
	 * @param member
	 * @param imac
	 */
	@Transactional
	public void create(final Member member, final String imac) {
		Location location = new Location(member, imac);

		locationRepository.save(location);

		member.setLocation(location);
	}

	/**
	 * 수동자리 업데이트
	 *
	 * @param updateCustomLocationDto 수동자리정보
	 * @param authUser accessToken 파싱한 정보
	 * @return responseLocationDTO
	 * @throws MemberException.NoMemberException 존재하지 않는 멤버입니다
	 */
	@Transactional
	public ResponseLocationDTO updateCustomLocation(
		final UpdateCustomLocationDTO updateCustomLocationDto,
		final AuthUser authUser) {
		final Member member = memberRepository.findByIntraId(authUser.getIntraId())
			.orElseThrow(MemberException.NoMemberException::new);
		final Location location = locationRepository.findByMember(member);

		location.setCustomLocation(updateCustomLocationDto.getCustomLocation());

		return ResponseLocationDTO.builder().location(location).build();
	}
}
