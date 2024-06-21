FROM xldevops/jdk17-alpine

#
#WORKDIR /app
## 의존성을  의존성 설치 앞에서 복사해서 코드 수정 후 빠르게 빌드되도록
#COPY ./gradlew .
#COPY ./gradle .
#COPY ./build.gradle .
#COPY ./settings.gradle .
#COPY ./src .
#
#
##빌드
#CMD ["./gradlew", "clean", "build"]
#
## gradlew 실행권한 부여
#RUN chmod +x ./gradlew
#RUN microdnf install findutils
## jar 파일 생성
#RUN ./gradlew bootJar


VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar
# or Maven
# ARG JAR_FILE_PATH=target/*.jar

COPY ${JAR_FILE} /app.jar

#ENV PROFILES=${PROFILES} ENV=${ENV} DB_HOST=${DB_HOST} DB_NAME=${DB_NAME} DB_USERNAME=${DB_USERNAME} DB_PASSWORD=${DB_PASSWORD}

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]