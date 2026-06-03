CREATE TABLE IF NOT EXISTS runs (
    id SERIAL PRIMARY KEY,

    experiment_id INTEGER NOT NULL,
    operator_username VARCHAR(100) NOT NULL,

    name VARCHAR(255) NOT NULL,

    CONSTRAINT fk_runs_experiment
        FOREIGN KEY (experiment_id)
        REFERENCES experiments(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_runs_operator
        FOREIGN KEY (operator_username)
        REFERENCES users(username)
);
