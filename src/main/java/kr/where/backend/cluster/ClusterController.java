package kr.where.backend.cluster;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.cluster.dto.ResponseMostPopularSeatDTO;
import kr.where.backend.cluster.swagger.ClusterApiDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{cluster}")
    public ResponseEntity<ResponseClusterListDTO> getCluster(@PathVariable("cluster") final String cluster,
                                                             @AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.ok(clusterService.getLoginMember(authUser, cluster));
    }

    @GetMapping("/popular")
    public ResponseEntity<ResponseMostPopularSeatDTO> getMostPopularSeat(@RequestParam("count") final Integer count) {
        return ResponseEntity.ok(clusterService.getMostPopularSeat(count));
    }
}
