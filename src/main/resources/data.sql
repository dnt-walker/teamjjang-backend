-- 사용자 데이터 초기화
INSERT INTO users (user_id, username, password, email, full_name) 
VALUES (1, 'admin', '$2a$10$iuyUfck6Yw0h.jP5adU26etSidwTpW/4ITzBajhfxf10GAQGytYU6', 'admin@example.com', '관리자');

INSERT INTO users (user_id, username, password, email, full_name) 
VALUES (2, 'user1', '$2a$10$iuyUfck6Yw0h.jP5adU26etSidwTpW/4ITzBajhfxf10GAQGytYU6', 'user1@example.com', '김민수');

INSERT INTO users (user_id, username, password, email, full_name) 
VALUES (3, 'user2', '$2a$10$iuyUfck6Yw0h.jP5adU26etSidwTpW/4ITzBajhfxf10GAQGytYU6', 'user2@example.com', '박지은');

INSERT INTO users (user_id, username, password, email, full_name) 
VALUES (4, 'user3', '$2a$10$iuyUfck6Yw0h.jP5adU26etSidwTpW/4ITzBajhfxf10GAQGytYU6', 'user3@example.com', '이승현');

INSERT INTO users (user_id, username, password, email, full_name) 
VALUES (5, 'user4', '$2a$10$iuyUfck6Yw0h.jP5adU26etSidwTpW/4ITzBajhfxf10GAQGytYU6', 'user4@example.com', '정다영');

-- 사용자 역할 초기화
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_roles (user_id, role) VALUES (1, 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES (2, 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES (3, 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES (4, 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES (5, 'ROLE_USER');

-- 프로젝트 초기화 (statusCode 사용)
INSERT INTO projects (project_id, project_name, project_desc, start_date, end_date, manager_id, project_status,
                      register_id,  modifier_id, registered_date, modified_date)
VALUES (1, '태스크 관리 시스템 개발', '팀 작업 관리를 위한 웹 기반 태스크 관리 시스템 개발', '2025-04-01', '2025-05-31', 1, 'IP', 1, 1,
        CURRENT_TIMESTAMP
(), CURRENT_TIMESTAMP());

INSERT INTO projects (project_id, project_name, project_desc, start_date, end_date, manager_id, project_status, register_id,  modifier_id,
                      registered_date, modified_date)
VALUES (2, '모바일 앱 개발', '크로스 플랫폼 모바일 애플리케이션 개발', '2025-05-01', '2025-07-31', 2, 'CT', 1, 1, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP());

-- 프로젝트 할당 사용자 초기화
INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (1, 1, CURRENT_TIMESTAMP());

INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (1, 2, CURRENT_TIMESTAMP());

INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (1, 3, CURRENT_TIMESTAMP());

INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (1, 4, CURRENT_TIMESTAMP());

INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (1, 5, CURRENT_TIMESTAMP());

INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (2, 3, CURRENT_TIMESTAMP());

INSERT INTO projects_assigned_user (project_id, user_id, registered_date)
VALUES (2, 4, CURRENT_TIMESTAMP());

-- 태스크 초기화
INSERT INTO task (task_id, project_id, task_name, start_date, planned_end_date, completion_date, task_desc,
                  task_status, register_id,  modifier_id, registered_date, modified_date)
VALUES (1, 1, '웹 애플리케이션 설계', '2025-04-01', '2025-04-15', null, '새로운 태스크 관리 시스템의 설계 작업', 'IP',  1, 1, CURRENT_TIMESTAMP
(), CURRENT_TIMESTAMP());

INSERT INTO task (task_id, project_id, task_name, start_date, planned_end_date, completion_date, task_desc,
                  task_status, register_id,  modifier_id, registered_date, modified_date)
VALUES (2, 1, '백엔드 API 개발', '2025-04-05', '2025-04-25', null, '태스크 관리 시스템을 위한 RESTful API 개발', 'CT',
        1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO task (task_id, project_id, task_name, start_date, planned_end_date, completion_date, task_desc,
                  task_status, register_id,  modifier_id, registered_date, modified_date)
VALUES (3, 1, '프론트엔드 개발', '2025-04-10', '2025-05-05', null, '사용자 인터페이스 구현 및 백엔드 API 연동', 'CT',  1, 1, CURRENT_TIMESTAMP
(), CURRENT_TIMESTAMP());

INSERT INTO task (task_id, project_id, task_name, start_date, planned_end_date, completion_date, task_desc,
                  task_status, register_id,  modifier_id, registered_date, modified_date)
VALUES (4, 1, '테스트 및 QA', '2025-04-25', '2025-05-10', null, '시스템 테스트 및 품질 보증 활동', 'CT',  1, 1, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP());

INSERT INTO task (task_id, project_id, task_name, start_date, planned_end_date, completion_date, task_desc,
                  task_status, register_id,  modifier_id, registered_date, modified_date)
VALUES (5, 2, '요구사항 분석', '2025-05-01', '2025-05-15', null, '모바일 앱 요구사항 수집 및 분석', 'CT',  1, 1, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP());

-- 태스크 할당 사용자 초기화
INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (1, 2, CURRENT_TIMESTAMP());

INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (1, 4, CURRENT_TIMESTAMP());

INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (2, 3, CURRENT_TIMESTAMP());

INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (3, 4, CURRENT_TIMESTAMP());

INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (4, 5, CURRENT_TIMESTAMP());

INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (5, 3, CURRENT_TIMESTAMP());

INSERT INTO tasks_assigned_user (task_id, user_id, registered_date)
VALUES (5, 4, CURRENT_TIMESTAMP());

-- 서브태스크(잡) 초기화
INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status, register_id,
                 modifier_id, registered_date, modified_date)
VALUES (1, 1, '요구사항 분석', '시스템 요구사항 수집 및 분석', '2025-04-01 09:00:00', '2025-04-05 18:00:00', 'FS', 1, 1,CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status,register_id,  modifier_id, registered_date, modified_date)
VALUES (2, 1, '시스템 아키텍처 설계', '전체 시스템 아키텍처 설계 및 문서화', '2025-04-06 09:00:00', '2025-04-10 18:00:00', 'IP', 1, 1,CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status, register_id,  modifier_id,
                 registered_date, modified_date)
VALUES (3, 1, '데이터베이스 설계', '데이터베이스 스키마 설계 및 모델링', '2025-04-11 09:00:00', null, 'CT', 1, 1, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP());

INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status, register_id,  modifier_id,
                 registered_date, modified_date)
VALUES (4, 2, 'Task API 개발', 'Task 관련 RESTful API 엔드포인트 구현', '2025-04-05 09:00:00', null, 'CT', 1, 1,CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status, register_id,  modifier_id,
                 registered_date, modified_date)
VALUES (5, 2, 'Job API 개발', 'Job 관련 RESTful API 엔드포인트 구현', '2025-04-13 09:00:00', null, 'CT',1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status, register_id,  modifier_id,
                 registered_date, modified_date)
VALUES (6, 3, '컴포넌트 설계', 'React 컴포넌트 설계 및 구성', '2025-04-10 09:00:00', null, 'CT',1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO job (subtask_id, task_id, subtask_name, subtask_desc, start_time, end_time, subtask_status, register_id,  modifier_id,
                 registered_date, modified_date)
VALUES (7, 3, 'Task 관리 UI 개발', 'Task 관리를 위한 사용자 인터페이스 개발', '2025-04-18 09:00:00', null, 'CT',  1, 1, CURRENT_TIMESTAMP
(), CURRENT_TIMESTAMP());

-- 서브태스크(잡) 할당 사용자 초기화
INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (1, 2, CURRENT_TIMESTAMP());

INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (2, 4, CURRENT_TIMESTAMP());

INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (3, 2, CURRENT_TIMESTAMP());

INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (4, 3, CURRENT_TIMESTAMP());

INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (5, 3, CURRENT_TIMESTAMP());

INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (6, 4, CURRENT_TIMESTAMP());

INSERT INTO jobs_assigned_user (subtask_id, user_id, registered_date)
VALUES (7, 4, CURRENT_TIMESTAMP());

-- H2 데이터베이스의 시퀀스 초기화
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 6;
ALTER TABLE projects ALTER COLUMN project_id RESTART WITH 3;
ALTER TABLE task ALTER COLUMN task_id RESTART WITH 6;
ALTER TABLE job ALTER COLUMN subtask_id RESTART WITH 8;