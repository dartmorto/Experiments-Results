INSERT INTO users(username, password_hash)
VALUES (?, ?)
ON CONFLICT (username) DO NOTHING
