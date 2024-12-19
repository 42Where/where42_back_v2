package kr.where.backend.cluster;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.cluster.dto.ResponseClusterDTO;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.cluster.exception.ClusterException;
import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.location.Location;
import kr.where.backend.location.LocationRepository;
import kr.where.backend.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ClusterService {

    private final ClusterRepository clusterRepository;
    private final LocationRepository locationRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void init() {
        Map<Integer, ClusterLayout> initClusterSeat = new HashMap<>();
        initClusterSeat.put(1, ClusterLayout.CLUSTER_1);
        initClusterSeat.put(2, ClusterLayout.CLUSTER_2);
        initClusterSeat.put(3, ClusterLayout.CLUSTER_3);
        initClusterSeat.put(4, ClusterLayout.CLUSTER_4);
        initClusterSeat.put(5, ClusterLayout.CLUSTER_5);
        initClusterSeat.put(6, ClusterLayout.CLUSTER_6);
        clusterRepository.deleteAll();
        for (int cluster = ClusterConstant.CLUSTER_C.getMinValue(); cluster <= ClusterConstant.CLUSTER_C.getMaxValue(); cluster++) {
            initClusterSeat(cluster, initClusterSeat.get(cluster));
        }
        //x클러스터 초기화 코드 추가해야함.
    }

    private void initClusterSeat(final int c, final ClusterLayout clusterLayout) {
        for (int r = 1; r <= clusterLayout.getRow(); r++) {
            for (int s = 1; s <= clusterLayout.getSeat(); s++) {
                final Cluster cluster = new Cluster(String.valueOf(c), r, s);
                if (clusterRepository.findByClusterAndRowIndexAndSeat(String.valueOf(c), r, s).isEmpty())
                    clusterRepository.save(cluster);
            }
        }
    }

    public ResponseClusterListDTO getLoginMember(final AuthUser authUser, final String cluster) {
        validateCluster(cluster);
        final List<Location> locations = locationRepository.findByImacLocationStartingWith(cluster);

        final List<Member> members = groupMemberRepository.findMembersByGroupId(authUser.getDefaultGroupId());

        return ResponseClusterListDTO.of(
                locations.stream()
                    .map(location -> {
                        final Member member = location.getMember(); // 한 번만 가져오기
                        return ResponseClusterDTO.of(
                                member.getIntraId(),
                                member.getIntraName(),
                                member.getImage(),
                                location.getImacLocation(),
                                members.contains(member)
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
