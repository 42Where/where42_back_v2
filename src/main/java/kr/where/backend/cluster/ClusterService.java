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

    private final static String CLUSTER_REGEX = "^c(x)?\\d+$";
    private final static String ALL_NUMBER = "\\d";
    private final static String ALL_STRING = "[^\\d]";
    private final static String EMPTY_STRING = "";
    private final static String CLUSTER_C = "c";
    private final static String CLUSTER_CX = "cx";
    private static final int CLUSTER_C_MIN = 1;
    private static final int CLUSTER_C_MAX = 6;
    private static final int CLUSTER_CX_MIN = 1;
    private static final int CLUSTER_CX_MAX = 2;

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
        for (int cluster = CLUSTER_C_MIN; cluster <= CLUSTER_C_MAX; cluster++) {
            initClusterSeat(cluster, initClusterSeat.get(cluster));
        }
        //x클러스터 초기화 코드 추가해야함.
    }

    private void initClusterSeat(final int c, final ClusterLayout clusterLayout) {
        for (int r = 1; r <= clusterLayout.getRow(); r++) {
            for (int s = 1; s <= clusterLayout.getSeat(); s++) {
                Cluster cluster = new Cluster(String.valueOf(c), r, s);
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
        return clusterZone.matches(CLUSTER_REGEX);
    }

    private String extractPrefix(final String clusterZone) {
        return clusterZone.replaceAll(ALL_NUMBER, EMPTY_STRING);
    }

    private int extractClusterNumber(final String clusterZone) {
        return Integer.parseInt(clusterZone.replaceAll(ALL_STRING, EMPTY_STRING));
    }

    private boolean isValidClusterRange(final String prefix, final int clusterNumber) {
        if (Objects.equals(CLUSTER_C, prefix)) {
            return clusterNumber >= CLUSTER_C_MIN && clusterNumber <= CLUSTER_C_MAX;
        }
        if (Objects.equals(CLUSTER_CX, prefix)) {
            return clusterNumber >= CLUSTER_CX_MIN && clusterNumber <= CLUSTER_CX_MAX;
        }
        return false;
    }
}
