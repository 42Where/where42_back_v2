package kr.where.backend.cluster.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;

public interface ClusterApiDocs {
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
}
