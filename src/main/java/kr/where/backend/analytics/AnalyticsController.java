package kr.where.backend.analytics;

import kr.where.backend.analytics.dto.ResponsePopularImacListDTO;
import kr.where.backend.analytics.dto.ResponseSeatHistoryListDTO;
import kr.where.backend.analytics.swagger.AnalyticsApiDocs;
import kr.where.backend.aspect.LogLevel;
import kr.where.backend.aspect.RequestLogging;
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

    @Override
    @GetMapping("/seat-history")
    public ResponseEntity<ResponseSeatHistoryListDTO> getMemberSeatHistory(@RequestParam("count") final Integer count) {
        return null;
    }

    @Override
    @GetMapping("/popular-imac")
    public ResponseEntity<ResponsePopularImacListDTO> getPopularImac(@RequestParam("count") final Integer count) {
        return null;
    }
}
