package kr.where.backend.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RedisTestSupport {

	// JUnit이 아닌 우리 코드가 라이프사이클을 관리 (stop 호출하지 않음)
	private static final GenericContainer<?> REDIS =
		new GenericContainer<>("redis:7-alpine")
			.withExposedPorts(6379)
			.waitingFor(Wait.forListeningPort());
	private static volatile boolean STARTED = false;

	private static synchronized void ensureStarted() {
		if (!STARTED) {
			REDIS.start();   // JVM 내에서 단 한 번만 기동
			STARTED = true;
		}
	}

	@DynamicPropertySource
	static void redisProps(DynamicPropertyRegistry registry) {
		ensureStarted();
		registry.add("spring.data.redis.host", REDIS::getHost);
		registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
		// 필요시 비밀번호, DB 등 추가
	}

	@Autowired(required = false)
	private StringRedisTemplate srt;

	@BeforeEach
	void flushAll() {
		if (srt != null) {
			srt.getRequiredConnectionFactory()
				.getConnection()
				.serverCommands()
				.flushAll();
		}
	}
}