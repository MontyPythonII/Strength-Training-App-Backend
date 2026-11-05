CREATE TABLE app_user (
    id            BIGSERIAL PRIMARY KEY,
    display_name  VARCHAR(100) NOT NULL,
    email         VARCHAR(254) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type     VARCHAR(7)   NOT NULL
);

CREATE TABLE exercise (
    id               BIGSERIAL PRIMARY KEY,
    owner_id         BIGINT       NOT NULL REFERENCES app_user(id),
    name             VARCHAR(100) NOT NULL,
    description      VARCHAR(500) NOT NULL,
    barbell_exercise BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE exercise_set (
    id           BIGSERIAL PRIMARY KEY,
    exercise_id  BIGINT       NOT NULL REFERENCES exercise(id) ON DELETE CASCADE,
    set_number   INTEGER      NOT NULL,
    weight       NUMERIC(6,2),
    reps         INTEGER,
    distance     INTEGER,
    time_seconds INTEGER,
    notes        VARCHAR(500),
    UNIQUE (exercise_id, set_number)
);

CREATE TABLE completed_exercise_set (
    id           BIGSERIAL PRIMARY KEY,
    exercise_id  BIGINT      NOT NULL REFERENCES exercise(id) ON DELETE CASCADE,
    set_number   INTEGER     NOT NULL,
    weight       NUMERIC(6,2),
    reps         INTEGER,
    distance     INTEGER,
    time_seconds INTEGER,
    completed_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE routine (
    id          BIGSERIAL PRIMARY KEY,
    owner_id    BIGINT       NOT NULL REFERENCES app_user(id),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL
);

CREATE TABLE routine_exercise (
    routine_id  BIGINT  NOT NULL REFERENCES routine(id) ON DELETE CASCADE,
    exercise_id BIGINT  NOT NULL REFERENCES exercise(id),
    position    INTEGER NOT NULL,
    PRIMARY KEY (routine_id, position)
);

CREATE TABLE schedule (
    id          BIGSERIAL PRIMARY KEY,
    owner_id    BIGINT       NOT NULL REFERENCES app_user(id),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    start_date  DATE,
    end_date    DATE
);

CREATE TABLE schedule_routine (
    schedule_id BIGINT     NOT NULL REFERENCES schedule(id) ON DELETE CASCADE,
    day_of_week VARCHAR(9) NOT NULL,
    routine_id  BIGINT     NOT NULL REFERENCES routine(id),
    PRIMARY KEY (schedule_id, day_of_week)
);

CREATE INDEX idx_exercise_owner ON exercise(owner_id);
CREATE INDEX idx_routine_owner  ON routine(owner_id);
CREATE INDEX idx_schedule_owner ON schedule(owner_id);
CREATE INDEX idx_completed_set_exercise ON completed_exercise_set(exercise_id, completed_at);
