package kr.where.backend.cluster;

public enum ClusterLayout {
    CLUSTER_1(9, 7),
    CLUSTER_2(10, 8),
    CLUSTER_3(9, 7),
    CLUSTER_4(10, 8),
    CLUSTER_5(9, 7),
    CLUSTER_6(10, 8),
    CLUSTER_X1_1(4),
    CLUSTER_X1_2(4),
    CLUSTER_X1_3(4),
    CLUSTER_X1_4(8),
    CLUSTER_X1_5(8),
    CLUSTER_X2_1(4),
    CLUSTER_X2_2(10),
    CLUSTER_X2_3(8),
    CLUSTER_X2_4(6),
    CLUSTER_X2_5(6),
    CLUSTER_X2_6(8),
    CLUSTER_X2_7(10),
    CLUSTER_X2_8(4);

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
    ClusterLayout(final int seat) {
        this.seat = seat;
    }

    public int row;
    public int seat;
}
