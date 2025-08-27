-- Custom enum types for Guarde.me
DO $$
BEGIN
  CREATE TYPE content_type AS ENUM (
    'text',
    'audio', 
    'photo',
    'screenshot',
    'image_link',
    'selection'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;

DO $$
BEGIN
  CREATE TYPE content_source AS ENUM (
    'selected_text',
    'camera',
    'gallery', 
    'clipboard',
    'url',
    'screen_share'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;

DO $$
BEGIN
  CREATE TYPE privacy_mode AS ENUM (
    'standard',
    'biometric_lock',
    'e2e_premium'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;

DO $$
BEGIN
  CREATE TYPE when_type AS ENUM (
    'date',
    'datetime',
    'recurrence'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;

DO $$
BEGIN
  CREATE TYPE schedule_status AS ENUM (
    'scheduled',
    'paused',
    'canceled',
    'completed'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;

DO $$
BEGIN
  CREATE TYPE delivery_channel AS ENUM (
    'in_app',
    'push',
    'email',
    'calendar'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;

DO $$
BEGIN
  CREATE TYPE delivery_status AS ENUM (
    'pending',
    'in_flight',
    'succeeded',
    'failed'
  );
EXCEPTION WHEN duplicate_object THEN 
  NULL;
END $$;