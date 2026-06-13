-- Admin Dashboard & Feedback Management migration
-- Compatible with MySQL 8+

-- Add audit field to users for dashboard/analytics (safe for existing rows)
ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP(6) NULL DEFAULT CURRENT_TIMESTAMP(6);

UPDATE users SET created_at = CURRENT_TIMESTAMP(6) WHERE created_at IS NULL;

-- Feedback table
CREATE TABLE IF NOT EXISTS feedbacks (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    title       VARCHAR(200) NOT NULL,
    content     TEXT         NOT NULL,
    type        VARCHAR(20)  NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'OPEN',
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NULL,
    CONSTRAINT chk_feedback_type CHECK (type IN ('BUG', 'FEATURE', 'IMPROVEMENT', 'OTHER')),
    CONSTRAINT chk_feedback_status CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED')),
    INDEX idx_feedbacks_user_id (user_id),
    INDEX idx_feedbacks_status (status),
    INDEX idx_feedbacks_created_at (created_at)
);

-- Feedback reply table
CREATE TABLE IF NOT EXISTS feedback_replies (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    feedback_id BIGINT NOT NULL,
    admin_id    BIGINT NOT NULL,
    message     TEXT   NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    INDEX idx_feedback_replies_feedback_id (feedback_id),
    CONSTRAINT fk_feedback_replies_feedback
        FOREIGN KEY (feedback_id) REFERENCES feedbacks(id) ON DELETE CASCADE
);
