CREATE TABLE IF NOT EXISTS tb_project (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
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