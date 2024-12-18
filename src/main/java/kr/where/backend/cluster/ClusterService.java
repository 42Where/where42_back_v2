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
        for (int cluster = 1; cluster <= 6; cluster++) {
            initClusterSeat(cluster, initClusterSeat.get(cluster));
        }
        //x클러스터 초기화 코드 추가해야함.
    }

    private void initClusterSeat(int c, ClusterLayout clusterLayout) {
        for (int r = 1; r <= clusterLayout.getRow(); r++) {
            for (int s = 1; s <= clusterLayout.getSeat(); s++) {
                Cluster cluster = new Cluster(String.valueOf(c), r, s);
                if (!clusterRepository.findByClusterAndRowIndexAndSeat(String.valueOf(c), r, s).isPresent())
                    clusterRepository.save(cluster);
            }
        }
    }

    public ResponseClusterListDTO getLoginMember(final AuthUser authUser, final String cluster) {
        validateCluster(cluster);
        final List<Location> locations = locationRepository.findByImacLocationStartingWith(cluster);

        final List<ResponseClusterDTO> responseClusterDTOS = new ArrayList<>();
        for (Location location : locations) {
            final List<Member> members = groupMemberRepository.findMembersByGroupId(authUser.getDefaultGroupId());

            responseClusterDTOS.add(new ResponseClusterDTO(
                    location.getMember().getIntraId(),
                    location.getMember().getIntraName(),
                    location.getMember().getImage(),
                    location.getImacLocation(),
                    members.contains(location.getMember()))
            );
        }
        return ResponseClusterListDTO.of(responseClusterDTOS);
    }


    private void validateCluster(final String cluster) {
        //포맷형식이 맞는가? ex: "cx1" or "c1"
        if (!isValidFormat(cluster)) {
            throw new ClusterException.InvalidPathVariable();
        }

        //"c1"에서 "c" 추출
        final String prefix = extractPrefix(cluster);
        //"c1"에서 "1"을 추출
        final int clusterNumber = extractClusterNumber(cluster);

        //클러스터 숫자범위가 유효한가? ex: "c7"은 에러이다.
        if (!isValidClusterRange(prefix, clusterNumber)) {
            throw new ClusterException.InvalidPathVariable();
        }
    }

    private boolean isValidFormat(final String cluster) {
        return cluster.matches(CLUSTER_REGEX);
    }

    private String extractPrefix(final String cluster) {
        return cluster.replaceAll("\\d", "");
    }

    private int extractClusterNumber(final String cluster) {
        return Integer.parseInt(cluster.replaceAll("[^\\d]", ""));
    }

    private boolean isValidClusterRange(final String prefix, final int clusterNumber) {
        if ("c".equals(prefix)) {
            return clusterNumber >= CLUSTER_C_MIN && clusterNumber <= CLUSTER_C_MAX;
        }
        if ("cx".equals(prefix)) {
            return clusterNumber >= CLUSTER_CX_MIN && clusterNumber <= CLUSTER_CX_MAX;
        }
        return false;
    }
}
