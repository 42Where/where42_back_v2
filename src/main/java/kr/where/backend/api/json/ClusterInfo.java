package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterInfo {
    private Integer id;
    private String end_at;
    private String begin_at;
    private User user;
}
