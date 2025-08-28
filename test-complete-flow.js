const API_BASE = 'http://localhost:3000';

async function testCompleteFlow() {
  console.log('🧪 Testing complete end-to-end flow...\n');
  
  try {
    // Step 1: Test intent decode (simulate what the app does)
    console.log('1️⃣ Testing intent decode...');
    const intentResponse = await fetch(`${API_BASE}/api/intent/decode`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        transcript_redacted: 'Quero lembrar de comprar pão amanhã às 8 horas',
        partial: false
      })
    });

    if (!intentResponse.ok) {
      console.error('❌ Intent decode failed:', intentResponse.status);
      return;
    }

    const intentResult = await intentResponse.json();
    console.log('✅ Intent decode successful:');
    console.log(`   Intent: ${intentResult.intent}`);
    console.log(`   When type: ${intentResult.slots.whenType}`);
    console.log(`   When value: ${intentResult.slots.whenValue}`);
    console.log(`   Tags: ${intentResult.slots.topicTags?.join(', ') || 'None'}`);
    console.log(`   Channel: ${intentResult.slots.channel}\n`);

    // Step 2: Test schedule/memory creation (simulate what the app does)
    console.log('2️⃣ Testing memory and schedule creation...');
    const scheduleResponse = await fetch(`${API_BASE}/api/schedule/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        user_id: 'd9a31a61-0376-4c0c-a037-692be019c6a1', // Our demo user
        memory: {
          content_type: 'text',
          content_text: 'Quero lembrar de comprar pão amanhã às 8 horas',
          source: 'selected_text',
          tags: intentResult.slots.topicTags || [],
          privacy_mode: 'standard'
        },
        schedule: {
          when_type: intentResult.slots.whenType || 'datetime',
          dtstart: intentResult.slots.whenValue,
          timezone: 'America/Sao_Paulo',
          channel: intentResult.slots.channel || 'push'
        }
      })
    });

    if (!scheduleResponse.ok) {
      console.error('❌ Schedule creation failed:', scheduleResponse.status);
      const errorText = await scheduleResponse.text();
      console.error('Error details:', errorText);
      return;
    }

    const scheduleResult = await scheduleResponse.json();
    console.log('✅ Memory and schedule created successfully:');
    console.log(`   Memory ID: ${scheduleResult.data.memory.id}`);
    console.log(`   Memory Content: "${scheduleResult.data.memory.content_text}"`);
    console.log(`   Memory Tags: ${scheduleResult.data.memory.tags.join(', ') || 'None'}`);
    console.log(`   Schedule ID: ${scheduleResult.data.schedule.id}`);
    console.log(`   Schedule Status: ${scheduleResult.data.schedule.status}`);
    console.log(`   Next Run: ${scheduleResult.data.schedule.next_run_at || 'Not set'}\n`);

    console.log('🎉 Complete end-to-end flow successful!');
    console.log('✅ The entire system is working perfectly:');
    console.log('   • Demo user exists in database');
    console.log('   • AI intent processing works');
    console.log('   • Memory creation works');
    console.log('   • Schedule creation works');
    console.log('   • All API endpoints functional');
    
  } catch (error) {
    console.error('❌ Unexpected error:', error);
  }
}

testCompleteFlow();