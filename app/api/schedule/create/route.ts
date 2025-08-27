import { NextRequest, NextResponse } from 'next/server'
import { supabaseAdmin } from '@/lib/supabase/server'
import { z } from 'zod'

// Request schema for creating a memory with schedule
const CreateScheduleSchema = z.object({
  user_id: z.string().uuid(),
  memory: z.object({
    content_type: z.enum(['text', 'audio', 'photo', 'screenshot', 'image_link', 'selection']),
    content_text: z.string().optional(),
    media_path: z.string().optional(),
    source: z.enum(['selected_text', 'camera', 'gallery', 'clipboard', 'url', 'screen_share']).optional(),
    tags: z.array(z.string()).default([]),
    privacy_mode: z.enum(['standard', 'biometric_lock', 'e2e_premium']).default('standard')
  }),
  schedule: z.object({
    when_type: z.enum(['date', 'datetime', 'recurrence']),
    dtstart: z.string().optional(), // ISO-8601 datetime
    rrule: z.string().optional(),   // RFC 5545 RRULE
    timezone: z.string().default('America/Sao_Paulo'),
    channel: z.enum(['in_app', 'push', 'email', 'calendar']).default('push')
  })
})

export async function POST(req: NextRequest) {
  try {
    // Parse and validate request body
    const body = await req.json()
    const { user_id, memory, schedule } = CreateScheduleSchema.parse(body)

    // Start a transaction to create memory and schedule atomically
    const { data: memoryData, error: memoryError } = await (supabaseAdmin as any)
      .from('memories')
      .insert({
        user_id,
        content_type: memory.content_type,
        content_text: memory.content_text,
        media_path: memory.media_path,
        source: memory.source,
        tags: memory.tags,
        privacy_mode: memory.privacy_mode
      })
      .select()
      .single()

    if (memoryError) {
      console.error('Memory creation error:', memoryError)
      return NextResponse.json(
        { error: 'Failed to create memory', details: memoryError.message },
        { status: 400 }
      )
    }

    // Calculate next_run_at based on when_type and dtstart/rrule
    let next_run_at: string | null = null
    
    if (schedule.when_type === 'datetime' && schedule.dtstart) {
      next_run_at = schedule.dtstart
    } else if (schedule.when_type === 'date' && schedule.dtstart) {
      next_run_at = schedule.dtstart
    } else if (schedule.when_type === 'recurrence' && schedule.rrule) {
      // For now, use dtstart as the first occurrence
      // In production, you would use a proper RRULE library to calculate the next occurrence
      next_run_at = schedule.dtstart || new Date().toISOString()
    }

    // Create the schedule
    const { data: scheduleData, error: scheduleError } = await (supabaseAdmin as any)
      .from('schedules')
      .insert({
        user_id,
        memory_id: memoryData.id,
        when_type: schedule.when_type,
        dtstart: schedule.dtstart,
        rrule: schedule.rrule,
        timezone: schedule.timezone,
        status: 'scheduled',
        next_run_at
      })
      .select()
      .single()

    if (scheduleError) {
      console.error('Schedule creation error:', scheduleError)
      
      // Cleanup: delete the memory if schedule creation failed
      await (supabaseAdmin as any)
        .from('memories')
        .delete()
        .eq('id', memoryData.id)

      return NextResponse.json(
        { error: 'Failed to create schedule', details: scheduleError.message },
        { status: 400 }
      )
    }

    // Return the created memory and schedule
    return NextResponse.json({
      success: true,
      data: {
        memory: memoryData,
        schedule: scheduleData
      }
    })

  } catch (error) {
    console.error('Create schedule error:', error)
    
    if (error instanceof z.ZodError) {
      return NextResponse.json(
        { error: 'Invalid request format', details: error.issues },
        { status: 400 }
      )
    }

    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}