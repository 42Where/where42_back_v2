package kr.where.backend.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "42seoul API에서 get한 카뎃 정보")
public class Seoul42 {
   @Schema(description = "IntraId")
   private String login;
   @Schema(description = "맴버 클러스터 위치")
   private String location;
   @Schema(description = "이미지 URL")
   private Image image;
   @JsonProperty("active?")
   @Schema(description = "카뎃이 로그인해 있는지에 대한 상태")
   private boolean active;
   @Schema(description = "로그인한 클러스터 위치 서초 or 개포")
   private String created_at;
}