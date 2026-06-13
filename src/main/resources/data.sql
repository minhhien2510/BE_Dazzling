INSERT INTO users (keycloak_id, email, password, full_name, avatar_url, role)
SELECT NULL, 'admin@dazzling.io', '$2b$12$4unuUUQPGCv5nnHJ3tgaRe72Yj.eBLrZ3nJwQDSs/WXnPG0AzX3Nm', 'System Admin', NULL, 'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@dazzling.io');

UPDATE users SET created_at = CURRENT_TIMESTAMP(6) WHERE created_at IS NULL;
