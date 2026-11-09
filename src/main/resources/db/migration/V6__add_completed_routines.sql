CREATE TABLE completed_routine (
    id           BIGSERIAL PRIMARY KEY,
    athlete_id   BIGINT       NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    routine_id   BIGINT       REFERENCES routine(id) ON DELETE SET NULL,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(500) NOT NULL,
    completed_at TIMESTAMPTZ  NOT NULL
);

CREATE TABLE completed_exercise (
    id                   BIGSERIAL PRIMARY KEY,
    completed_routine_id BIGINT       NOT NULL REFERENCES completed_routine(id) ON DELETE CASCADE,
    exercise_id          BIGINT       REFERENCES exercise(id) ON DELETE SET NULL,
    position             INTEGER      NOT NULL,
    name                 VARCHAR(100) NOT NULL,
    description          VARCHAR(500) NOT NULL,
    barbell_exercise     BOOLEAN      NOT NULL,
    measure_type         VARCHAR(8)   NOT NULL,
    UNIQUE (completed_routine_id, position)
);

-- Completed sets now belong to the logged exercise instead of the coach's template, so deleting a template no longer deletes history.
-- Nothing writes completed sets yet, so the table is empty and the NOT NULL column can be added directly. Dropping exercise_id also drops idx_completed_set_exercise.
ALTER TABLE completed_exercise_set
    DROP COLUMN exercise_id,
    ADD COLUMN completed_exercise_id BIGINT NOT NULL REFERENCES completed_exercise(id) ON DELETE CASCADE;

CREATE INDEX idx_completed_routine_athlete ON completed_routine(athlete_id, completed_at);
CREATE INDEX idx_completed_exercise_template ON completed_exercise(exercise_id);
CREATE INDEX idx_completed_set_parent ON completed_exercise_set(completed_exercise_id);
