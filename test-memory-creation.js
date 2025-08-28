const { createClient } = require('@supabase/supabase-js');

const supabaseUrl = 'https://cnegjzoiryjoyvzffvyh.supabase.co';
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNuZWdqem9pcnlqb3l2emZmdnloIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjI0OTc2MSwiZXhwIjoyMDcxODI1NzYxfQ.orJmqtnEpZKJZ8qNzdthQIL8RB48ncUCnmBwNdzob3A';

const supabase = createClient(supabaseUrl, supabaseKey);

async function testMemoryCreation() {
  const demoUserId = 'd9a31a61-0376-4c0c-a037-692be019c6a1';
  
  try {
    console.log('🧪 Testing memory creation directly in database...');
    
    // Test creating a memory directly
    const { data: memory, error: memoryError } = await supabase
      .from('memories')
      .insert({
        user_id: demoUserId,
        content_type: 'text',
        content_text: 'Test memory for demo user',
        source: 'clipboard',
        tags: ['test', 'demo'],
        privacy_mode: 'standard'
      })
      .select()
      .single();

    if (memoryError) {
      console.error('❌ Memory creation failed:', memoryError);
    } else {
      console.log('✅ Memory created successfully:', memory);
    }

    // Check existing memories for this user
    const { data: memories, error: fetchError } = await supabase
      .from('memories')
      .select('*')
      .eq('user_id', demoUserId)
      .order('created_at', { ascending: false })
      .limit(5);

    if (fetchError) {
      console.error('❌ Error fetching memories:', fetchError);
    } else {
      console.log(`📋 Found ${memories.length} memories for demo user:`);
      memories.forEach((mem, index) => {
        console.log(`  ${index + 1}. ${mem.content_text} (${mem.created_at})`);
      });
    }

    // Check user profile
    const { data: profile, error: profileError } = await supabase
      .from('profiles')
      .select('*')
      .eq('user_id', demoUserId)
      .single();

    if (profileError) {
      console.error('❌ Error fetching profile:', profileError);
    } else {
      console.log('👤 User profile:', profile);
    }

  } catch (error) {
    console.error('❌ Unexpected error:', error);
  }
}

testMemoryCreation();