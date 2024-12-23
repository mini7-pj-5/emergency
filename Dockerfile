# Amazon Corretto 21을 기반 이미지로 사용
FROM amazoncorretto:17

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/mini7-0.0.1-SNAPSHOT.jar app.jar

# 데이터베이스 파일 복사
COPY src/main/resources/database/em.db /database/em.db

# 포트 노출
EXPOSE 8080

# 'dev','dockerdb' 프로파일을 활성화하여 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=dev,dockerdb", "-jar", "/app.jar"]