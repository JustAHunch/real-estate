-- ==================== 부동산 회사 정보 ====================
DELETE FROM company_info;

INSERT INTO company_info (id, created_at, updated_at,
                          business_name, address1, address2, directions,
                          phone_number, fax_number,
                          manager_position, manager_name, manager_phone, manager_email, manager_photo)
VALUES ('c0a80001-0000-0000-0000-000000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '범 공인중개사사무소 / 삼진종합 건축사무소',
        '경남 창원시 성산구 대원동 36-5',
        '경남 창원시 성산구 대원로 87 센트럴스퀘어 104호 농협은행앞',
        '대원초등학교 도보 1분 센트럴스퀘어 104호 농협은행앞',
        '055-276-1239',
        '055-277-2333',
        '대표 공인중개사 / 사무장',
        '김상민',
        '010-2822-2832',
        'beomreal@gmail.com',
        '/images/beom_logo.png');

-- ==================== 매물 데이터 ====================
DELETE FROM properties WHERE id LIKE '550e8400-e29b-41d4-a716-44665544%';

-- 1. 창원 성산구 아파트 (매매)
INSERT INTO properties (id, created_at, updated_at, address, detail_address, property_type, transaction_type,
                        price, deposit, monthly_rent, area_pyeong, floor, direction, heating_type,
                        move_in_date, move_in_type, bathroom, description, manager_contact,
                        morning_start_time, morning_end_time, afternoon_start_time, afternoon_end_time,
                        is_visible, latitude, longitude)
VALUES ('550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '경상남도 창원시 성산구 대방동', '센트럴스퀘어 104호', 'APARTMENT', 'SALE',
        350000000, 0, 0, 32.5, 5, 'SOUTH', '중앙난방',
        '2024-12-01', '즉시입주가능', '2개(분리형)',
        '성산구 중심가에 위치한 깔끔한 아파트입니다. 학군이 좋고 교통이 편리합니다.', '010-6565-9400',
        '09:00:00', '12:00:00', '14:00:00', '18:00:00',
        true, 35.2276, 128.6811);

-- 2. 창원 의창구 빌라 (전세)
INSERT INTO properties (id, created_at, updated_at, address, detail_address, property_type, transaction_type,
                        price, deposit, monthly_rent, area_pyeong, floor, direction, heating_type,
                        move_in_date, move_in_type, bathroom, description, manager_contact,
                        morning_start_time, morning_end_time, afternoon_start_time, afternoon_end_time,
                        is_visible, latitude, longitude)
VALUES ('550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '경상남도 창원시 의창구 팔용동', '팔용마을 2단지 101호', 'VILLA', 'JEONSE',
        0, 180000000, 0, 20.5, 2, 'EAST', '개별난방',
        '2025-01-15', '협의가능', '1개(욕조있음)',
        '조용한 주거지역의 깔끔한 빌라입니다. 주차 2대 가능합니다.', '010-6565-9400',
        '10:00:00', '12:00:00', '14:00:00', '19:00:00',
        true, 35.2532, 128.6989);

-- 3. 창원 마산합포구 원룸 (월세)
INSERT INTO properties (id, created_at, updated_at, address, detail_address, property_type, transaction_type,
                        price, deposit, monthly_rent, area_pyeong, floor, direction, heating_type,
                        move_in_date, move_in_type, bathroom, description, manager_contact,
                        morning_start_time, morning_end_time, afternoon_start_time, afternoon_end_time,
                        is_visible, latitude, longitude)
VALUES ('550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '경상남도 창원시 마산합포구 월영동', '월영역 도보 5분거리', 'ONE_ROOM', 'MONTHLY',
        0, 5000000, 400000, 8.5, 3, 'SOUTH', '개별난방',
        '2024-11-25', '즉시입주가능', '1개(샤워부스)',
        '월영역 근처 신축 원룸입니다. 풀옵션이며 관리비가 저렴합니다.', '010-6565-9400',
        '10:00:00', '13:00:00', '15:00:00', '20:00:00',
        true, 35.2019, 128.5697);

-- 4. 창원 성산구 상가 (매매)
INSERT INTO properties (id, created_at, updated_at, address, detail_address, property_type, transaction_type,
                        price, deposit, monthly_rent, area_pyeong, floor, direction, heating_type,
                        move_in_date, move_in_type, building_usage, description, manager_contact,
                        morning_start_time, morning_end_time, afternoon_start_time, afternoon_end_time,
                        is_visible, latitude, longitude)
VALUES ('550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '경상남도 창원시 성산구 상남동', '창원대로 메인상가 1층', 'COMMERCIAL', 'SALE',
        450000000, 0, 0, 25.0, 1, 'SOUTH', '중앙냉난방',
        '2024-12-10', '협의가능', '음식점, 카페 가능',
        '성산구 번화가 핵심 상권입니다. 유동인구가 많고 주차장이 완비되어 있습니다.', '010-6565-9400',
        '09:00:00', '13:00:00', '14:00:00', '19:00:00',
        true, 35.2253, 128.6810);

-- 5. 창원 진해구 투룸 (월세)
INSERT INTO properties (id, created_at, updated_at, address, detail_address, property_type, transaction_type,
                        price, deposit, monthly_rent, area_pyeong, floor, direction, heating_type,
                        move_in_date, move_in_type, bathroom, description, manager_contact,
                        morning_start_time, morning_end_time, afternoon_start_time, afternoon_end_time,
                        is_visible, latitude, longitude)
VALUES ('550e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '경상남도 창원시 진해구 여좌동', '진해 중앙시장 인근', 'TWOTHREE_ROOM', 'MONTHLY',
        0, 10000000, 550000, 15.5, 1, 'WEST', '개별난방',
        '2025-01-01', '협의가능', '1개(욕조있음)',
        '진해 중심가에 위치한 투룸입니다. 시장이 가까워 생활이 편리합니다.', '010-6565-9400',
        '10:00:00', '12:00:00', '15:00:00', '19:00:00',
        true, 35.1396, 128.7037);
