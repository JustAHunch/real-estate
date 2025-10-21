# PostgreSQL 설치 및 설정 가이드 (Windows)

## 1. PostgreSQL 설치

### 다운로드 및 설치
1. PostgreSQL 공식 사이트 접속: https://www.postgresql.org/download/windows/
2. Windows x86-64 installer 다운로드 (최신 버전 권장)
3. 설치 프로그램 실행

### 설치 시 설정
- **Port**: 5432 (기본값)
- **Superuser Password**: `postgres` (또는 원하는 비밀번호)
- **Locale**: Korean, Korea (또는 English, United States)
- **Components**: 
  - PostgreSQL Server (필수)
  - pgAdmin 4 (GUI 관리 도구, 권장)
  - Command Line Tools (권장)

## 2. 전용 사용자 계정 생성

보안을 위해 애플리케이션 전용 사용자 계정을 생성하는 것을 권장합니다.

### 방법 1: pgAdmin 4 사용 (GUI)
1. pgAdmin 4 실행
2. Servers > PostgreSQL > Login/Group Roles > 우클릭 > Create > Login/Group Role
3. General 탭:
   - Name: `beom_realestate`
4. Definition 탭:
   - Password: `beomrealestate12#$`
   - Password expires: 체크 해제
5. Privileges 탭:
   - Can login?: Yes
   - Superuser?: No
   - Create roles?: No
   - Create databases?: Yes
   - Inherit rights from the parent roles?: Yes
6. Save 클릭

### 방법 2: 명령줄 사용
```bash
# PostgreSQL 관리자 계정으로 접속
psql -U postgres

# 새 사용자 생성
CREATE USER beom_realestate WITH PASSWORD 'beomrealestate12#$';

# 데이터베이스 생성 권한 부여
ALTER USER beom_realestate CREATEDB;

# 사용자 확인
\du

# 종료
\q
```

## 3. 데이터베이스 생성

### 방법 1: pgAdmin 4 사용 (GUI)
1. pgAdmin 4 실행
2. Servers > PostgreSQL > 우클릭 > Create > Database
3. Database 이름: `realestate`
4. Owner: `beom_realestate`
5. Save 클릭

### 방법 2: 명령줄 사용 (신규 사용자로)
```bash
# 신규 사용자로 접속
psql -U beom_realestate -h localhost

# 데이터베이스 생성
CREATE DATABASE realestate;

# 생성 확인
\l

# 종료
\q
```

### 방법 3: 명령줄 사용 (관리자 권한으로)
```bash
# 관리자로 접속
psql -U postgres

# 데이터베이스 생성 및 소유자 지정
CREATE DATABASE realestate OWNER beom_realestate;

# 생성 확인
\l

# 종료
\q
```

## 4. application.yml 설정 확인

신규 사용자 계정을 사용하도록 설정을 변경하세요.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/realestate
    username: beom_realestate
    password: beomrealestate12#$
```

**보안 주의사항**: 프로덕션 환경에서는 환경변수나 외부 설정 파일로 비밀번호를 관리하세요.

```yaml
# 환경변수 사용 예시
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/realestate}
    username: ${DB_USERNAME:beom_realestate}
    password: ${DB_PASSWORD:beomrealestate12#$}
```

## 5. 연결 테스트

### Spring Boot 애플리케이션 실행
```bash
./mvnw spring-boot:run
```

### 연결 확인 로그
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

## 6. Windows 서비스 관리

### PostgreSQL 서비스 시작/중지
1. `Windows + R` > `services.msc` 실행
2. `postgresql-x64-버전` 서비스 찾기
3. 우클릭 > 시작/중지/재시작

### 명령줄로 관리
```cmd
# 관리자 권한 CMD에서
net start postgresql-x64-16  # 시작
net stop postgresql-x64-16   # 중지
```

## 7. 포트 및 방화벽 설정

### 포트 확인
```bash
netstat -an | findstr :5432
```

### 방화벽 규칙 추가 (필요시)
1. Windows Defender 방화벽 > 고급 설정
2. 인바운드 규칙 > 새 규칙
3. 포트 > TCP > 5432
4. 연결 허용

## 8. 데이터 백업 및 복원

### 백업
```bash
pg_dump -U postgres -d realestate > backup.sql
```

### 복원
```bash
psql -U postgres -d realestate < backup.sql
```

## 9. 트러블슈팅

### "FATAL: password authentication failed"
- application.yml의 비밀번호가 PostgreSQL 설치 시 설정한 비밀번호와 일치하는지 확인

### "Connection refused"
- PostgreSQL 서비스가 실행 중인지 확인
- 포트 번호가 5432인지 확인

### "Database does not exist"
- pgAdmin 또는 psql로 데이터베이스 생성 확인

### HikariCP 연결 풀 오류
- `maximum-pool-size`를 줄이거나 PostgreSQL max_connections 설정 확인

## 10. 성능 최적화 (선택사항)

### postgresql.conf 수정 (위치: C:\Program Files\PostgreSQL\16\data\postgresql.conf)
```ini
# 연결 수
max_connections = 100

# 메모리 설정 (시스템 RAM의 25%)
shared_buffers = 512MB

# 작업 메모리
work_mem = 16MB

# WAL 설정
wal_buffers = 16MB
```

설정 변경 후 PostgreSQL 서비스 재시작 필요

## 11. 유용한 SQL 명령어

```sql
-- 모든 테이블 조회
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public';

-- 테이블 구조 확인
\d table_name

-- 데이터베이스 크기 확인
SELECT pg_size_pretty(pg_database_size('realestate'));

-- 활성 연결 확인
SELECT * FROM pg_stat_activity;
```

## 참고 자료
- PostgreSQL 공식 문서: https://www.postgresql.org/docs/
- pgAdmin 문서: https://www.pgadmin.org/docs/
