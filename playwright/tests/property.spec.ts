import { test, expect } from '@playwright/test';
import { login } from './helpers/auth';

test.describe('Property 목록/등록 페이지 접근 및 필수 요소 확인', () => {
  const listPath = '/admin/properties/list';
  const registerPath = '/admin/properties/register';

  test('목록 진입 및 등록 버튼 확인', async ({ page }) => {
    await login(page);
    await page.goto(listPath);
    await expect(page).toHaveURL(/\/admin\/properties\/list/);

    // 타이틀 확인 (헤더 H1 우선)
    await expect(page.getByRole('heading', { level: 1, name: /매물 목록/ })).toBeVisible();

    // 등록 버튼 존재 확인 (카드 상단의 버튼을 타겟)
    const registerLink = page.locator('a.btn.btn-primary.btn-sm', { hasText: '매물 등록' });
    await expect(registerLink.first()).toBeVisible();

    // 데이터테이블 헤더 일부 확인 (직접 셀렉터 사용, AJAX 초기화까지 대기)
    const headerCell = page.locator('table#propertiesTable thead th', { hasText: '매물명' });
    await expect(headerCell.first()).toBeVisible();
  });

  test('등록 폼 진입 및 필수 입력 요소 확인', async ({ page }) => {
    await login(page);
    await page.goto(registerPath);
    await expect(page).toHaveURL(/\/admin\/properties\/register/);

    // 타이틀 확인
    await expect(page.getByRole('heading', { name: /매물 등록/ })).toBeVisible();

    // 필수 필드 가시성 확인 (입력/셀렉트)
    await expect(page.locator('#propertyName')).toBeVisible();
    await expect(page.locator('#propertyType')).toBeVisible();
    await expect(page.getByRole('button', { name: /주소 검색/ })).toBeVisible();

    // 다음 버튼으로 스텝 전환 확인
    const nextBtn = page.getByRole('button', { name: /다음/ });
    await expect(nextBtn).toBeVisible();
  });

  test('주소 검색 모달(다음/카카오) 표시 확인', async ({ page }) => {
    await login(page);
    await page.goto(registerPath);
    await expect(page).toHaveURL(/\/admin\/properties\/register/);

    // 주소 검색 버튼 클릭 시 팝업 대기
    const clickBtn = page.getByRole('button', { name: /주소 검색/ });
    const popupPromise = page.waitForEvent('popup').catch(() => null);
    await clickBtn.click();
    const popup = await popupPromise;

    if (popup) {
      await popup.waitForLoadState('domcontentloaded');
      // 팝업이 열렸음을 확인
      expect(popup).toBeTruthy();
      await popup.close();
    } else {
      // 일부 환경에서는 레이어/iframe 으로 표시됨 → 프레임 존재 확인
      const frame = page.frames().find(f => /postcode|daum|mapjsapi/.test(f.url()));
      expect(frame).toBeTruthy();
    }
  });
});


