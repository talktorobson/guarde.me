const { createClient } = require('@supabase/supabase-js');

// Initialize Supabase client
const supabaseUrl = 'https://cnegjzoiryjoyvzffvyh.supabase.co';
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNuZWdqem9pcnlqb3l2emZmdnloIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjI0OTc2MSwiZXhwIjoyMDcxODI1NzYxfQ.orJmqtnEpZKJZ8qNzdthQIL8RB48ncUCnmBwNdzob3A'; // service role key

const supabase = createClient(supabaseUrl, supabaseKey);

async function createDemoUser() {
  const demoUserId = '550e8400-e29b-41d4-a716-446655440000';
  
  try {
    console.log('Creating demo user...');
    
    // First, create the auth user using the admin API
    const { data: authUser, error: authError } = await supabase.auth.admin.createUser({
      user_id: demoUserId,
      email: 'demo@guarde.me',
      email_confirm: true,
      user_metadata: {
        display_name: 'Demo User'
      }
    });

    if (authError && authError.message.includes('already exists')) {
      console.log('Auth user already exists, continuing...');
    } else if (authError) {
      console.error('Error creating auth user:', authError);
    } else {
      console.log('Auth user created successfully:', authUser.user?.id);
    }

    // Then, create/update the profile
    const { data: profile, error: profileError } = await supabase
      .from('profiles')
      .upsert({
        user_id: demoUserId,
        display_name: 'Demo User',
        locale: 'pt-BR',
        timezone: 'America/Sao_Paulo',
        plan: 'free'
      })
      .select()
      .single();

    if (profileError) {
      console.error('Error creating profile:', profileError);
    } else {
      console.log('Profile created/updated successfully:', profile);
    }

    // Add a test push token
    const { data: pushToken, error: tokenError } = await supabase
      .from('push_tokens')
      .upsert({
        user_id: demoUserId,
        platform: 'android',
        token: 'demo_fcm_token_android',
        enabled: true,
        last_seen_at: new Date().toISOString()
      })
      .select()
      .single();

    if (tokenError) {
      console.error('Error creating push token:', tokenError);
    } else {
      console.log('Push token created/updated successfully:', pushToken);
    }

    console.log('✅ Demo user setup complete!');
    console.log(`User ID: ${demoUserId}`);
    console.log('Email: demo@guarde.me');
    
  } catch (error) {
    console.error('Unexpected error:', error);
  }
}

createDemoUser();