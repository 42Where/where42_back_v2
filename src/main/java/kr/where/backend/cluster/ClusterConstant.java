package kr.where.backend.cluster;

import lombok.Getter;

@Getter
public enum ClusterConstant {
    CLUSTER_REGEX("^c(x)?\\d+$", null),
    ALL_NUMBER("\\d", null),
    ALL_STRING("[^\\d]", null),
    EMPTY_STRING("", null),
    CLUSTER_C("c", 1, 6),
    CLUSTER_CX("cx", 1, 2);

    private final String stringValue;
    private final Integer minValue;
    private final Integer maxValue;

    ClusterConstant(String stringValue, Integer minValue, Integer maxValue) {
        this.stringValue = stringValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    ClusterConstant(String stringValue, Integer minValue) {
        this(stringValue, minValue, null);
    }
}
