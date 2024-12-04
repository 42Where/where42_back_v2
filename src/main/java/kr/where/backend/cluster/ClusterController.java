package kr.where.backend.cluster;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v3/cluster")
@Slf4j
public class ClusterController {

    private final ClusterService clusterService;
    @PostMapping("/init")
    public void init() {
        log.info("init 들어옴");
        clusterService.init();
    }
}
