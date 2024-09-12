package kr.where.backend.version;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class VersionServiceTest {

    @Autowired
    VersionService versionService;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    Version version;

    @Test
    public void versionUpdate() {

    }
}
