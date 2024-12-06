package kr.where.backend.cluster;

import kr.where.backend.api.IntraApiService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Triple;
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

    private static final String ADMIN_TOKEN = "admin";

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
                if (!clusterRepository.findByClusterAndRowAndSeat(String.valueOf(c), r, s).isPresent())
                    clusterRepository.save(cluster);
            }
        }
    }

    @Transactional
    public List<kr.where.backend.api.json.Cluster> getClusterSeat() {
        final String token = oauthTokenService.findAccessToken(ADMIN_TOKEN);

        final List<kr.where.backend.api.json.Cluster> result = new ArrayList<>();
        int page = 1;

        while (true) {
            final List<kr.where.backend.api.json.Cluster> loginMember = intraApiService.getCadetsInCluster(token, page);
            result.addAll(loginMember);
            if (loginMember.size() <= 99 || loginMember.get(99).getEnd_at() != null) {
                break;
            }
            log.info("" + page);
            page += 1;
        }
        log.info(String.valueOf(result.size()));

        //업데이트
        updateCluster(result);

        return result;
    }

    private void updateCluster(final List<kr.where.backend.api.json.Cluster> result) {
        clusterRepository.updateAllMembersToNull(); //초기화

        for (kr.where.backend.api.json.Cluster activeSeat : result) {
            final Triple<String, Integer, Integer> parsedSeat = parsingSeat(activeSeat.getUser().getLocation());

            final Optional<Cluster> cluster = clusterRepository.findByClusterAndRowAndSeat(parsedSeat.a, parsedSeat.b, parsedSeat.c);
            if (cluster.isEmpty()) //존재하지 않는 자리. ex)"c1r21s3"
                continue;
            final Optional<Member> member = memberRepository.findByIntraId(activeSeat.getUser().getId());
            if (member.isEmpty()) //없는 멤버. ex)"mamuraka"
                continue;
            cluster.get().updateMember(member.get());
            clusterRepository.save(cluster.get());
        }
    }

    private Triple<String, Integer, Integer> parsingSeat(final String location) {

        int start = 1;
        int end = location.indexOf('r');
        String c = location.substring(start, end);

        start = end + 1;
        end = location.indexOf('s');
        int r = Integer.parseInt(location.substring(start, end));

        start = end + 1;
        end = location.length();
        int s = Integer.parseInt(location.substring(start, end));

        return new Triple<>(c, r, s);
    }
}
