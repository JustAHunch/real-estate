import { Page, expect } from '@playwright/test';

export async function login(page: Page, username = 'admin', password = 'admin123') {
  await page.goto('/login');
  await expect(page).toHaveURL(/\/login/);

  await page.locator('input[name="username"]').fill(username);
  await page.locator('input[name="password"]').fill(password);
  await Promise.all([
    page.waitForURL(/\/admin\/properties\/(list|.*)/),
    page.getByRole('button', { name: '로그인' }).click(),
  ]);
}


