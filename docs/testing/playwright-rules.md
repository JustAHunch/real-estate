# Playwright 테스트 운영 규칙

본 문서는 E2E 테스트(Playwright) 운영을 위한 프로젝트 합의 규칙입니다.

## 범위
- 대상: 사용자 시나리오 및 단위 기능 Cycle(예: CRUD 한 사이클)
- 목적: 회귀 방지, 기능 안정성 검증, 결함의 체계적 수집/개선

## 기본 원칙
1. 단위 기능은 Cycle 단위(예: Create → Read → Update → Delete)로 테스트를 작성합니다.
2. 단위 기능 Cycle의 테스트가 진행되는 동안에는 발견된 결함을 즉시 수정하지 않습니다.
   - 같은 Cycle 내 테스트를 끝까지 수행하여 결함 전후 영향 범위를 확보합니다.
3. 결함(Defect)은 단위 기능별로 개별 MD 파일로 기록합니다.
   - 위치: `docs/testing/defects/<기능키>-<간단제목>.md`
   - 템플릿: `docs/testing/defects/TEMPLATE.md`
4. 수집된 결함 문서를 기반으로 Cycle 종료 후 개선 작업을 진행합니다.
5. 테스트 코드는 화면/도메인 변경에 강건하도록 의미 중심의 셀렉터를 우선합니다.

## 디렉터리/파일 구조
- 테스트 코드: `playwright/tests/**/*.spec.ts`
- 실행 결과: `playwright/test-results/`, `playwright-report/` (자동 생성)
- 결함 문서: `docs/testing/defects/`

## 실행 방법
1. 애플리케이션 실행 (기본 포트는 application.yml 의 8081)
   - Windows PowerShell: `./mvnw spring-boot:run`
2. Playwright 브라우저 바이너리 설치(최초 1회)
   - `npm run test:e2e:install`
3. 테스트 실행
   - 기본: `npm run test:e2e`
   - 헤디드: `npm run test:e2e:headed`
   - UI 모드: `npm run test:e2e:ui`
   - 리포트 보기: `npm run test:e2e:report`

## 테스트 작성 가이드
- 구조: `describe(기능/시나리오) → it(Cycle)`
- 실패 시: trace/screenshot/video가 실패 케이스에 대해 자동 수집됩니다.
- 공통 설정: `playwright.config.ts`의 baseURL, use 옵션을 따릅니다.

## 결함 기록 원칙
- 한 Cycle 내에서 다수 결함이 있어도 항목을 분리하여 기록합니다.
- 재현 절차는 스텝 단위로 상세하게, 기대 결과/실제 결과를 명확히 기술합니다.
- 영향 범위와 잠정 원인을 추정합니다.
- 보안/개인정보 관련 이슈는 민감 정보가 노출되지 않도록 마스킹합니다.

## CI 연계(선택)
- 웹서버 자동 기동이 필요하면 `playwright.config.ts`의 webServer 섹션을 활성화하고, Windows/Unix 호환 명령을 지정합니다.
- CI에서는 `npm ci && npm run test:e2e`를 수행하고 HTML 리포트를 아티팩트로 보관합니다.

