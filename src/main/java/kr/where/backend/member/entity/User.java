package kr.where.backend.member.entity;

import jakarta.persistence.*;
import lombok.Getter;

@MappedSuperclass
@SequenceGenerator(
        name = "MEMBERS_SEQ_GENERATOR",
        sequenceName = "MEMBERS_SEQ",
        initialValue = 1, allocationSize = 1
)
@Getter
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBERS_SEQ_GENERATOR")
    @Column(name = "member_id")
    protected Long id;

    @Column(name = "member_name", nullable = false, unique = true)
    protected String name;
}
