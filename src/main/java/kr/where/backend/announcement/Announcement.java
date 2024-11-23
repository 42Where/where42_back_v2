package kr.where.backend.announcement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;

    @Column(length = 20, nullable = false)
    private String authorName;

    @Column(nullable = false)
    private LocalDate createAt;

    @Column(nullable = false)
    private LocalDate updateAt;

    public Announcement(String title, String content, String authorName, LocalDate createAt, LocalDate updateAt) {
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
