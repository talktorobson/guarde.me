import { NextResponse } from 'next/server'
import { supabaseAdmin } from '@/lib/supabase/server'
import { Database } from '@/types/supabase'

type PushToken = Database['public']['Tables']['push_tokens']['Row']

// FCM Push notification function using legacy API for simplicity
async function sendPush(user_id: string, title: string, body: string) {
  console.log('FCM Push notification requested for user:', user_id)
  console.log('VAPID keys configured:', {
    public: !!process.env.FCM_VAPID_PUBLIC_KEY,
    private: !!process.env.FCM_VAPID_PRIVATE_KEY
  })
  
  // For now, log the push notification attempt
  // In Sprint 1, this will be implemented with proper FCM integration
  console.log('Push notification would be sent:', { title, body })
  
  // Get user's push tokens
  const { data: tokens, error: tokenError } = await supabaseAdmin
    .from('push_tokens')
    .select('*')
    .eq('user_id', user_id)
    .eq('enabled', true)

  if (tokenError || !tokens || tokens.length === 0) {
    console.log('No push tokens found for user:', user_id)
    return // No tokens to send to
  }

  console.log(`Found ${tokens.length} push tokens for user:`, user_id)
  // TODO: Implement actual FCM push in Sprint 1 Android development
}

// Email sending function
async function sendEmail(to: string, subject: string, html: string, ics?: string) {
  if (!process.env.RESEND_API_KEY) {
    throw new Error('RESEND_API_KEY not configured')
  }

  const attachments = ics ? [{
    filename: 'reminder.ics',
    content: Buffer.from(ics).toString('base64')
  }] : []

  const response = await fetch('https://api.resend.com/emails', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${process.env.RESEND_API_KEY}`
    },
    body: JSON.stringify({
      from: 'Guarde.me <noreply@guarde.me>',
      to,
      subject,
      html,
      attachments
    })
  })

  if (!response.ok) {
    throw new Error(`Email send failed: ${response.statusText}`)
  }
}

// ICS calendar file builder
function buildICS({ 
  uid, 
  dtstart, 
  durationMinutes = 10, 
  summary, 
  description 
}: { 
  uid: string
  dtstart: string
  durationMinutes?: number
  summary: string
  description?: string
}) {
  const dt = dtstart.replace(/[-:]/g, '').replace('.000Z', 'Z')
  const dtend = new Date(new Date(dtstart).getTime() + durationMinutes * 60000)
    .toISOString()
    .replace(/[-:]/g, '')
    .replace('.000Z', 'Z')

  return [
    'BEGIN:VCALENDAR',
    'VERSION:2.0',
    'PRODID:-//Guarde.me//EN',
    'BEGIN:VEVENT',
    `UID:${uid}`,
    `DTSTAMP:${dt}`,
    `DTSTART:${dt}`,
    `DTEND:${dtend}`,
    `SUMMARY:${summary}`,
    description ? `DESCRIPTION:${description}` : undefined,
    'END:VEVENT',
    'END:VCALENDAR'
  ].filter(Boolean).join('\r\n')
}

export async function POST() {
  try {
    console.log('Starting delivery run...')

    // Claim up to 50 pending deliveries using the SQL function
    const { data: claimedDeliveries, error: claimError } = await (supabaseAdmin as any)
      .rpc('app_claim_pending_deliveries', { max_rows: 50 })

    if (claimError) {
      console.error('Failed to claim deliveries:', claimError)
      return NextResponse.json(
        { error: 'Failed to claim deliveries', details: claimError.message },
        { status: 500 }
      )
    }

    console.log(`Claimed ${claimedDeliveries?.length || 0} deliveries`)

    const results = {
      processed: 0,
      succeeded: 0,
      failed: 0,
      errors: [] as string[]
    }

    // Process each claimed delivery
    for (const delivery of claimedDeliveries || []) {
      results.processed++
      
      try {
        // Load the schedule with memory details
        const { data: schedule } = await supabaseAdmin
          .from('schedules')
          .select('*, memories(*)')
          .eq('id', delivery.schedule_id)
          .single()

        if (!schedule || !(schedule as { memories?: unknown }).memories) {
          throw new Error('Schedule or memory not found')
        }

        const memory = (schedule as { memories: { content_text?: string } }).memories
        const title = 'Guarde.me — Lembrete'
        const body = memory.content_text?.slice(0, 120) || 'Você tem uma memória para revisitar.'

        // Send based on delivery channel
        if (delivery.channel === 'push' || delivery.channel === 'in_app') {
          await sendPush(delivery.user_id, title, body)
        } 
        
        if (delivery.channel === 'email') {
          // For now, use TEST_FALLBACK_EMAIL - in production, get from user profile
          const email = process.env.TEST_FALLBACK_EMAIL
          if (!email) {
            throw new Error('No email configured for delivery')
          }

          const ics = buildICS({
            uid: delivery.id,
            dtstart: delivery.run_at,
            summary: title,
            description: body
          })

          await sendEmail(email, title, `<p>${body}</p>`, ics)
        }

        // Mark delivery as succeeded
        await (supabaseAdmin as any)
          .from('deliveries')
          .update({ 
            status: 'succeeded', 
            updated_at: new Date().toISOString() 
          })
          .eq('id', delivery.id)

        results.succeeded++
        console.log(`Delivered ${delivery.channel} to user ${delivery.user_id}`)

      } catch (error) {
        results.failed++
        const errorMessage = error instanceof Error ? error.message : 'Unknown error'
        results.errors.push(`Delivery ${delivery.id}: ${errorMessage}`)

        // Mark delivery as failed
        await (supabaseAdmin as any)
          .from('deliveries')
          .update({ 
            status: 'failed', 
            last_error: errorMessage,
            updated_at: new Date().toISOString() 
          })
          .eq('id', delivery.id)

        console.error(`Failed to deliver ${delivery.id}:`, error)
      }
    }

    console.log('Delivery run completed:', results)

    return NextResponse.json({
      success: true,
      results
    })

  } catch (error) {
    console.error('Delivery run error:', error)
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}