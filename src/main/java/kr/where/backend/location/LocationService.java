package kr.where.backend.location;

import java.util.List;
import java.util.Objects;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.cluster.ClusterConstant;
import kr.where.backend.location.dto.ResponseLoggedImacDTO;
import kr.where.backend.location.dto.ResponseLoggedImacListDTO;
import kr.where.backend.cluster.exception.ClusterException;
import kr.where.backend.group.GroupMemberRepository;
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
	private final GroupMemberRepository groupMemberRepository;

	/**
	 * member의 imac 정보를 set
	 * member와 location은 one-to-one mappling 되어있음
	 *
	 * @param member
	 * @param imac
	 */
	@Transactional
	public void create(final Member member, final String imac) {
		final Location location = new Location(member, imac);

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
		final AuthUser authUser
	) {
		final Member member = memberRepository.findByIntraId(authUser.getIntraId())
			.orElseThrow(MemberException.NoMemberException::new);
		final Location location = locationRepository.findByMember(member);

		location.setCustomLocation(updateCustomLocationDto.getCustomLocation());

		return ResponseLocationDTO.builder().location(location).build();
	}

	/**
	 * 수동자리 초기화(삭제)
	 *
	 * @param authUser accessToken 파싱한 정보
	 * @return responseLocationDTO
	 * @throws MemberException.NoMemberException 존재하지 않는 멤버입니다
	 */
	@Transactional
	public ResponseLocationDTO deleteCustomLocation(final AuthUser authUser) {
		final Member member = memberRepository.findByIntraId(authUser.getIntraId())
			.orElseThrow(MemberException.NoMemberException::new);
		final Location location = locationRepository.findByMember(member);

		location.setCustomLocation(null);

		return ResponseLocationDTO.builder().location(location).build();
	}

	/**
	 * 현재 아이맥에 로그인한 유저 정보 모두 조회
	 *
	 * @param authUser accessToken 파싱한 정보
	 * @return responseClusterListDTO
	 */
	public ResponseLoggedImacListDTO getLoggedInIMacs(final AuthUser authUser, final String cluster) {
		validateCluster(cluster);
		final List<Location> loggedInImacs = locationRepository.findByImacLocationStartingWith(cluster);

		final List<Member> friends = groupMemberRepository.findMembersByGroupId(authUser.getDefaultGroupId());

		return ResponseLoggedImacListDTO.of(
				loggedInImacs.stream()
						.map(location -> {
							final Member member = location.getMember(); // 한 번만 가져오기
							return ResponseLoggedImacDTO.of(
									member.getIntraId(),
									member.getIntraName(),
									member.getImage(),
									location.getImacLocation(),
									friends.contains(member)
							);
						})
						.toList()
		);
	}

	private void validateCluster(final String clusterZone) {
		//포맷형식이 맞는가? ex: "cx1" or "c1"
		if (!isValidFormat(clusterZone)) {
			throw new ClusterException.InvalidPathVariable();
		}

		//"c1"에서 "c" 추출
		final String prefix = extractPrefix(clusterZone);
		//"c1"에서 "1"을 추출
		final int clusterNumber = extractClusterNumber(clusterZone);

		//클러스터 숫자범위가 유효한가? ex: "c7"은 에러이다.
		if (!isValidClusterRange(prefix, clusterNumber)) {
			throw new ClusterException.InvalidPathVariable();
		}
	}

	private boolean isValidFormat(final String clusterZone) {
		return clusterZone.matches(ClusterConstant.CLUSTER_REGEX.getStringValue());
	}

	private String extractPrefix(final String clusterZone) {
		return clusterZone.replaceAll(ClusterConstant.ALL_NUMBER.getStringValue(), ClusterConstant.EMPTY_STRING.getStringValue());
	}

	private int extractClusterNumber(final String clusterZone) {
		return Integer.parseInt(clusterZone.replaceAll(ClusterConstant.ALL_STRING.getStringValue(), ClusterConstant.EMPTY_STRING.getStringValue()));
	}

	private boolean isValidClusterRange(final String prefix, final int clusterNumber) {
		if (Objects.equals(ClusterConstant.CLUSTER_C.getStringValue(), prefix)) {
			return clusterNumber >= ClusterConstant.CLUSTER_C.getMinValue() && clusterNumber <= ClusterConstant.CLUSTER_C.getMaxValue();
		}
		if (Objects.equals(ClusterConstant.CLUSTER_CX.getStringValue(), prefix)) {
			return clusterNumber >= ClusterConstant.CLUSTER_CX.getMinValue() && clusterNumber <= ClusterConstant.CLUSTER_CX.getMaxValue();
		}
		return false;
	}
}
