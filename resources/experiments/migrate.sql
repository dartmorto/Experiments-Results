ALTER TABLE experiments
ADD COLUMN IF NOT EXISTS owner_username VARCHAR(100);

ALTER TABLE experiments
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE experiments
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'experiments' AND column_name = 'owner'
    ) THEN
        UPDATE experiments
        SET owner_username = owner
        WHERE owner_username IS NULL;
    END IF;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'experiments' AND column_name = 'owner_id'
    ) AND EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users' AND column_name = 'id'
    ) THEN
        UPDATE experiments
        SET owner_username = users.username
        FROM users
        WHERE experiments.owner_id = users.id
          AND experiments.owner_username IS NULL;
    END IF;
END $$;

ALTER TABLE experiments
DROP COLUMN IF EXISTS owner_id CASCADE;

ALTER TABLE experiments
DROP COLUMN IF EXISTS owner CASCADE;

UPDATE experiments
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

UPDATE experiments
SET updated_at = created_at
WHERE updated_at IS NULL;
