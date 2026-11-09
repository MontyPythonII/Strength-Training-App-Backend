ALTER TABLE athlete_group
    ADD COLUMN invite_code UUID NOT NULL UNIQUE DEFAULT gen_random_uuid();
