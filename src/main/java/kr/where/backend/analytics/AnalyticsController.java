package kr.where.backend.analytics;

import kr.where.backend.analytics.dto.ResponseAnalyticsDTO;
import kr.where.backend.analytics.swagger.AnalyticsApiDocs;
import kr.where.backend.aspect.LogLevel;
import kr.where.backend.aspect.RequestLogging;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestLogging(level = LogLevel.INFO)
@RequestMapping("v3/analytics")
public class AnalyticsController implements AnalyticsApiDocs {
    private final AnalyticsService analyticsService;

    @Override
    @GetMapping("/seat-history")
    public ResponseEntity<ResponseAnalyticsDTO> getMemberSeatHistory(@AuthUserInfo final AuthUser authUser,
                                                                     @RequestParam("count") final Integer count) {
        return ResponseEntity.ok(
                analyticsService.getMemberImacUsageAnalyticsData(authUser.getIntraId(), count)
        );
    }

    @Override
    @GetMapping("/popular-imac")
    public ResponseEntity<ResponseAnalyticsDTO> getPopularImac(@AuthUserInfo final AuthUser authUser,
                                                               @RequestParam("count") final Integer count) {
        return ResponseEntity.ok(
                analyticsService.getImacUsageAnalyticsData(count)
        );
    }
}
