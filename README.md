# 부동산 관리 시스템

Spring Boot 기반의 부동산 매물 관리 및 엑셀 업로드/다운로드 시스템

## 기술 스택

- **Backend**: Spring Boot 3.3.5, Java 17
- **Database**: PostgreSQL (프로덕션), H2 (개발/테스트)
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **View**: Thymeleaf
- **Build**: Maven

## 시작하기

### 사전 요구사항

1. **JDK 17 이상** 설치
2. **PostgreSQL 설치 및 설정** (자세한 내용은 [POSTGRESQL_SETUP.md](POSTGRESQL_SETUP.md) 참조)
3. **Maven** (프로젝트에 포함된 Maven Wrapper 사용 가능)

### 데이터베이스 설정

1. PostgreSQL 설치 (Windows)
```bash
# PostgreSQL 다운로드
https://www.postgresql.org/download/windows/
```

2. 데이터베이스 생성
```sql
CREATE DATABASE realestate;
```

3. `application.yml` 설정 확인
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/realestate
    username: postgres
    password: your_password  # 본인의 PostgreSQL 비밀번호로 변경
```

### 애플리케이션 실행

#### 개발 환경 (기본 프로필)
```bash
# Windows
mvnw.cmd spring-boot:run

# 또는
mvnw.cmd clean install
java -jar target/real-estate-0.0.1-SNAPSHOT.jar
```

#### 프로덕션 환경
```bash
# 환경변수 설정
set DB_PASSWORD=your_password
set SERVER_PORT=8080

# 프로덕션 프로필로 실행
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod

# 또는 jar 실행
java -jar -Dspring.profiles.active=prod target/real-estate-0.0.1-SNAPSHOT.jar
```

### 접속 정보

- **애플리케이션**: http://localhost:8080
- **PostgreSQL 기본 포트**: 5432

## 프로젝트 구조

```
real-estate/
├── src/
│   ├── main/
│   │   ├── java/com/hunch/realestate/
│   │   │   ├── config/          # 설정 클래스
│   │   │   ├── domain/          # Entity 및 DTO
│   │   │   ├── repository/      # JPA Repository
│   │   │   ├── service/         # 비즈니스 로직
│   │   │   └── web/             # Controller
│   │   └── resources/
│   │       ├── application.yml           # 기본 설정
│   │       ├── application-prod.yml      # 프로덕션 설정
│   │       └── templates/                # Thymeleaf 템플릿
│   └── test/
├── data/                        # JSON 데이터 파일
├── POSTGRESQL_SETUP.md          # PostgreSQL 설치 가이드
└── pom.xml
```

## 주요 기능

- ✅ 부동산 매물 관리 (CRUD)
- ✅ 사용자 인증 및 권한 관리 (Spring Security)
- ✅ 엑셀 업로드/다운로드
- ✅ JPA를 통한 데이터 영속성 관리
- ✅ PostgreSQL 데이터베이스 연동

## 환경별 설정

### 개발 환경 (application.yml)
- H2 인메모리 DB 또는 PostgreSQL 로컬
- SQL 로그 활성화
- Hot Reload 활성화
- ddl-auto: update

### 프로덕션 환경 (application-prod.yml)
- PostgreSQL
- SQL 로그 비활성화
- 캐시 활성화
- ddl-auto: validate
- 환경변수를 통한 보안 설정

## 환경변수 설정

프로덕션 환경에서는 다음 환경변수를 설정하세요:

```bash
# Windows CMD
set DB_URL=jdbc:postgresql://localhost:5432/realestate
set DB_USERNAME=postgres
set DB_PASSWORD=your_secure_password
set SERVER_PORT=8080
set STORAGE_BASE_PATH=C:/realestate/data
set LOG_PATH=C:/realestate/logs

# Windows PowerShell
$env:DB_PASSWORD="your_secure_password"

# 시스템 환경변수로 영구 설정
setx DB_PASSWORD "your_secure_password"
```

## 데이터베이스 마이그레이션

### 백업
```bash
pg_dump -U postgres -d realestate > backup_$(date +%Y%m%d).sql
```

### 복원
```bash
psql -U postgres -d realestate < backup_20241021.sql
```

## 트러블슈팅

### PostgreSQL 연결 오류
1. PostgreSQL 서비스가 실행 중인지 확인
2. `application.yml`의 비밀번호 확인
3. 방화벽 설정 확인 (포트 5432)

### Maven 빌드 오류
```bash
mvnw.cmd clean install -U
```

### 포트 충돌
```bash
# 8080 포트 사용 중인 프로세스 확인
netstat -ano | findstr :8080

# 프로세스 종료
taskkill /PID [프로세스ID] /F
```

## 개발 도구

- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **DB 관리**: pgAdmin 4 / DBeaver
- **API 테스트**: Postman / cURL

## 참고 문서

- [PostgreSQL 설치 가이드](POSTGRESQL_SETUP.md)
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [PostgreSQL 공식 문서](https://www.postgresql.org/docs/)

## 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다.
