package kr.where.backend.cluster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Cluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String cluster;

    private Integer rowIndex;

    private Integer seat;

    public Cluster(final String cluster, final Integer row, final Integer seat) {
        this.cluster = cluster;
        this.rowIndex = row;
        this.seat = seat;
    }
}
