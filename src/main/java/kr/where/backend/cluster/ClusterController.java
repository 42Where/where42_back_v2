package kr.where.backend.cluster;

import kr.where.backend.api.json.Cluster;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<List<Cluster>> getClusterInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(clusterService.getClusterSeat());
    }
}
