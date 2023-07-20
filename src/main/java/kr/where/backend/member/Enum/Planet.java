package kr.where.backend.member.Enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Planet {
    gaepo(1), seocho(2), rest(3), error(4);

    Planet(Integer value) {
        this.value = value;
    }

    private final Integer value;

    @JsonValue
    public int getValue() {
        return this.value;
    }

}
