package kr.where.backend.cluster.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.cluster.dto.ResponseMostPopularSeatDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface ClusterApiDocs {
    @Operation(summary = "getCluster API", description = "클러스터별 imac 로그인한 사람 조회",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                @ApiResponse(responseCode = "200", description = "imac에 로그인한 멤버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseClusterListDTO.class))),
            }
    )
    @GetMapping("/{cluster}")
    public ResponseEntity<ResponseClusterListDTO> getCluster(@PathVariable("cluster") final String cluster, @AuthUserInfo final AuthUser authUser);

    @Operation(summary = "initCluster API", description = "DB의 Cluster 데이터 모두 초기화",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "초기화 성공", content = @Content(schema = @Schema(type = "string"))),
            }
    )
    @PostMapping("/init")
    public void init();


    @Operation(summary = "Most Popular Seat API", description = "클러스터 내 가장 인기 있는 자리 조회",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseMostPopularSeatDTO.class))),
            }
    )
    @GetMapping("/popular")
    public ResponseEntity<ResponseMostPopularSeatDTO> getMostPopularSeat();

}
