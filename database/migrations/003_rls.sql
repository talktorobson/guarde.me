-- Row Level Security (RLS) policies for all tables

-- Enable RLS on all user tables
ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE memories ENABLE ROW LEVEL SECURITY;
ALTER TABLE schedules ENABLE ROW LEVEL SECURITY;
ALTER TABLE deliveries ENABLE ROW LEVEL SECURITY;
ALTER TABLE push_tokens ENABLE ROW LEVEL SECURITY;
ALTER TABLE intent_logs ENABLE ROW LEVEL SECURITY;

-- RLS Policies - Users can only access their own data
CREATE POLICY p_profiles_owner ON profiles 
  USING (user_id = auth.uid());

CREATE POLICY p_memories_owner ON memories 
  USING (user_id = auth.uid());

CREATE POLICY p_schedules_owner ON schedules 
  USING (user_id = auth.uid());

CREATE POLICY p_deliveries_owner ON deliveries 
  USING (user_id = auth.uid());

CREATE POLICY p_push_tokens_owner ON push_tokens 
  USING (user_id = auth.uid());

CREATE POLICY p_intent_logs_owner ON intent_logs 
  USING (user_id = auth.uid());