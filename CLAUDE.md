# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
**Guarde.me** is a voice-first Android application for capturing memories and scheduling intelligent delivery. Users can say "Guarde me" (wake word) to capture multimodal content (text/audio/photo) and specify when and how they want to receive reminders.

### Key Features
- **Voice-first capture**: Wake word "Guarde me" + natural language intent parsing
- **Multimodal content**: Text, audio, photos, screenshots
- **Smart scheduling**: ISO-8601 dates + RRULE recurrences (RFC 5545)
- **Multiple delivery channels**: FCM push notifications, email with .ics calendar attachments
- **Privacy-focused**: On-device processing first, EU data residency

## Architecture Overview

### Tech Stack
- **Frontend**: Android (Kotlin + Jetpack Compose)
- **Backend**: Vercel Serverless Functions (Node.js/TypeScript)
- **Database**: Supabase (PostgreSQL + Storage + Auth + RLS)
- **Scheduling**: pg_cron + Supabase functions
- **AI/NLU**: On-device parser + Google Gemini (AI Studio) fallback
- **Push**: Firebase Cloud Messaging (FCM)
- **Email**: Resend API with .ics attachments

### System Components
- **Wake Word Service**: On-device hotword detection
- **Intent Parser**: Local NLU + Gemini fallback with JSON schema
- **Scheduler**: RRULE-based recurring reminders
- **Delivery System**: Push notifications and email with calendar integration
- **Timeline**: Local memory browsing with filters

## Development Commands

### Frontend (Android)
```bash
# Build Android app
./gradlew build

# Run tests
./gradlew test

# Install on device/emulator
./gradlew installDebug
```

### Backend (Vercel)
```bash
# Development server
npm run dev

# Build for production
npm run build

# Deploy to Vercel
vercel --prod

# Run linting (REQUIRED after major edits)
npm run lint
```

### Database (Supabase)
```bash
# Run migrations
npx supabase migration up

# Generate types
npx supabase gen types typescript --local > types/supabase.ts

# Seed database
psql -h localhost -p 54322 -d postgres -U postgres < scripts/seed.sql
```

## Database Schema

### Core Tables
- **profiles**: User preferences (locale, timezone, plan)
- **memories**: Multimodal content storage with privacy modes
- **schedules**: RRULE-based scheduling with next_run_at optimization
- **deliveries**: Instantiated delivery attempts with retry logic
- **push_tokens**: FCM token management
- **intent_logs**: Redacted NLU audit trail

### Key Features
- **Row Level Security (RLS)**: All tables filtered by `auth.uid()`
- **RRULE Support**: RFC 5545 recurrence rules with timezone handling
- **Media Storage**: Supabase Storage with signed URLs
- **Atomic Operations**: SQL functions for concurrent delivery claiming

## API Endpoints

### Core APIs
- **POST /api/intent/decode**: NLU intent parsing with Gemini fallback
- **POST /api/schedule/create**: Memory + schedule creation
- **POST /api/deliver/run**: Process pending deliveries (cron trigger)

### Authentication
All APIs use Supabase Auth with JWT tokens. RLS policies ensure user data isolation.

## Key Requirements & Constraints

### Performance Targets
- Wake word → UI: < 500ms (p95)
- Backend operations: < 300ms simple, < 1.5s scheduling
- Delivery SLA: 95% within ±1 minute
- Battery impact: ≤ 3% daily

### Privacy & Security
- **No raw audio upload**: On-device ASR, redacted transcripts only
- **EU data residency**: Supabase EU region required
- **PII redaction**: Before any cloud NLU processing
- **User consent**: Explicit opt-in for microphone access

### Accuracy Metrics
- False Accept Rate (FAR): ≤ 0.5/hour
- False Reject Rate (FRR): ≤ 5%
- NLU success rate: ≥ 95%

## Development Workflow

### Environment Setup
1. **Supabase**: Set up EU region project with RLS enabled
2. **Vercel**: Deploy with environment variables
3. **Android Studio**: Kotlin + Compose project setup
4. **API Keys**: Gemini AI Studio, FCM, Resend

### Testing Strategy
- **Unit tests**: Intent parsing, RRULE generation
- **Integration tests**: End-to-end delivery flows
- **Performance tests**: Wake word latency, battery usage
- **Privacy tests**: No PII leakage, proper redaction

### Code Standards
- **Naming**: Follow existing camelCase (Android) / snake_case (DB) patterns
- **Error handling**: Graceful fallbacks for all external dependencies
- **Logging**: Structured logs with no PII
- **Commits**: Meaningful messages, no Claude signatures

## Configuration

### Required Environment Variables
```bash
# Supabase
SUPABASE_URL=https://xxx.supabase.co
SUPABASE_SERVICE_ROLE_KEY=eyJ...

# AI/NLU
GEMINI_API_KEY=AIza...

# Push notifications
FCM_SERVER_KEY=AAAA...

# Email
RESEND_API_KEY=re_...
TEST_FALLBACK_EMAIL=test@example.com
```

### Port Configuration
- **Avoid**: 5000-5004 (macOS AirDrop conflict)
- **Preferred**: 3000 (Next.js dev), 8000 (alternative), 5001 (fallback)

## Deployment

### Production Checklist
- [ ] EU region compliance (Supabase + Vercel Edge)
- [ ] RLS policies active on all tables
- [ ] Environment variables configured
- [ ] pg_cron job scheduled for deliveries
- [ ] FCM and Resend API limits configured
- [ ] Privacy policy and ToS updated

### Monitoring
- Wake word performance metrics
- NLU success rates and fallback usage
- Delivery success rates by channel
- User engagement (memories/day, retention)
- Privacy compliance audit logs

## Known Limitations & Roadmap

### MVP Scope
- **Included**: Android, push notifications, email/ICS
- **Excluded**: iOS, web dashboard, official social media integrations
- **Future**: WhatsApp/Instagram via share intents, web timeline

### Technical Debt
- RRULE computation currently simplified in SQL (full implementation in API)
- User email resolution hardcoded for testing
- Channel fallback logic pending
- TTS for memory replay (nice-to-have)

## Business Context
- **Target**: 1k MAU in 90 days, ≥3 memories/user/month
- **Monetization**: Freemium model with premium features
- **Market**: Portuguese-speaking users, family/professional use cases
- **Compliance**: GDPR, EU data residency, user consent flows