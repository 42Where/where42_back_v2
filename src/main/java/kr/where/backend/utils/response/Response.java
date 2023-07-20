package kr.where.backend.utils.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class Response {
    private int statusCode;
    private String responseMsg;

    public static Response res(final int statusCode, final String responseMsg) {
        return Response.builder()
                .statusCode(statusCode)
                .responseMsg(responseMsg)
                .build();
    }
}

