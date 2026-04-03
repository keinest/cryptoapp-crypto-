CREATE DATABASE IF NOT EXISTS crypto_db;

USE crypto_db;

CREATE TABLE IF NOT EXISTS users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS sessions(
    id VARCHAR(64) PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS crypto_history(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    algorithm ENUM('CESAR', 'VIGENERE', 'RSA', 'AFFINE', 'VERNAM', 'FEISTEL', 'HILL','Recursive Transposition') NOT NULL,
    operation ENUM('ENCRYPT', 'DECRYPT', 'BRUTE_FORCE') NOT NULL,
    input_text TEXT,
    output_text TEXT,
    key_used VARCHAR(255),
    execution_time_ms INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS app_settings(
    setting_key VARCHAR(50) PRIMARY KEY,
    setting_value TEXT,
    description VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO app_settings(setting_key, setting_value, description) VALUES
('app_name', 'Crypto Application', 'Nom de l application'),
('app_version', '1.0', 'Version de l application'),
('max_login_attempts', '3', 'Nombre maximum de tentatives de connexion'),
('session_timeout_minutes', '30', 'Duree de vie des sessions en minutes');

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_sessions_user_id ON sessions(user_id);
CREATE INDEX idx_sessions_expires_at ON sessions(expires_at);
CREATE INDEX idx_crypto_history_user_id ON crypto_history(user_id);
CREATE INDEX idx_crypto_history_created_at ON crypto_history(created_at);

SELECT 'Base de donnees initialisee avec succes!' as message;