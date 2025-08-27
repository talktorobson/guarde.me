-- Helper functions for delivery management

-- Function to claim pending deliveries with SKIP LOCKED semantics
CREATE OR REPLACE FUNCTION app_claim_pending_deliveries(max_rows INT DEFAULT 100)
RETURNS SETOF deliveries
LANGUAGE plpgsql SECURITY DEFINER AS $$
BEGIN
  RETURN QUERY
  WITH cte AS (
    SELECT id
    FROM deliveries
    WHERE status = 'pending' AND run_at <= NOW()
    ORDER BY run_at ASC
    LIMIT max_rows
    FOR UPDATE SKIP LOCKED
  )
  UPDATE deliveries d
    SET status = 'in_flight', 
        attempt = attempt + 1, 
        updated_at = NOW()
    FROM cte
    WHERE d.id = cte.id
  RETURNING d.*;
END; $$;

-- Function to enqueue due deliveries from schedules
CREATE OR REPLACE FUNCTION app_enqueue_due_deliveries()
RETURNS VOID LANGUAGE plpgsql SECURITY DEFINER AS $$
DECLARE 
  r RECORD;
BEGIN
  FOR r IN (
    SELECT s.id AS schedule_id, s.user_id, s.next_run_at
    FROM schedules s
    WHERE s.status = 'scheduled' 
      AND s.next_run_at IS NOT NULL 
      AND s.next_run_at <= NOW()
    FOR UPDATE SKIP LOCKED
  ) LOOP
    -- Insert delivery for this schedule
    INSERT INTO deliveries(user_id, schedule_id, channel, status, run_at)
    VALUES (r.user_id, r.schedule_id, 'push', 'pending', COALESCE(r.next_run_at, NOW()))
    ON CONFLICT DO NOTHING;

    -- Advance next_run_at or complete schedule
    -- Note: This is a simplified increment - production should use proper RRULE calculation
    UPDATE schedules s
      SET next_run_at = CASE
        WHEN s.rrule IS NOT NULL AND s.rrule <> '' 
        THEN s.next_run_at + INTERVAL '1 week' -- placeholder; recompute in app
        ELSE NULL
      END,
      status = CASE 
        WHEN s.rrule IS NULL OR s.rrule = '' 
        THEN 'completed' 
        ELSE s.status 
      END
    WHERE s.id = r.schedule_id;
  END LOOP;
END; $$;

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at on deliveries
CREATE TRIGGER set_timestamp
  BEFORE UPDATE ON deliveries
  FOR EACH ROW
  EXECUTE PROCEDURE trigger_set_timestamp();