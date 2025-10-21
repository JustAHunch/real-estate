-- PostgreSQL 스키마 초기화 스크립트
-- 이 파일은 필요시 수동으로 실행하거나, spring.sql.init.mode=always 설정 시 자동 실행됩니다.

-- 기존 테이블 삭제 (주의: 데이터 손실)
-- DROP TABLE IF EXISTS transactions CASCADE;
-- DROP TABLE IF EXISTS properties CASCADE;
-- DROP TABLE IF EXISTS company_info CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;

-- 시퀀스 생성 (JPA가 자동 생성하지만, 명시적으로 정의 가능)
-- CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE IF NOT EXISTS properties_seq START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE IF NOT EXISTS transactions_seq START WITH 1 INCREMENT BY 1;

-- 인덱스 생성 (성능 최적화)
-- 사용자 이메일 인덱스
-- CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 매물 상태 및 가격 인덱스
-- CREATE INDEX IF NOT EXISTS idx_properties_status ON properties(status);
-- CREATE INDEX IF NOT EXISTS idx_properties_price ON properties(price);

-- 거래 날짜 인덱스
-- CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date);

-- Full-text search를 위한 설정 (필요시)
-- ALTER TABLE properties ADD COLUMN IF NOT EXISTS search_vector tsvector;
-- CREATE INDEX IF NOT EXISTS idx_properties_search ON properties USING gin(search_vector);

-- 주석: JPA가 자동으로 테이블을 생성하므로 이 파일은 선택사항입니다.
-- ddl-auto: update 설정으로 Entity 변경사항이 자동 반영됩니다.
