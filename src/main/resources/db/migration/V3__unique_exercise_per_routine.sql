-- Earlier routine creation allowed repeated exercises. Keep the first occurrence by position and drop the rest.
DELETE FROM routine_exercise older
USING routine_exercise newer
WHERE older.routine_id = newer.routine_id
  AND older.exercise_id = newer.exercise_id
  AND older.position > newer.position;

UPDATE routine_exercise re
SET position = sub.rn - 1
FROM (
    SELECT routine_id, exercise_id, ROW_NUMBER() OVER (PARTITION BY routine_id ORDER BY position) AS rn
    FROM routine_exercise
) sub
WHERE re.routine_id = sub.routine_id AND re.exercise_id = sub.exercise_id;


ALTER TABLE routine_exercise
    ADD CONSTRAINT uq_routine_exercise UNIQUE (routine_id, exercise_id);