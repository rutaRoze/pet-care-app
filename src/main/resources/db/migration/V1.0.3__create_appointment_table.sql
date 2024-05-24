USE petcaredb;

DROP TABLE IF EXISTS appointments;

CREATE TABLE appointments (
    appointment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    vet_id BIGINT NOT NULL,
    appointment_datetime DATETIME NOT NULL,
    reason VARCHAR(500) NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(user_id),
    FOREIGN KEY (vet_id) REFERENCES users(user_id)
);