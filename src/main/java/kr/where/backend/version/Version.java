package kr.where.backend.version;

import jakarta.persistence.*;
import kr.where.backend.version.exception.VersionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String latestVersion;

    private String osType;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    private static final String versionRegex = "^\\d+\\.[0-9]\\.[0-9]$";

    public Version(final String latestVersion, final String os) {
        this.latestVersion = latestVersion;
        this.osType = os;
    }

    public String checkValidVersionFormat(final String version) {
        if (!version.matches(versionRegex)) {
            throw new VersionException.InvalidVersionFormatException();
        }
        return version;
    }

    public void updateVersion(final String version) {
        this.latestVersion = version;
    }
}
