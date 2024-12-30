package kr.where.backend.cluster;

import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.location.LocationRepository;
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
}
