import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: 'playwright/tests',
  timeout: 30_000,
  expect: {
    timeout: 5_000,
  },
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: process.env.BASE_URL || 'http://localhost:8081',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
  },
  // 서버는 외부에서 미리 실행하는 것을 기본으로 합니다.
  // 필요 시 아래 주석을 OS에 맞게 수정하여 사용하세요.
  // webServer: {
  //   command: process.platform === 'win32' ? 'mvnw.cmd spring-boot:run' : './mvnw spring-boot:run',
  //   url: 'http://localhost:8081',
  //   reuseExistingServer: true,
  //   timeout: 120_000,
  // },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    // 필요 시 아래 브라우저를 활성화하세요.
    // { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
    // { name: 'webkit', use: { ...devices['Desktop Safari'] } },
  ],
  outputDir: 'playwright/test-results',
});


