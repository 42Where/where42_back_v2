package kr.where.backend.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Schema(description = "사용자 정보")
public class AdminInfo {

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "비밀번호", example = "****")
    private String passwd;
}
