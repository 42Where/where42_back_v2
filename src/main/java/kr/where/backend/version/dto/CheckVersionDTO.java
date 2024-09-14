package kr.where.backend.version.dto;

import lombok.Getter;

@Getter
public class CheckVersionDTO {
    private String version;
    private String os;

    public void setOs(String os) {
        this.os = os.toUpperCase();
    }
}