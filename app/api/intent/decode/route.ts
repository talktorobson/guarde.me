import { NextRequest, NextResponse } from 'next/server'
import { GoogleGenAI, createUserContent } from '@google/genai'
import { z } from 'zod'

// Schema for intent parsing response
const IntentSchema = z.object({
  intent: z.enum(['SAVE_MEMORY', 'SCHEDULE_REPLAY', 'DELIVERY_CHANNEL']),
  slots: z.object({
    content_type: z.enum(['text', 'voice', 'photo', 'screenshot', 'image_link', 'selection']).optional().or(z.null()).optional(),
    content_source: z.enum(['selected_text', 'camera', 'gallery', 'clipboard', 'url', 'screen_share']).optional().or(z.null()).optional(),
    topic_tags: z.array(z.string()).optional().or(z.null()).optional(),
    when_type: z.enum(['date', 'datetime', 'recurrence']).optional().or(z.null()).optional(),
    when_value: z.string().optional().or(z.null()).optional(), // ISO-8601 or RRULE
    channel: z.enum(['in_app', 'push', 'email', 'calendar']).optional().or(z.null()).optional()
  })
})

// Request schema
const RequestSchema = z.object({
  transcript_redacted: z.string().min(1),
  partial: z.boolean().optional()
})

export const runtime = 'edge'

export async function POST(req: NextRequest) {
  try {
    // Validate request body
    const body = await req.json()
    const { transcript_redacted } = RequestSchema.parse(body)

    // Check if Gemini API key is configured
    if (!process.env.GEMINI_API_KEY) {
      return NextResponse.json(
        { error: 'Gemini API key not configured' },
        { status: 500 }
      )
    }

    // Initialize Gemini with new API
    const ai = new GoogleGenAI({
      apiKey: process.env.GEMINI_API_KEY
    })

    // Create prompt for few-shot learning using new API
    const promptText = `You are an intent parser for a voice-first memory app called "Guarde.me". 
          
Users say "Guarde me" followed by:
1. What they want to save (text, voice note, photo, etc.)
2. When they want to be reminded (specific time, recurring)  
3. How they want to receive it (push, email, calendar)

Examples:
- "Guarde me esta ideia para amanhã às 9h por email"
- "Guarde me esta foto toda sexta às 18h"  
- "Guarde me este texto para o aniversário da mamãe"

Parse this transcript and return ONLY valid JSON matching this exact schema:
{
  "intent": "SAVE_MEMORY" | "SCHEDULE_REPLAY" | "DELIVERY_CHANNEL",
  "slots": {
    "content_type": "text" | "voice" | "photo" | "screenshot" | "image_link" | "selection" (optional),
    "content_source": "selected_text" | "camera" | "gallery" | "clipboard" | "url" | "screen_share" (optional),  
    "topic_tags": string[] (optional),
    "when_type": "date" | "datetime" | "recurrence" (optional),
    "when_value": string (optional, ISO-8601 or RRULE),
    "channel": "in_app" | "push" | "email" | "calendar" (optional)
  }
}

Transcript: "${transcript_redacted}"

Return ONLY the JSON, no other text:`

    // Generate content with new Gemini API
    const result = await ai.models.generateContent({
      model: 'gemini-2.5-flash',
      contents: createUserContent([promptText])
    })
    const responseText = result.text

    // Clean up response (remove markdown formatting if present)
    const jsonText = responseText
      .replace(/```json\n?/g, '')
      .replace(/```\n?/g, '')
      .trim()

    // Parse and validate response
    let parsedResponse
    try {
      parsedResponse = JSON.parse(jsonText)
    } catch {
      console.error('Failed to parse Gemini response:', jsonText)
      return NextResponse.json(
        { error: 'Invalid response from AI model' },
        { status: 422 }
      )
    }

    // Validate against schema
    const validatedResponse = IntentSchema.safeParse(parsedResponse)
    
    if (!validatedResponse.success) {
      console.error('Schema validation failed:', validatedResponse.error)
      return NextResponse.json(
        { error: 'Invalid intent structure', details: validatedResponse.error.issues },
        { status: 422 }
      )
    }

    return NextResponse.json(validatedResponse.data)

  } catch (error) {
    console.error('Intent decode error:', error)
    
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