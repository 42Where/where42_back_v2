package kr.where.backend.version;

import jakarta.persistence.*;
import kr.where.backend.version.exception.VersionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Version {
    private static final String versionRegex = "^\\d+\\.\\d+\\.\\d+$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String latestVersion;

    @Column(unique = true)
    private String osType;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public Version(final String latestVersion, final String os) {

        this.latestVersion = latestVersion;
        this.osType = os.toUpperCase();
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
