package kr.where.backend.location;

import java.util.Arrays;
import java.util.List;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.location.dto.*;
import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final LocationUtils locationUtils;

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
     * @param authUser                accessToken 파싱한 정보
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
        locationUtils.validateCluster(cluster);
        final List<Location> loggedInImacs = locationRepository.findByImacLocationStartingWithAndMemberInClusterTrueOrMemberAgreeFalse(cluster);

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

    public ResponseClusterUsageListDTO getClusterImacUsage() {
        List<ResponseClusterUsageDTO> responseClusterUsageDTOs = Arrays.stream(ClusterLayout.values())
                .map(cluster -> {
                    final int usingImacCount = locationRepository.countAllByImacLocationStartingWith(cluster.getClusterName());
                    final int usingRate = locationUtils.getPercentage(usingImacCount, cluster.getTotalSeatCount());
                    return ResponseClusterUsageDTO.of(cluster.getClusterName(), usingRate, usingImacCount, cluster.getTotalSeatCount());
                })
                .toList();
        return ResponseClusterUsageListDTO.of(responseClusterUsageDTOs);
    }

    public ResponseImacUsageDTO getImacUsagePerHaneCount() {
        final int haneInCount = memberRepository.countAllByInClusterIsTrue();
        final int usingImacCount = locationRepository.countAllByImacLocationIsNotNull();
        final int usingRate = locationUtils.getPercentage(usingImacCount, haneInCount);
        return ResponseImacUsageDTO.of(usingRate, usingImacCount, haneInCount);
    }
}
