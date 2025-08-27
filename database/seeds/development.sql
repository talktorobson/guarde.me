-- Development seed data for Guarde.me

-- Insert a test user (this would normally be handled by Supabase Auth)
-- Note: In production, users are created through the auth system
-- This is just for testing the database structure

-- Insert test profiles (these would link to real auth.users)
-- We can't actually insert into auth.users directly, so we'll insert with placeholder UUIDs

INSERT INTO profiles (user_id, display_name, locale, timezone, plan) 
VALUES 
  ('11111111-1111-1111-1111-111111111111', 'João Silva', 'pt-BR', 'America/Sao_Paulo', 'free'),
  ('22222222-2222-2222-2222-222222222222', 'Maria Santos', 'pt-BR', 'America/Sao_Paulo', 'premium')
ON CONFLICT (user_id) DO NOTHING;

-- Insert test memories
INSERT INTO memories (user_id, content_type, content_text, source, tags, privacy_mode) 
VALUES 
  ('11111111-1111-1111-1111-111111111111', 'text', 'Lembrar de ligar para a mãe no aniversário dela', 'selected_text', ARRAY['família', 'aniversário'], 'standard'),
  ('11111111-1111-1111-1111-111111111111', 'text', 'Reunião importante com o cliente amanhã às 14h', 'selected_text', ARRAY['trabalho', 'reunião'], 'standard'),
  ('22222222-2222-2222-2222-222222222222', 'photo', 'Foto da receita de bolo da vovó', 'camera', ARRAY['receita', 'família'], 'biometric_lock')
ON CONFLICT DO NOTHING;

-- Insert test schedules
INSERT INTO schedules (user_id, memory_id, when_type, dtstart, timezone, status, next_run_at)
SELECT 
  m.user_id,
  m.id,
  'datetime',
  (NOW() + INTERVAL '1 hour')::timestamptz,
  'America/Sao_Paulo',
  'scheduled',
  (NOW() + INTERVAL '1 hour')::timestamptz
FROM memories m
WHERE m.content_text = 'Reunião importante com o cliente amanhã às 14h'
ON CONFLICT DO NOTHING;

-- Insert recurring schedule
INSERT INTO schedules (user_id, memory_id, when_type, dtstart, rrule, timezone, status, next_run_at)
SELECT 
  m.user_id,
  m.id,
  'recurrence',
  (DATE_TRUNC('year', NOW()) + INTERVAL '6 months')::timestamptz, -- Next birthday
  'FREQ=YEARLY;BYMONTH=6;BYMONTHDAY=15', -- June 15th every year (example birthday)
  'America/Sao_Paulo',
  'scheduled',
  (DATE_TRUNC('year', NOW()) + INTERVAL '6 months')::timestamptz
FROM memories m
WHERE m.content_text = 'Lembrar de ligar para a mãe no aniversário dela'
ON CONFLICT DO NOTHING;

-- Insert test push tokens (for FCM testing)
INSERT INTO push_tokens (user_id, platform, token, enabled, last_seen_at)
VALUES 
  ('11111111-1111-1111-1111-111111111111', 'android', 'test_fcm_token_user1', true, NOW()),
  ('22222222-2222-2222-2222-222222222222', 'android', 'test_fcm_token_user2', true, NOW())
ON CONFLICT (token) DO NOTHING;

-- Insert test intent logs (redacted examples)
INSERT INTO intent_logs (user_id, transcript_redacted, intent)
VALUES 
  ('11111111-1111-1111-1111-111111111111', 'Guarde me [REDACTED] para amanhã às 14h por push', 
   '{"intent": "SAVE_MEMORY", "slots": {"content_type": "text", "when_type": "datetime", "channel": "push"}}'::jsonb),
  ('22222222-2222-2222-2222-222222222222', 'Guarde me esta [REDACTED] toda sexta às 18h por email',
   '{"intent": "SAVE_MEMORY", "slots": {"content_type": "photo", "when_type": "recurrence", "channel": "email"}}'::jsonb)
ON CONFLICT DO NOTHING;