-- ResearchSphere Database Schema
-- Run: mysql -u root -p < researchsphere_db.sql

CREATE DATABASE IF NOT EXISTS researchsphere_db;
USE researchsphere_db;

DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS discussions;
DROP TABLE IF EXISTS uploaded_files;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS activity_logs;
DROP TABLE IF EXISTS milestones;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS team_members;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    leader_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (leader_id) REFERENCES users(id)
);

CREATE TABLE team_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    member_role VARCHAR(30) DEFAULT 'MEMBER',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_team_user (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(80),
    status VARCHAR(30) DEFAULT 'PLANNING',
    team_id BIGINT,
    created_by BIGINT NOT NULL,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    assigned_to BIGINT,
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    status VARCHAR(30) DEFAULT 'TODO',
    due_date DATE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE milestones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    target_date DATE,
    completion_percent INT DEFAULT 0,
    status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE discussions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    discussion_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    content TEXT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (discussion_id) REFERENCES discussions(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    type VARCHAR(40) DEFAULT 'INFO',
    read_flag TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE activity_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE uploaded_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    uploaded_by BIGINT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

-- Demo users (password for all: password123)
-- SHA-256 (UTF-8) of "password123" — must match PasswordUtil.hash()
INSERT INTO users (full_name, email, password_hash, role) VALUES
('System Admin', 'admin@researchsphere.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'ADMIN'),
('Dr. Sarah Faculty', 'faculty@researchsphere.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'FACULTY'),
('John Student', 'student@researchsphere.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'STUDENT'),
('Emily Student', 'emily@researchsphere.edu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'STUDENT');

INSERT INTO teams (name, description, leader_id) VALUES
('AI Research Lab', 'Machine learning and NLP research team', 2),
('Green Energy Group', 'Sustainable energy research', 2);

INSERT INTO team_members (team_id, user_id, member_role) VALUES
(1, 2, 'LEADER'), (1, 3, 'MEMBER'), (1, 4, 'MEMBER'),
(2, 2, 'LEADER'), (2, 3, 'MEMBER');

INSERT INTO projects (title, description, category, status, team_id, created_by, start_date, end_date) VALUES
('Neural Network Optimization', 'Research on efficient training algorithms', 'Computer Science', 'IN_PROGRESS', 1, 2, '2025-01-15', '2025-12-31'),
('Solar Panel Efficiency', 'Improving photovoltaic cell output', 'Engineering', 'PLANNING', 2, 2, '2025-03-01', '2026-06-30');

INSERT INTO tasks (project_id, title, description, assigned_to, priority, status, due_date, created_by) VALUES
(1, 'Literature Review', 'Survey existing papers on NN optimization', 3, 'HIGH', 'IN_PROGRESS', '2025-06-01', 2),
(1, 'Prototype Model', 'Build baseline model in Python', 4, 'MEDIUM', 'TODO', '2025-07-15', 2),
(2, 'Data Collection', 'Gather solar efficiency datasets', 3, 'LOW', 'TODO', '2025-08-01', 2);

INSERT INTO milestones (project_id, title, description, target_date, completion_percent, status) VALUES
(1, 'Phase 1 - Research', 'Complete literature review', '2025-05-30', 75, 'IN_PROGRESS'),
(1, 'Phase 2 - Implementation', 'Working prototype', '2025-09-30', 20, 'PENDING'),
(2, 'Proposal Approval', 'Faculty approval of proposal', '2025-04-15', 100, 'COMPLETED');

INSERT INTO discussions (project_id, title, content, created_by) VALUES
(1, 'Weekly Sync Notes', 'Please share progress updates every Friday.', 2),
(1, 'Dataset Sources', 'Which public datasets should we use?', 3);

INSERT INTO comments (discussion_id, parent_id, content, created_by) VALUES
(1, NULL, 'I will post my update by EOD Friday.', 3),
(2, NULL, 'ImageNet and CIFAR are good starting points.', 2);

INSERT INTO notifications (user_id, title, message, type, read_flag) VALUES
(3, 'Task Assigned', 'You were assigned: Literature Review', 'TASK', 0),
(3, 'Deadline Reminder', 'Literature Review due in 7 days', 'DEADLINE', 0),
(4, 'New Comment', 'New reply on Dataset Sources discussion', 'COMMENT', 0);

INSERT INTO activity_logs (user_id, action, entity_type, entity_id, details) VALUES
(2, 'CREATE_PROJECT', 'PROJECT', 1, 'Created project Neural Network Optimization'),
(3, 'UPDATE_TASK', 'TASK', 1, 'Updated task status to IN_PROGRESS'),
(2, 'CREATE_DISCUSSION', 'DISCUSSION', 1, 'Started discussion Weekly Sync Notes');
