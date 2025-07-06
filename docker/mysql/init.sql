-- Initialize MySQL database for Library Management System
CREATE DATABASE IF NOT EXISTS library_db;

USE library_db;

-- Grant privileges to library_user
GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'%';
FLUSH PRIVILEGES;

-- Create tables will be handled by Hibernate DDL auto