CREATE TABLE athlete_group (
    id                 BIGSERIAL PRIMARY KEY,
    owner_id           BIGINT       NOT NULL REFERENCES app_user(id),
    name               VARCHAR(100) NOT NULL,
    description        VARCHAR(500) NOT NULL,
    active_schedule_id BIGINT       REFERENCES schedule(id) ON DELETE SET NULL
);

ALTER TABLE app_user
    ADD COLUMN coach_id BIGINT REFERENCES app_user(id)      ON DELETE SET NULL,
    ADD COLUMN athlete_group_id BIGINT REFERENCES athlete_group(id) ON DELETE SET NULL;

CREATE INDEX idx_group_owner ON athlete_group(owner_id);
CREATE INDEX idx_user_coach  ON app_user(coach_id);
CREATE INDEX idx_user_group  ON app_user(athlete_group_id);

