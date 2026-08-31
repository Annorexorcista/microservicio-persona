CREATE TABLE IF NOT EXISTS person (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_person_email (email)
);

CREATE TABLE IF NOT EXISTS person_bootcamp (
    id BIGINT NOT NULL AUTO_INCREMENT,
    person_id BIGINT NOT NULL,
    bootcamp_id BIGINT NOT NULL,
    launch_date DATE NOT NULL,
    duration_days INT NOT NULL,
    enrolled_at DATE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_person_bootcamp (person_id, bootcamp_id),
    CONSTRAINT fk_person_bootcamp_person
        FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE
);
