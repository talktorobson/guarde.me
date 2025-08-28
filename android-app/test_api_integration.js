#!/usr/bin/env node

/**
 * Test script to verify Guarde.me backend integration
 * Tests the same endpoints that the Android app uses
 */

const http = require('http');

// Test configuration
const API_BASE = 'http://localhost:3000';
const DEMO_USER_ID = 'd9a31a61-0376-4c0c-a037-692be019c6a1';

// Helper function to make HTTP requests
function makeRequest(path, method = 'GET', data = null) {
    return new Promise((resolve, reject) => {
        const url = new URL(path, API_BASE);
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json',
                'User-Agent': 'GuardeMeApp/1.0 (Android Test)'
            }
        };

        const req = http.request(url, options, (res) => {
            let body = '';
            res.on('data', (chunk) => body += chunk);
            res.on('end', () => {
                try {
                    const result = {
                        status: res.statusCode,
                        headers: res.headers,
                        data: body ? JSON.parse(body) : null
                    };
                    resolve(result);
                } catch (e) {
                    resolve({
                        status: res.statusCode,
                        headers: res.headers,
                        data: body
                    });
                }
            });
        });

        req.on('error', reject);

        if (data) {
            req.write(JSON.stringify(data));
        }

        req.end();
    });
}

async function testMemoriesAPI() {
    console.log('🧠 Testing Memories API...');
    
    try {
        const response = await makeRequest(`/api/memories?user_id=${DEMO_USER_ID}&limit=10`);
        
        console.log(`Status: ${response.status}`);
        
        if (response.status === 200) {
            console.log('✅ Memories API working!');
            console.log(`Found ${response.data.data?.length || 0} memories`);
            
            if (response.data.data?.length > 0) {
                console.log('📝 Sample memory:', {
                    id: response.data.data[0].id,
                    content: response.data.data[0].content_text?.substring(0, 50) + '...',
                    created: response.data.data[0].created_at
                });
            }
        } else {
            console.log('❌ Memories API failed');
            console.log('Response:', response.data);
        }
    } catch (error) {
        console.log('❌ Memories API error:', error.message);
    }
}

async function testIntentAPI() {
    console.log('\n🎯 Testing Intent Decode API...');
    
    const testIntent = {
        transcript_redacted: "guarde me lembrar de tomar remédio amanhã às 8 da manhã"
    };
    
    try {
        const response = await makeRequest('/api/intent/decode', 'POST', testIntent);
        
        console.log(`Status: ${response.status}`);
        
        if (response.status === 200) {
            console.log('✅ Intent API working!');
            console.log('🧠 AI Understanding:', {
                memory: response.data.memory?.content,
                schedule: response.data.schedule?.when_type,
                confidence: response.data.confidence
            });
        } else {
            console.log('❌ Intent API failed');
            console.log('Response:', response.data);
        }
    } catch (error) {
        console.log('❌ Intent API error:', error.message);
    }
}

async function runTests() {
    console.log('🚀 Starting Guarde.me API Integration Tests\n');
    console.log(`📍 API Base: ${API_BASE}`);
    console.log(`👤 Demo User: ${DEMO_USER_ID}\n`);
    
    await testMemoriesAPI();
    await testIntentAPI();
    
    console.log('\n✨ Tests completed!');
}

// Run tests
runTests().catch(console.error);