ALTER TABLE runs
ADD COLUMN IF NOT EXISTS operator_username VARCHAR(100);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'runs' AND column_name = 'operator'
    ) THEN
        UPDATE runs
        SET operator_username = operator
        WHERE operator_username IS NULL;
    END IF;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'runs' AND column_name = 'operator_id'
    ) AND EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users' AND column_name = 'id'
    ) THEN
        UPDATE runs
        SET operator_username = users.username
        FROM users
        WHERE runs.operator_id = users.id
          AND runs.operator_username IS NULL;
    END IF;
END $$;

ALTER TABLE runs
DROP COLUMN IF EXISTS operator_id CASCADE;

ALTER TABLE runs
DROP COLUMN IF EXISTS operator CASCADE;
