package kr.where.backend.version;

import jakarta.persistence.*;
import kr.where.backend.version.exception.VersionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
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

    public Version(String latestVersion, String os) {
        this.latestVersion = checkValidVersionFormat(latestVersion);
        this.osType = OsTypes.checkAllowedOs(os).toString().toLowerCase();
    }

    private String checkValidVersionFormat(String version) {
        String versionRegex = "^\\d+\\.\\d+\\.\\d+$";
        if (!version.matches(versionRegex)) {
            throw new VersionException.InvalidVersionFormatException();
        }

        return version;
    }
}
