# JDK11 이미지 사용
FROM openjdk:17-jdk

# VOLUME 설정
VOLUME /tmp

# Jar파일 위치 변수 정의
ARG JAR_FILE="build/libs/*.jar"

# Host에 있는 build파일을 Docker 컨테이너에 app.jar로 복사
COPY ${JAR_FILE} where42.jar

# 도커 이미지가 run 될 시에 수행할 동작
ENTRYPOINT ["java", "-jar", "/where42.jar"]