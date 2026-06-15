ALTER TABLE results
ADD COLUMN IF NOT EXISTS unit VARCHAR(50);

ALTER TABLE results
ADD COLUMN IF NOT EXISTS comment TEXT;

ALTER TABLE results
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'results' AND column_name = 'timestamp'
    ) THEN
        UPDATE results
        SET created_at = timestamp
        WHERE created_at IS NULL;
    END IF;
END $$;

UPDATE results
SET unit = ''
WHERE unit IS NULL;

UPDATE results
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

ALTER TABLE results
DROP COLUMN IF EXISTS timestamp CASCADE;
