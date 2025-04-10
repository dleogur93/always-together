# 포토폴리오 설정 및 배포 

## 기본 환경

### 백엔드

- **언어**: Java 17 (Azul Zulu 17.0.8)
- **빌드 도구**: Gradle
- **프레임워크**: Spring Boot 3.2.2

### 사용 기술

#### Spring Boot 핵심 스타터

- `spring-boot-starter-web`: 웹 애플리케이션 개발
- `spring-boot-starter-thymeleaf`: Thymeleaf 템플릿 엔진
- `spring-boot-starter-data-jpa`: JPA (Hibernate) 사용
- `spring-boot-starter-security`: Spring Security
- `spring-boot-starter-validation`: 데이터 유효성 검사
- `spring-boot-starter-mail`: 이메일 전송

#### ORM & QueryDSL

- `spring-boot-starter-data-jpa`: JPA 기반 데이터 처리
- `com.querydsl:querydsl-core:5.0.0`: QueryDSL
- `com.querydsl:querydsl-jpa:5.0.0:jakarta`: JPA용 QueryDSL
- `com.querydsl:querydsl-apt:5.0.0:jakarta`: QueryDSL 코드 생성

#### 데이터베이스

- MySQL (`com.mysql:mysql-connector-j`)

#### CI/CD

- **Github Actions** 이용한 간단한 CI/CD

