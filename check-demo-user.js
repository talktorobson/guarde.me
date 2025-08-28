const { createClient } = require('@supabase/supabase-js');

const supabaseUrl = 'https://cnegjzoiryjoyvzffvyh.supabase.co';
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNuZWdqem9pcnlqb3l2emZmdnloIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjI0OTc2MSwiZXhwIjoyMDcxODI1NzYxfQ.orJmqtnEpZKJZ8qNzdthQIL8RB48ncUCnmBwNdzob3A';

const supabase = createClient(supabaseUrl, supabaseKey);

async function checkDemoUser() {
  try {
    // Get the user by email
    const { data: { users }, error } = await supabase.auth.admin.listUsers();
    
    if (error) {
      console.error('Error listing users:', error);
      return;
    }
    
    const demoUser = users.find(user => user.email === 'demo@guarde.me');
    
    if (demoUser) {
      console.log('Demo user found:');
      console.log('ID:', demoUser.id);
      console.log('Email:', demoUser.email);
      console.log('Created at:', demoUser.created_at);
      
      // Now create profile and push token with the correct ID
      const userId = demoUser.id;
      
      const { data: profile, error: profileError } = await supabase
        .from('profiles')
        .upsert({
          user_id: userId,
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
        console.log('✅ Profile created successfully:', profile);
      }

      // Add a test push token
      const { data: pushToken, error: tokenError } = await supabase
        .from('push_tokens')
        .upsert({
          user_id: userId,
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
        console.log('✅ Push token created successfully:', pushToken);
      }
      
    } else {
      console.log('Demo user not found');
    }
    
  } catch (error) {
    console.error('Unexpected error:', error);
  }
}

checkDemoUser();