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

-- 첨부파일 테이블
CREATE TABLE IF NOT EXISTS attached_files (
    id VARCHAR(36) PRIMARY KEY,
    file_grp_id VARCHAR(50) NOT NULL,
    file_uid VARCHAR(100) NOT NULL UNIQUE,
    file_origin_nm VARCHAR(500) NOT NULL,
    file_type VARCHAR(50),
    file_path VARCHAR(1000) NOT NULL,
    file_nm VARCHAR(500) NOT NULL,
    file_size BIGINT,
    prgm_cd VARCHAR(100),
    syscd VARCHAR(50),
    del_fl VARCHAR(1) DEFAULT 'N',
    input_user_id VARCHAR(100),
    ref01 VARCHAR(500),
    ref02 VARCHAR(500),
    ref03 VARCHAR(500),
    ref04 VARCHAR(500),
    ref05 VARCHAR(500),
    ref06 VARCHAR(500),
    ref07 VARCHAR(500),
    ref08 VARCHAR(500),
    ref09 VARCHAR(500),
    ref10 VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_del_fl CHECK (del_fl IN ('Y', 'N'))
);

-- 첨부파일 인덱스
CREATE INDEX IF NOT EXISTS idx_attached_files_grp_id ON attached_files(file_grp_id);
CREATE INDEX IF NOT EXISTS idx_attached_files_uid ON attached_files(file_uid);
CREATE INDEX IF NOT EXISTS idx_attached_files_prgm_cd ON attached_files(prgm_cd);
CREATE INDEX IF NOT EXISTS idx_attached_files_del_fl ON attached_files(del_fl);

-- 첨부파일 테이블 및 컬럼 주석
COMMENT ON TABLE attached_files IS '첨부파일 정보 테이블';
COMMENT ON COLUMN attached_files.id IS '첨부파일 고유 ID (UUID)';
COMMENT ON COLUMN attached_files.file_grp_id IS '파일 그룹 ID (여러 파일을 하나의 그룹으로 묶음)';
COMMENT ON COLUMN attached_files.file_uid IS '파일 고유 식별자 (UNIQUE)';
COMMENT ON COLUMN attached_files.file_origin_nm IS '원본 파일명';
COMMENT ON COLUMN attached_files.file_type IS '파일 타입 (확장자)';
COMMENT ON COLUMN attached_files.file_path IS '파일 저장 경로';
COMMENT ON COLUMN attached_files.file_nm IS '저장된 파일명 (UUID + 확장자)';
COMMENT ON COLUMN attached_files.file_size IS '파일 크기 (bytes)';
COMMENT ON COLUMN attached_files.prgm_cd IS '프로그램 코드 (어느 기능에서 사용하는 파일인지)';
COMMENT ON COLUMN attached_files.syscd IS '시스템 코드';
COMMENT ON COLUMN attached_files.del_fl IS '삭제 여부 (Y: 삭제됨, N: 사용중)';
COMMENT ON COLUMN attached_files.input_user_id IS '등록 사용자 ID';
COMMENT ON COLUMN attached_files.ref01 IS '참조 필드 1';
COMMENT ON COLUMN attached_files.ref02 IS '참조 필드 2';
COMMENT ON COLUMN attached_files.ref03 IS '참조 필드 3';
COMMENT ON COLUMN attached_files.ref04 IS '참조 필드 4';
COMMENT ON COLUMN attached_files.ref05 IS '참조 필드 5';
COMMENT ON COLUMN attached_files.ref06 IS '참조 필드 6';
COMMENT ON COLUMN attached_files.ref07 IS '참조 필드 7';
COMMENT ON COLUMN attached_files.ref08 IS '참조 필드 8';
COMMENT ON COLUMN attached_files.ref09 IS '참조 필드 9';
COMMENT ON COLUMN attached_files.ref10 IS '참조 필드 10';
COMMENT ON COLUMN attached_files.created_at IS '생성 일시';
COMMENT ON COLUMN attached_files.updated_at IS '수정 일시';

-- 주석: JPA가 자동으로 테이블을 생성하므로 이 파일은 선택사항입니다.
-- ddl-auto: update 설정으로 Entity 변경사항이 자동 반영됩니다.
