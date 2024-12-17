package kr.where.backend.cluster;

import kr.where.backend.api.IntraApiService;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.cluster.dto.ResponseClusterDTO;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.cluster.exception.ClusterException;
import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.location.Location;
import kr.where.backend.location.LocationRepository;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.oauthtoken.OAuthTokenService;
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
    private final IntraApiService intraApiService;
    private final OAuthTokenService oauthTokenService;
    private final MemberRepository memberRepository;
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
        for (int cluster = 1; cluster <= 6; cluster++) {
            initClusterSeat(cluster, initClusterSeat.get(cluster));
        }
        //x 클러스터 초기화
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
                    location.getMember().getId(),
                    location.getMember().getIntraName(),
                    location.getMember().getImage(),
                    location.getImacLocation(),
                    members.contains(location.getMember()))
            );
        }
        return ResponseClusterListDTO.of(responseClusterDTOS);
    }

    private void validateCluster(final String cluster) {
        final String CLUSTER_REGEX = "^c(x)?\\d+$";

        //포맷형식이 맞는가? ex: "cx1" or "c1"
        if (!cluster.matches(CLUSTER_REGEX)) {
            throw new ClusterException.InvalidPathVariable();
        }

        //클러스터 숫자범위가 유효한가? ex: "c7"은 에러이다.
        final String area = cluster.replaceAll("\\d", "");
        final int number = Integer.parseInt(cluster.replaceAll("[^\\d]", ""));
        if (area.equals("c") && !(number >= 1 && number <= 6)) {
            throw new ClusterException.InvalidPathVariable();
        }
        if (area.equals("cx") && !(number >= 1 && number <= 2)) {
            throw new ClusterException.InvalidPathVariable();
        }
    }
}
