import { test, expect } from '@playwright/test';
import { login } from './helpers/auth';

test.describe('Company 정보 저장 사이클', () => {
  const pagePath = '/admin/company/info';

  test('Load → Fill → Save → Reload values', async ({ page }) => {
    await login(page);
    await page.goto(pagePath);
    await expect(page).toHaveURL(/\/admin\/company\/info/);

    // 헤더 타이틀 확인
    await expect(page.getByRole('heading', { name: /부동산정보 관리/ })).toBeVisible();

    // 입력 값 준비
    const businessName = `E2E 부동산 ${Date.now()}`;
    const phoneNumber = '055-123-4567';
    const address = '경남 창원시 성산구 테스트로 123';
    const detailAddress = '테스트빌딩 101호';
    const directions = '창원중앙역 3번 출구 도보 5분';
    const faxNumber = '055-765-4321';
    const managerName = '홍길동';
    const managerPhone = '010-1111-2222';
    const managerEmail = 'manager@example.com';

    // 폼 채우기 (id 기반)
    await page.locator('#businessName').fill(businessName);
    await page.locator('#phoneNumber').fill(phoneNumber);
    await page.locator('#address').fill(address);
    await page.locator('#detailAddress').fill(detailAddress);
    await page.locator('#directions').fill(directions);
    await page.locator('#faxNumber').fill(faxNumber);
    await page.locator('#managerName').fill(managerName);
    await page.locator('#managerPhone').fill(managerPhone);
    await page.locator('#managerEmail').fill(managerEmail);

    // 저장하기 클릭 및 완료 대기 (AJAX PUT → 성공 토스트/스왈)
    const saveBtn = page.getByRole('button', { name: /저장하기/ });
    await Promise.all([
      page.waitForResponse((res) => res.url().includes('/api/v1/admin/company/info') && res.request().method() === 'PUT' && res.ok()),
      saveBtn.click(),
    ]);

    // 저장 후, loadCompanyInfo 재호출되며 값이 반영되는지 확인을 위해 잠시 대기 후 검증
    await page.waitForTimeout(500); // 짧은 대기(프론트 로딩/토스트 시간)

    await expect(page.locator('#businessName')).toHaveValue(businessName);
    await expect(page.locator('#address')).toHaveValue(address);
    await expect(page.locator('#managerName')).toHaveValue(managerName);
  });
});


