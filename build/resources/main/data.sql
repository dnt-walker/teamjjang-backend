-- Users
INSERT INTO users (username, password, email, full_name) 
VALUES ('admin', '$2a$10$HzSQIuGNSyYeHp2FHLQhGu9wXN3kDeZjxBUR/F/Q0YVlmXo8MKozi', 'admin@taskmanager.com', 'Admin User');

INSERT INTO users (username, password, email, full_name) 
VALUES ('user', '$2a$10$BpCLFpnw6K3YDaGTpIzOaeF8KFJDTLPOjhQ0yJiRbC6aFpGBwdZXe', 'user@taskmanager.com', 'Normal User');

-- User Roles
INSERT INTO user_roles (user_id, role) 
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';

INSERT INTO user_roles (user_id, role) 
SELECT id, 'ROLE_USER' FROM users WHERE username = 'admin';

INSERT INTO user_roles (user_id, role) 
SELECT id, 'ROLE_USER' FROM users WHERE username = 'user';

-- Projects
INSERT INTO projects (name, description, start_date, end_date, manager, active)
VALUES ('웹 애플리케이션 개발', '고객을 위한 새로운 전자상거래 웹 애플리케이션 개발', '2025-04-01', '2025-08-31', 'admin', true);

INSERT INTO projects (name, description, start_date, end_date, manager, active)
VALUES ('모바일 앱 리디자인', '기존 모바일 애플리케이션 UX/UI 개선 프로젝트', '2025-04-15', '2025-07-15', 'admin', true);

INSERT INTO projects (name, description, start_date, end_date, manager, active)
VALUES ('데이터베이스 최적화', '시스템 성능 향상을 위한 데이터베이스 구조 개선', '2025-05-01', '2025-06-30', 'user', true);

-- Tasks for Project 1 (웹 애플리케이션 개발)
INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('요구사항 분석', '고객 요구사항 수집 및 분석', '2025-04-01', '2025-04-15', 'admin', 1);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('UI/UX 설계', '웹 애플리케이션 UI/UX 디자인 및 프로토타입 개발', '2025-04-16', '2025-05-15', 'admin', 1);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('백엔드 개발', 'API 및 서버 로직 구현', '2025-05-01', '2025-07-15', 'user', 1);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('프론트엔드 개발', '사용자 인터페이스 구현', '2025-05-15', '2025-07-31', 'user', 1);

-- Tasks for Project 2 (모바일 앱 리디자인)
INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('현행 앱 분석', '기존 모바일 앱 사용성 및 디자인 평가', '2025-04-15', '2025-04-30', 'admin', 2);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('새로운 디자인 개발', '개선된 UI/UX 디자인 작업', '2025-05-01', '2025-06-15', 'admin', 2);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('개발 및 구현', '디자인 변경사항 코드 구현', '2025-06-01', '2025-07-10', 'user', 2);

-- Tasks for Project 3 (데이터베이스 최적화)
INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('데이터베이스 분석', '현재 데이터베이스 성능 평가 및 문제점 파악', '2025-05-01', '2025-05-15', 'user', 3);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('스키마 개선', '데이터베이스 스키마 재설계 및 최적화', '2025-05-16', '2025-06-15', 'user', 3);

INSERT INTO tasks (name, description, start_date, planned_end_date, creator, project_id)
VALUES ('쿼리 최적화', '주요 쿼리 성능 개선', '2025-06-01', '2025-06-30', 'admin', 3);

-- Task Assignees
INSERT INTO task_assignees (task_id, assignee) VALUES (1, 'admin');
INSERT INTO task_assignees (task_id, assignee) VALUES (1, 'user');
INSERT INTO task_assignees (task_id, assignee) VALUES (2, 'admin');
INSERT INTO task_assignees (task_id, assignee) VALUES (3, 'user');
INSERT INTO task_assignees (task_id, assignee) VALUES (4, 'admin');
INSERT INTO task_assignees (task_id, assignee) VALUES (4, 'user');
INSERT INTO task_assignees (task_id, assignee) VALUES (5, 'admin');
INSERT INTO task_assignees (task_id, assignee) VALUES (6, 'admin');
INSERT INTO task_assignees (task_id, assignee) VALUES (7, 'user');
INSERT INTO task_assignees (task_id, assignee) VALUES (8, 'user');
INSERT INTO task_assignees (task_id, assignee) VALUES (9, 'user');
INSERT INTO task_assignees (task_id, assignee) VALUES (10, 'admin');
INSERT INTO task_assignees (task_id, assignee) VALUES (10, 'user');

-- Jobs for Tasks
INSERT INTO jobs (task_id, name, assigned_user, description, start_time, end_time, completed)
VALUES (1, '고객 인터뷰', 'admin', '주요 고객 5명과 인터뷰 진행', '2025-04-02 10:00:00', '2025-04-05 18:00:00', true);

INSERT INTO jobs (task_id, name, assigned_user, description, start_time, end_time, completed)
VALUES (1, '요구사항 문서 작성', 'user', '수집된 요구사항 정리 및 문서화', '2025-04-06 09:00:00', '2025-04-15 17:00:00', false);

INSERT INTO jobs (task_id, name, assigned_user, description, start_time, end_time, completed)
VALUES (2, '와이어프레임 디자인', 'admin', '주요 화면 와이어프레임 설계', '2025-04-16 09:00:00', '2025-04-30 17:00:00', false);

INSERT INTO jobs (task_id, name, assigned_user, description, start_time, end_time, completed)
VALUES (3, 'API 설계', 'user', 'RESTful API 설계 및 문서화', '2025-05-01 09:00:00', '2025-05-15 17:00:00', false);

INSERT INTO jobs (task_id, name, assigned_user, description, start_time, end_time, completed)
VALUES (5, '사용자 피드백 수집', 'admin', '현행 앱에 대한 사용자 의견 취합', '2025-04-15 09:00:00', '2025-04-22 17:00:00', false);

INSERT INTO jobs (task_id, name, assigned_user, description, start_time, end_time, completed)
VALUES (8, '데이터베이스 쿼리 로그 분석', 'user', '느린 쿼리 식별 및 병목 현상 분석', '2025-05-01 09:00:00', '2025-05-10 17:00:00', false);
