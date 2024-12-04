package kr.where.backend.cluster;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClusterService {

    private final ClusterRepository clusterRepository;

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

    public void updateClusterSeat(List<Cluster> clusterList) {

    }


}
