# 프로젝트 설정 및 배포 가이드

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

### 프론트엔드 & 빌드 설정

- **Node.js & npm**
  - Node.js 18.12.1 / npm 8.19.2 사용
  - `com.github.node-gradle.node` 플러그인 사용
- **정적 파일 경로**: `src/main/resources/static`

---

## AWS 배포 설정

### 1. EC2 보안 그룹 설정

EC2 보안 그룹에서 인바운드 규칙을 설정하여 애플리케이션 접근을 허용해야 합니다.

- **포트 열기**
  - 80 (HTTP)
  - 443 (HTTPS)
  - 22 (SSH, 필요 시)

### 2. Nginx 리버스 프록시 설정

Spring Boot 애플리케이션을 직접 실행하는 것이 아니라, Nginx를 통해 요청을 프록시하도록 설정합니다.

1. Nginx 설치

```bash
sudo yum install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

2. Nginx 설정 파일 수정 (`/etc/nginx/conf.d/default.conf`)

```nginx
server {
    listen 80;
    server_name alwaystogether.store;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

3. Nginx 재시작

```bash
sudo systemctl restart nginx
```

### 3. Route 53 및 DNS 설정

1. 도메인 네임서버 확인

```bash
nslookup alwaystogether.store
```

2. Gabia에서 AWS 네임서버로 변경
3. Route 53에서 A 레코드를 EC2 퍼블릭 IP로 설정

---

## 배포 후 점검 사항

- `curl -I http://alwaystogether.store` 로 응답 확인
- `sudo journalctl -u nginx --no-pager | tail -n 50` 로 Nginx 로그 확인
- `sudo systemctl status nginx` 및 `sudo systemctl status your-app.service`로 서비스 상태 확인

