-- Core tables for Guarde.me

-- Profiles (linked to auth.users)
CREATE TABLE IF NOT EXISTS profiles (
  user_id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  display_name TEXT,
  locale TEXT DEFAULT 'pt-BR',
  timezone TEXT DEFAULT 'America/Sao_Paulo',
  plan TEXT DEFAULT 'free',
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Memories table
CREATE TABLE IF NOT EXISTS memories (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  content_type content_type NOT NULL,
  content_text TEXT,
  media_path TEXT, -- path in Supabase Storage
  source content_source,
  tags TEXT[] DEFAULT '{}',
  privacy_mode privacy_mode DEFAULT 'standard',
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes for memories
CREATE INDEX IF NOT EXISTS idx_memories_user_created ON memories(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_memories_content_type ON memories(content_type);
CREATE INDEX IF NOT EXISTS idx_memories_tags ON memories USING GIN(tags);

-- Schedules (one memory -> many schedules)
CREATE TABLE IF NOT EXISTS schedules (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  memory_id UUID NOT NULL REFERENCES memories(id) ON DELETE CASCADE,
  when_type when_type NOT NULL,
  dtstart TIMESTAMPTZ, -- for date/datetime
  rrule TEXT,          -- for recurrence
  timezone TEXT NOT NULL DEFAULT 'America/Sao_Paulo',
  status schedule_status NOT NULL DEFAULT 'scheduled',
  next_run_at TIMESTAMPTZ, -- precomputed next occurrence
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes for schedules
CREATE INDEX IF NOT EXISTS idx_schedules_due ON schedules(status, next_run_at);
CREATE INDEX IF NOT EXISTS idx_schedules_user ON schedules(user_id);
CREATE INDEX IF NOT EXISTS idx_schedules_memory ON schedules(memory_id);

-- Deliveries (instantiated executions)
CREATE TABLE IF NOT EXISTS deliveries (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  schedule_id UUID NOT NULL REFERENCES schedules(id) ON DELETE CASCADE,
  channel delivery_channel NOT NULL,
  status delivery_status NOT NULL DEFAULT 'pending',
  attempt INT NOT NULL DEFAULT 0,
  last_error TEXT,
  run_at TIMESTAMPTZ DEFAULT NOW(),
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes for deliveries
CREATE INDEX IF NOT EXISTS idx_deliveries_pending ON deliveries(status, run_at);
CREATE INDEX IF NOT EXISTS idx_deliveries_user ON deliveries(user_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_schedule ON deliveries(schedule_id);

-- Push tokens for FCM
CREATE TABLE IF NOT EXISTS push_tokens (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  platform TEXT NOT NULL DEFAULT 'android',
  token TEXT NOT NULL,
  enabled BOOLEAN DEFAULT TRUE,
  last_seen_at TIMESTAMPTZ
);

-- Unique constraint on push token
CREATE UNIQUE INDEX IF NOT EXISTS uq_push_token ON push_tokens(token);
CREATE INDEX IF NOT EXISTS idx_push_tokens_user ON push_tokens(user_id);

-- Intent logs for audit (redacted transcripts only)
CREATE TABLE IF NOT EXISTS intent_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
  transcript_redacted TEXT NOT NULL,
  intent JSONB NOT NULL,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Index for intent logs
CREATE INDEX IF NOT EXISTS idx_intent_logs_user ON intent_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_intent_logs_created ON intent_logs(created_at DESC);