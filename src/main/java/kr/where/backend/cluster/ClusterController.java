package kr.where.backend.cluster;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.cluster.swagger.ClusterApiDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.info("init 들어옴");
        clusterService.init();
    }

    @GetMapping("/{cluster}")
    public ResponseEntity<ResponseClusterListDTO> getCluster(@PathVariable("cluster") final String cluster,
                                                             @AuthUserInfo final AuthUser authUser) {
        final ResponseClusterListDTO responseClusterListDTO = clusterService.getLoginMember(authUser, cluster);
        return ResponseEntity.status(HttpStatus.OK).body(responseClusterListDTO);
    }
}