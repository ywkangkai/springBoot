CREATE TABLE IF NOT EXISTS tb_project (
    id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL,
    leader_id INT,
    leader_name VARCHAR(255),
    description TEXT,
    is_deleted TINYINT(1),
    create_by_id INT,
    create_by_name VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by_id INT,
    update_by_name VARCHAR(255),
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_task (
     id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(255) NOT NULL,
     description TEXT,
     is_delete TINYINT(1),
     is_archive TINYINT(1),
     is_job TINYINT(1),
     archive_id INT,
     archive_name VARCHAR(255),
     project_id INT
);


CREATE TABLE IF NOT EXISTS tb_task_module (
    id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(255) NOT NULL,
    task_id INT,
    module_id INT,
    create_by_id INT,
    create_by_name VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by_id INT,
    update_by_name VARCHAR(255),
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_module (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_delete TINYINT(1),
    project_id INT,
    create_by_id INT,
    create_by_name VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by_id INT,
    update_by_name VARCHAR(255),
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_module_project FOREIGN KEY (project_id) REFERENCES tb_project(id) ON DELETE CASCADE
    );