ALTER TABLE completed_routine ADD COLUMN completed_on DATE;
UPDATE completed_routine SET completed_on = completed_at::date;
ALTER TABLE completed_routine ALTER COLUMN completed_on SET NOT NULL;
ALTER TABLE completed_routine ADD CONSTRAINT uq_completed_routine_athlete_day UNIQUE (athlete_id, completed_on);
