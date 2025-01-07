package kr.where.backend.seatHistory;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.seatHistory.dto.ResponseHistoryDTO;
import kr.where.backend.seatHistory.swagger.SeatHistoryApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/seat-history")
public class SeatHistoryController implements SeatHistoryApiDocs {
    private final SeatHistoryService seatHistoryService;

    @GetMapping("")
    public ResponseEntity<List<ResponseHistoryDTO>> getPopularSeatHistory(final @AuthUserInfo AuthUser authUser) {
        return ResponseEntity.ok(
                seatHistoryService.getPopularSeatHistory(authUser.getIntraId())
        );
    }
}
