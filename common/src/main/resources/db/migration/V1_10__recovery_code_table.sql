CREATE TABLE IF NOT EXISTS recovery_code (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE recovery_code
ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE;

ALTER TABLE recovery_code
ALTER COLUMN expired_at TYPE TIMESTAMP WITH TIME ZONE;