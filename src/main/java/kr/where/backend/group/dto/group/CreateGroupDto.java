package kr.where.backend.group.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupDto {

//    @NotNull
//    @JsonProperty("memberIntraId")
    private Long memberIntraId;
//    @Size(max = 40)
//    @NotNull
//    @JsonProperty("groupName")
    private String groupName;

}
