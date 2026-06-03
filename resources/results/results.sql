CREATE TABLE IF NOT EXISTS results (
    id SERIAL PRIMARY KEY,
    run_id INTEGER NOT NULL,
    param VARCHAR(100) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    unit VARCHAR(50) NOT NULL,
    comment TEXT,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_results_run
        FOREIGN KEY (run_id)
        REFERENCES runs(id)
        ON DELETE CASCADE
);
