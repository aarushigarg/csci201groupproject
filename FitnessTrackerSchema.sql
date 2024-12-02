DROP DATABASE IF EXISTS FitnessTracker;
CREATE DATABASE FitnessTracker;

USE FitnessTracker;

CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(128) UNIQUE NOT NULL,
    hashed_password VARCHAR(64) NOT NULL,
    weight_pounds INT,
    height_inches INT,
    age INT,
    gender CHAR(1),
    goal VARCHAR(100)
);

CREATE TABLE Exercise (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    date DATE NOT NULL,
    name VARCHAR(100) NOT NULL,
    repetitions INT,
    sets INT,
    duration_mins INT,
    is_ai_suggestion BOOLEAN DEFAULT false,
    is_completed BOOLEAN DEFAULT false,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
