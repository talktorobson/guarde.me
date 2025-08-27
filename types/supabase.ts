// TypeScript types for Supabase database schema
export interface Database {
  public: {
    Tables: {
      profiles: {
        Row: {
          user_id: string
          display_name: string | null
          locale: string
          timezone: string
          plan: string
          created_at: string
        }
        Insert: {
          user_id: string
          display_name?: string | null
          locale?: string
          timezone?: string
          plan?: string
          created_at?: string
        }
        Update: {
          user_id?: string
          display_name?: string | null
          locale?: string
          timezone?: string
          plan?: string
          created_at?: string
        }
      }
      memories: {
        Row: {
          id: string
          user_id: string
          content_type: 'text' | 'audio' | 'photo' | 'screenshot' | 'image_link' | 'selection'
          content_text: string | null
          media_path: string | null
          source: 'selected_text' | 'camera' | 'gallery' | 'clipboard' | 'url' | 'screen_share' | null
          tags: string[]
          privacy_mode: 'standard' | 'biometric_lock' | 'e2e_premium'
          created_at: string
        }
        Insert: {
          id?: string
          user_id: string
          content_type: 'text' | 'audio' | 'photo' | 'screenshot' | 'image_link' | 'selection'
          content_text?: string | null
          media_path?: string | null
          source?: 'selected_text' | 'camera' | 'gallery' | 'clipboard' | 'url' | 'screen_share' | null
          tags?: string[]
          privacy_mode?: 'standard' | 'biometric_lock' | 'e2e_premium'
          created_at?: string
        }
        Update: {
          id?: string
          user_id?: string
          content_type?: 'text' | 'audio' | 'photo' | 'screenshot' | 'image_link' | 'selection'
          content_text?: string | null
          media_path?: string | null
          source?: 'selected_text' | 'camera' | 'gallery' | 'clipboard' | 'url' | 'screen_share' | null
          tags?: string[]
          privacy_mode?: 'standard' | 'biometric_lock' | 'e2e_premium'
          created_at?: string
        }
      }
      schedules: {
        Row: {
          id: string
          user_id: string
          memory_id: string
          when_type: 'date' | 'datetime' | 'recurrence'
          dtstart: string | null
          rrule: string | null
          timezone: string
          status: 'scheduled' | 'paused' | 'canceled' | 'completed'
          next_run_at: string | null
          created_at: string
        }
        Insert: {
          id?: string
          user_id: string
          memory_id: string
          when_type: 'date' | 'datetime' | 'recurrence'
          dtstart?: string | null
          rrule?: string | null
          timezone?: string
          status?: 'scheduled' | 'paused' | 'canceled' | 'completed'
          next_run_at?: string | null
          created_at?: string
        }
        Update: {
          id?: string
          user_id?: string
          memory_id?: string
          when_type?: 'date' | 'datetime' | 'recurrence'
          dtstart?: string | null
          rrule?: string | null
          timezone?: string
          status?: 'scheduled' | 'paused' | 'canceled' | 'completed'
          next_run_at?: string | null
          created_at?: string
        }
      }
      deliveries: {
        Row: {
          id: string
          user_id: string
          schedule_id: string
          channel: 'in_app' | 'push' | 'email' | 'calendar'
          status: 'pending' | 'in_flight' | 'succeeded' | 'failed'
          attempt: number
          last_error: string | null
          run_at: string
          created_at: string
          updated_at: string
        }
        Insert: {
          id?: string
          user_id: string
          schedule_id: string
          channel: 'in_app' | 'push' | 'email' | 'calendar'
          status?: 'pending' | 'in_flight' | 'succeeded' | 'failed'
          attempt?: number
          last_error?: string | null
          run_at?: string
          created_at?: string
          updated_at?: string
        }
        Update: {
          id?: string
          user_id?: string
          schedule_id?: string
          channel?: 'in_app' | 'push' | 'email' | 'calendar'
          status?: 'pending' | 'in_flight' | 'succeeded' | 'failed'
          attempt?: number
          last_error?: string | null
          run_at?: string
          created_at?: string
          updated_at?: string
        }
      }
      push_tokens: {
        Row: {
          id: string
          user_id: string
          platform: string
          token: string
          enabled: boolean
          last_seen_at: string | null
        }
        Insert: {
          id?: string
          user_id: string
          platform?: string
          token: string
          enabled?: boolean
          last_seen_at?: string | null
        }
        Update: {
          id?: string
          user_id?: string
          platform?: string
          token?: string
          enabled?: boolean
          last_seen_at?: string | null
        }
      }
      intent_logs: {
        Row: {
          id: string
          user_id: string | null
          transcript_redacted: string
          intent: any // JSONB
          created_at: string
        }
        Insert: {
          id?: string
          user_id?: string | null
          transcript_redacted: string
          intent: any // JSONB
          created_at?: string
        }
        Update: {
          id?: string
          user_id?: string | null
          transcript_redacted?: string
          intent?: any // JSONB
          created_at?: string
        }
      }
    }
    Functions: {
      app_claim_pending_deliveries: {
        Args: { max_rows?: number }
        Returns: Database['public']['Tables']['deliveries']['Row'][]
      }
      app_enqueue_due_deliveries: {
        Args: {}
        Returns: void
      }
    }
  }
}