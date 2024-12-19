package kr.where.backend.cluster;

public enum ClusterLayout {
    CLUSTER_1(9, 7),
    CLUSTER_2(10, 8),
    CLUSTER_3(9, 7),
    CLUSTER_4(10, 8),
    CLUSTER_5(9, 7),
    CLUSTER_6(10, 8);

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    ClusterLayout(final int row, final int seat) {
        this.row = row;
        this.seat = seat;
    }

    public int row;
    public int seat;
}
