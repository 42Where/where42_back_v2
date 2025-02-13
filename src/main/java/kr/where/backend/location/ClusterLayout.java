package kr.where.backend.location;

public enum ClusterLayout {
    C1("c1", 63),
    C2("c2", 80),
    CX1("cx1", 28),
    CX2("cx2", 56),
    C3("c3", 63),
    C4("c4", 80),
    C5("c5", 63),
    C6("c6", 80);

    private final String clusterName;
    private final int totalSeatCount;

    public String getClusterName() {
        return clusterName;
    }

    public int getTotalSeatCount() {
        return totalSeatCount;
    }

    ClusterLayout(final String clusterName, final int totalSeatCount) {
        this.clusterName = clusterName;
        this.totalSeatCount = totalSeatCount;
    }
}
