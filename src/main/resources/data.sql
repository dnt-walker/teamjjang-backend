-- Users
INSERT INTO users (username, password, email, full_name)
VALUES ('admin',  '$2a$10$HRGrcgGY6Hy6iGKEEkUwy.3mYmvO7V4Sq6JqwDx2kplnPTTKc3/9S', 'admin@taskmanager.com', 'Admin User');

INSERT INTO users (username, password, email, full_name)
VALUES ('user',  '$2a$10$HRGrcgGY6Hy6iGKEEkUwy.3mYmvO7V4Sq6JqwDx2kplnPTTKc3/9S', 'user@taskmanager.com', 'Normal User');

-- 기존 ROLE 구문 대신 User 자체를 쿼리하여 ID값을 가져오도록 수정
INSERT INTO user_roles (user_id, role)
SELECT user_id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';

INSERT INTO user_roles (user_id, role)
SELECT user_id, 'ROLE_USER' FROM users WHERE username = 'admin';

INSERT INTO user_roles (user_id, role)
SELECT user_id, 'ROLE_USER' FROM users WHERE username = 'user';

-- Projects
INSERT INTO projects (name, description, start_date, end_date, manager, active)
VALUES ('웹 애플리케이션 개발', '고객을 위한 새로운 전자상거래 웹 애플리케이션 개발', '2025-04-01', '2025-08-31', 1, true);

INSERT INTO projects (name, description, start_date, end_date, manager, active)
VALUES ('모바일 앱 리디자인', '기존 모바일 애플리케이션 UX/UI 개선 프로젝트', '2025-04-15', '2025-07-15', 1, true);

INSERT INTO projects (name, description, start_date, end_date, manager, active)
VALUES ('데이터베이스 최적화', '시스템 성능 향상을 위한 데이터베이스 구조 개선', '2025-05-01', '2025-06-30', 2, true);

-- Tasks for Project 1
INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('요구사항 분석', '고객 요구사항 수집 및 분석', '2025-04-01', '2025-04-15', 1, 1);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('UI/UX 설계', '웹 애플리케이션 UI/UX 디자인 및 프로토타입 개발', '2025-04-16', '2025-05-15', 1, 1);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('백엔드 개발', 'API 및 서버 로직 구현', '2025-05-01', '2025-07-15', 2, 1);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('프론트엔드 개발', '사용자 인터페이스 구현', '2025-05-15', '2025-07-31', 2, 1);

-- Tasks for Project 2
INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('현행 앱 분석', '기존 모바일 앱 사용성 및 디자인 평가', '2025-04-15', '2025-04-30', 1, 2);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('새로운 디자인 개발', '개선된 UI/UX 디자인 작업', '2025-05-01', '2025-06-15', 1, 2);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('개발 및 구현', '디자인 변경사항 코드 구현', '2025-06-01', '2025-07-10', 2, 2);

-- Tasks for Project 3
INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('데이터베이스 분석', '현재 데이터베이스 성능 평가 및 문제점 파악', '2025-05-01', '2025-05-15', 2, 3);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('스키마 개선', '데이터베이스 스키마 재설계 및 최적화', '2025-05-16', '2025-06-15', 2, 3);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('쿼리 최적화', '주요 쿼리 성능 개선', '2025-06-01', '2025-06-30', 1, 3);

-- Task Assignees - task_id는 tasks 테이블의 FK
INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'admin' FROM tasks WHERE task_id = 1;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 1;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'admin' FROM tasks WHERE task_id = 2;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 3;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'admin' FROM tasks WHERE task_id = 4;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 4;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'admin' FROM tasks WHERE task_id = 5;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'admin' FROM tasks WHERE task_id = 6;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 7;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 8;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 9;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'admin' FROM tasks WHERE task_id = 10;

INSERT INTO task_assignees (task_id, assignee) 
SELECT task_id, 'user' FROM tasks WHERE task_id = 10;

-- Jobs for Tasks (컬럼 변경에 맞추어 수정)
-- assigned_user 컬럼이 아닌 별도의 job_assignees 테이블에 삽입합니다
INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, '고객 인터뷰', '주요 고객 5명과 인터뷰 진행', '2025-04-02 10:00:00', '2025-04-05 18:00:00', true, 'FS'
FROM tasks WHERE task_id = 1;

-- 작업별 담당자 추가 (job_id 1번에 담당자 admin 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (1, 'admin');

INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, '요구사항 문서 작성', '수집된 요구사항 정리 및 문서화', '2025-04-06 09:00:00', '2025-04-15 17:00:00', false, 'IP'
FROM tasks WHERE task_id = 1;

-- 작업별 담당자 추가 (job_id 2번에 담당자 user 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (2, 'user');

INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, '와이어프레임 디자인', '주요 화면 와이어프레임 설계', '2025-04-16 09:00:00', '2025-04-30 17:00:00', false, 'CT'
FROM tasks WHERE task_id = 2;

-- 작업별 담당자 추가 (job_id 3번에 담당자 admin 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (3, 'admin');

INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, 'API 설계', 'RESTful API 설계 및 문서화', '2025-05-01 09:00:00', '2025-05-15 17:00:00', false, 'CT'
FROM tasks WHERE task_id = 3;

-- 작업별 담당자 추가 (job_id 4번에 담당자 user 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (4, 'user');

INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, '사용자 피드백 수집', '현행 앱에 대한 사용자 의견 취합', '2025-04-15 09:00:00', '2025-04-22 17:00:00', false, 'WT'
FROM tasks WHERE task_id = 5;

-- 작업별 담당자 추가 (job_id 5번에 담당자 admin 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (5, 'admin');

INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, '데이터베이스 쿼리 로그 분석', '느린 쿼리 식별 및 병목 현상 분석', '2025-05-01 09:00:00', '2025-05-10 17:00:00', false, 'CT'
FROM tasks WHERE task_id = 8;

-- 작업별 담당자 추가 (job_id 6번에 담당자 user 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (6, 'user');

-- 여러 담당자를 가진 작업 예제 추가
INSERT INTO jobs (task_id, name, description, start_time, end_time, completed, status)
SELECT task_id, '프로젝트 킥오프 미팅', '프로젝트 시작을 위한 팀 전체 회의', '2025-04-01 10:00:00', '2025-04-01 12:00:00', true, 'FS'
FROM tasks WHERE task_id = 1;

-- 작업별 담당자 추가 (job_id 7번에 담당자 여러 명 추가)
INSERT INTO job_assignees (job_id, assigned_user)
VALUES (7, 'admin');

INSERT INTO job_assignees (job_id, assigned_user)
VALUES (7, 'user');