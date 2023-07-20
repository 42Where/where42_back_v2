package kr.where.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "http response dto")
public class ResponseWithData<T> {
    @Schema(description = "상태코드")
    private int statusCode;

    @Schema(description = "응답 메세지")
    private String responseMsg;

    @Schema(description = "응답 데이터(application/json)")
    private T data;

    public static<T> ResponseWithData<T> res(final int statusCode, final String responseMsg, final T t) {
        return ResponseWithData.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMsg(responseMsg)
                .build();
    }
}
