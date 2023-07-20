package kr.where.backend.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "JSON 형식의 key:value")
public class KeyValueInfo {

    @Schema(description = "{'key':'value'}", example = "value")
    private String key;
}
