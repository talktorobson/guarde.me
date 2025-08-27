-- pg_cron setup for delivery scheduling

-- Enable pg_cron extension (requires superuser privileges)
-- This should be enabled in Supabase dashboard
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- Schedule the delivery enqueue function to run every minute
-- Note: This will need to be run manually in the Supabase SQL editor
-- as it requires superuser privileges
SELECT cron.schedule(
  'guarde_me_enqueue', 
  '* * * * *', 
  $$SELECT app_enqueue_due_deliveries();$$
);