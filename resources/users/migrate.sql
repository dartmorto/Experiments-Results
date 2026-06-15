ALTER TABLE users
ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users' AND column_name = 'password'
    ) THEN
        UPDATE users
        SET password_hash = password
        WHERE password_hash IS NULL;
    END IF;
END $$;
