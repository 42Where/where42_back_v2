package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ClusterInfo {
    private Integer id;
    private String end_at;
    private String begin_at;
    private String host;
    private User user;
}
