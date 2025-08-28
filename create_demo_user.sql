-- Create demo user for testing
-- Insert the demo user into auth.users (this is the specific UUID used by the app)

-- First, insert into auth.users table
INSERT INTO auth.users (
  id,
  aud,
  role,
  email,
  raw_app_meta_data,
  raw_user_meta_data,
  is_super_admin,
  created_at,
  updated_at,
  email_confirmed_at,
  confirmation_sent_at
) VALUES (
  '550e8400-e29b-41d4-a716-446655440000',
  'authenticated',
  'authenticated',
  'demo@guarde.me',
  '{"provider": "email", "providers": ["email"]}',
  '{"display_name": "Demo User"}',
  false,
  NOW(),
  NOW(),
  NOW(),
  NOW()
) ON CONFLICT (id) DO UPDATE SET
  email = EXCLUDED.email,
  updated_at = NOW();

-- Then create the profile
INSERT INTO profiles (user_id, display_name, locale, timezone, plan) 
VALUES (
  '550e8400-e29b-41d4-a716-446655440000', 
  'Demo User', 
  'pt-BR', 
  'America/Sao_Paulo', 
  'free'
) ON CONFLICT (user_id) DO UPDATE SET
  display_name = EXCLUDED.display_name,
  locale = EXCLUDED.locale,
  timezone = EXCLUDED.timezone,
  plan = EXCLUDED.plan;

-- Add a test push token for the demo user
INSERT INTO push_tokens (user_id, platform, token, enabled, last_seen_at)
VALUES (
  '550e8400-e29b-41d4-a716-446655440000', 
  'android', 
  'demo_fcm_token_android', 
  true, 
  NOW()
) ON CONFLICT (token) DO UPDATE SET
  user_id = EXCLUDED.user_id,
  enabled = EXCLUDED.enabled,
  last_seen_at = NOW();