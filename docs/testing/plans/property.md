# Property 테스트 계획 및 시나리오

## 목표
- Property 도메인의 CRUD 사이클을 단위 기능(Cycle)로 검증
- 회귀 테스트 기반 마련 및 결함 수집/개선

## 사전 조건
- 서버 실행: http://localhost:8081
- 초기 로그인/권한 요구 시, 공용 테스트 계정 또는 익명 접근 경로 정의 필요
- 테스트 데이터: 고유 식별 가능한 테스트용 명칭 사용(예: "E2E Property <timestamp>")

## 범위
- 목록 조회(Read List)
- 생성(Create)
- 상세 조회(Read Detail)
- 수정(Update)
- 삭제(Delete)

## 공통 가이드
- 의미 기반 셀렉터 사용(Role, Label, Placeholder, Test ID)
- 결함은 Cycle 완료 후 `docs/testing/defects`에 기능별 MD로 기록

## 시나리오: Property CRUD 사이클
1) 목록 페이지 진입
- 경로: /properties
- 기대: 목록 렌더링, 페이지 타이틀/헤더 노출

2) 생성(Create)
- [버튼] 부동산 추가 클릭
- [입력] 명칭, 유형, 주소 등 필수값 입력
- [버튼] 저장 클릭
- 기대: 성공 알림/신규 행 노출/상세 페이지로 이동

3) 상세 조회(Read Detail)
- 방금 생성한 항목 클릭 또는 자동 이동
- 기대: 생성한 값이 상세에 반영되어 노출

4) 수정(Update)
- [버튼] 수정 클릭
- [입력] 일부 값 변경 → 저장
- 기대: 변경 내용 반영 확인(목록/상세)

5) 삭제(Delete)
- [버튼] 삭제 클릭 → 확인
- 기대: 목록에서 해당 항목 미노출

## 비기능/예외
- 유효성 검증: 필수값 미입력 시 에러 메시지
- 중복 데이터: 동일 주소/식별자 중복 거절 처리(정책에 따름)
- 권한: 비인가 사용자의 접근 제한/리다이렉트

## 산출물
- 테스트 코드: `playwright/tests/property.spec.ts`
- 결함 문서: `docs/testing/defects/property-*.md`
