package kr.where.backend.cluster;

import kr.where.backend.cluster.swagger.ClusterApiDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v3/cluster")
@Slf4j
public class ClusterController implements ClusterApiDocs {

    private final ClusterService clusterService;

    @PostMapping("/init")
    public void init() {
        clusterService.init();
    }
}
