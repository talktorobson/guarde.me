# 🚀 Guarde.me MVP - Sprint Planning & Progress

## 📋 Project Overview

**Guarde.me** is a Portuguese voice-first Android app for capturing memories and scheduling intelligent delivery. Users say "Guarde me" + their memory, and the system uses AI to understand when and how to deliver it back to them.

### 🎯 MVP Goals
- Voice-first memory capture with wake word "Guarde me"
- Natural language processing for scheduling (Gemini AI)
- Multi-channel delivery (FCM push, email with .ics)
- RRULE-based recurring schedules
- Privacy-focused architecture with on-device processing

---

## 📊 Sprint Progress Tracker

### ✅ Sprint 0: Backend Infrastructure (COMPLETED)
**Duration**: 2 weeks  
**Status**: ✅ **100% COMPLETE**  
**Completed**: 2025-08-27  
**AI Integration**: ✅ **GEMINI OPERATIONAL** - Portuguese NLU working

#### Achievements:
- ✅ **Database Schema**: Complete PostgreSQL schema with 6 tables and RLS policies
- ✅ **Next.js 14 Backend**: App Router with TypeScript and Edge runtime
- ✅ **API Endpoints**: 3 core endpoints with proper validation
- ✅ **Supabase Integration**: Connected to existing project "Guarde.me"
- ✅ **Type Safety**: Generated TypeScript types from database schema
- ✅ **Build Process**: Production-ready with successful compilation
- ✅ **Development Environment**: Local server running on port 3000

#### Technical Implementation:
```yaml
Database:
  - profiles: User preferences & settings
  - memories: Multimodal content storage  
  - schedules: RRULE-based scheduling
  - deliveries: Delivery execution tracking
  - push_tokens: FCM token management
  - intent_logs: Redacted NLU audit trail

API Endpoints:
  - POST /api/intent/decode: Gemini AI intent parsing
  - POST /api/schedule/create: Memory and schedule creation  
  - POST /api/deliver/run: Delivery execution

Configuration:
  - Environment variables configured for Supabase
  - Tailwind CSS for future UI development
  - ESLint with strict TypeScript rules
```

#### 🎯 Sprint 0 Results:
- **Build Time**: ✅ < 10 seconds
- **Type Safety**: ✅ 100% TypeScript coverage (warnings only for necessary `any` types)
- **Database**: ✅ Optimized with proper indexing and RLS
- **Documentation**: ✅ Comprehensive README and implementation summary

---

### 🔄 Sprint 1: Android App Foundation (NEXT)
**Duration**: 3-4 weeks  
**Status**: 🔄 **PENDING**  
**Priority**: HIGH

#### 📋 Sprint 1 Development Cards:

---

### 🎤 **EPIC 1: Voice & Wake Word System**

#### 🎫 **S1-001: Android Voice Recognition Setup**
**Story**: As a user, I want to activate the app with "Guarde me" so I can quickly capture memories hands-free.

**Tasks**:
- [ ] Set up Android Speech-to-Text service with Portuguese locale
- [ ] Implement wake word detection using Android Speech Recognition API
- [ ] Configure background voice processing with proper permissions
- [ ] Add audio recording permissions and user consent flow
- [ ] Test wake word accuracy with different Portuguese accents

**Acceptance Criteria**:
- Wake word "Guarde me" triggers recording 95% of the time
- Portuguese language detection working correctly
- Background processing doesn't drain > 3% battery daily
- Proper permission handling and user privacy controls

**Effort**: 8 points | **Priority**: High

---

#### 🎫 **S1-002: Voice Recording & Processing**
**Story**: As a user, I want my voice to be clearly captured and processed so my memories are accurately understood.

**Tasks**:
- [ ] Implement audio capture with noise reduction
- [ ] Add real-time voice-to-text conversion
- [ ] Create local audio file storage for backup/training
- [ ] Implement privacy controls for voice data
- [ ] Add voice recording UI with visual feedback

**Acceptance Criteria**:
- Clear audio capture in various environments
- Real-time transcription with < 2s latency
- Local storage with automatic cleanup
- User can control voice data retention

**Effort**: 5 points | **Priority**: High

---

### 🤖 **EPIC 2: AI Integration & Intent Processing**

#### 🎫 **S1-003: Gemini AI Client Integration**
**Story**: As a user, I want my voice commands to be understood correctly so the app can schedule my memories appropriately.

**Tasks**:
- [ ] Create Android HTTP client for /api/intent/decode endpoint
- [ ] Implement JSON serialization for request/response
- [ ] Add error handling and retry logic with exponential backoff
- [ ] Create offline capability with local caching
- [ ] Add network connectivity checks

**Acceptance Criteria**:
- Successful API calls to working backend endpoint
- Proper error handling for network failures
- Offline mode with cached responses
- < 2s response time for intent processing

**Effort**: 5 points | **Priority**: High

---

#### 🎫 **S1-004: Portuguese NLU Integration**
**Story**: As a Portuguese speaker, I want the app to understand my time expressions and scheduling requests naturally.

**Tasks**:
- [ ] Integrate with working Gemini API for Portuguese processing
- [ ] Handle Portuguese time expressions ("em 1 ano", "amanhã", "próxima semana")
- [ ] Extract scheduling information from voice commands
- [ ] Implement context-aware intent resolution
- [ ] Add fallback handling for ambiguous commands

**Acceptance Criteria**:
- 90% accuracy on common Portuguese scheduling phrases
- Proper time parsing ("1 ano" → "P365D")
- Context awareness for relative time expressions
- Graceful handling of unclear commands

**Effort**: 8 points | **Priority**: High

---

### 📱 **EPIC 3: Core Android Architecture**

#### 🎫 **S1-005: Project Setup & Architecture**
**Story**: As a developer, I want a well-structured Android project so development is efficient and maintainable.

**Tasks**:
- [ ] Create new Android project with Kotlin + Jetpack Compose
- [ ] Set up MVVM architecture with ViewModels
- [ ] Configure Room database for local storage
- [ ] Implement dependency injection with Hilt
- [ ] Set up project structure with proper packages

**Acceptance Criteria**:
- Clean architecture following Android best practices
- Proper separation of concerns (UI, Domain, Data)
- Database setup with migrations
- DI container configured for all modules

**Effort**: 8 points | **Priority**: High

---

#### 🎫 **S1-006: Supabase Authentication Integration**
**Story**: As a user, I want to securely log into the app so my memories are private and synced across devices.

**Tasks**:
- [ ] Integrate Supabase Auth SDK for Android
- [ ] Implement user registration and login flows
- [ ] Add session management and token refresh
- [ ] Create profile creation and onboarding flow
- [ ] Add biometric authentication option

**Acceptance Criteria**:
- Secure user registration and login
- Session persistence across app restarts
- Profile creation with user preferences
- Biometric login option available

**Effort**: 8 points | **Priority**: High

---

### 🔔 **EPIC 4: Notification System**

#### 🎫 **S1-007: FCM Push Notifications**
**Story**: As a user, I want to receive push notifications when my memories are ready for delivery.

**Tasks**:
- [ ] Set up Firebase Cloud Messaging in Android project
- [ ] Implement FCM token registration with backend
- [ ] Create notification handling and display logic
- [ ] Add custom notification actions (View, Dismiss, Snooze)
- [ ] Handle notification permissions (Android 13+)

**Acceptance Criteria**:
- FCM tokens properly registered with backend
- Notifications display correctly with memory content
- Custom actions work properly
- Proper permission handling

**Effort**: 5 points | **Priority**: Medium

---

#### 🎫 **S1-008: Local Notification Scheduling**
**Story**: As a user, I want local backup notifications so I don't miss important memories even without internet.

**Tasks**:
- [ ] Implement Android AlarmManager integration
- [ ] Create notification scheduling system
- [ ] Add background processing for scheduled notifications
- [ ] Implement battery optimization handling
- [ ] Add notification settings and preferences

**Acceptance Criteria**:
- Local notifications trigger at correct times
- Background processing survives device restarts
- Battery optimization properly handled
- User can customize notification preferences

**Effort**: 5 points | **Priority**: Medium

---

### 🎨 **EPIC 5: Core UI & User Experience**

#### 🎫 **S1-009: Main Voice Capture Interface**
**Story**: As a user, I want an intuitive interface to capture and review my voice memories.

**Tasks**:
- [ ] Design and implement main capture screen with Compose
- [ ] Add voice recording visualization (waveform, pulse animation)
- [ ] Create memory preview and confirmation UI
- [ ] Implement recording controls (start, stop, cancel)
- [ ] Add accessibility features for voice interface

**Acceptance Criteria**:
- Intuitive voice recording interface
- Visual feedback during recording
- Memory preview before confirmation
- Accessibility compliant

**Effort**: 8 points | **Priority**: High

---

#### 🎫 **S1-010: Onboarding & Setup Flow**
**Story**: As a new user, I want a smooth onboarding experience so I can quickly start using the app.

**Tasks**:
- [ ] Create welcome screen with app introduction
- [ ] Design permission request flows (microphone, notifications)
- [ ] Implement voice training/calibration screen
- [ ] Add tutorial for wake word usage
- [ ] Create account setup integration with Supabase

**Acceptance Criteria**:
- Complete onboarding in < 3 minutes
- All necessary permissions granted
- User understands wake word functionality
- Account properly created and authenticated

**Effort**: 5 points | **Priority**: High

**Sprint 1 Total**: 69 points

#### 📊 Sprint 1 Success Metrics:
- Voice wake word detection: 95% accuracy
- Intent parsing: 90% success rate for common phrases
- App response time: < 2 seconds for voice processing
- User onboarding completion: < 3 minutes

---

### 🎯 Sprint 2: Memory Management & Scheduling (PLANNED)
**Duration**: 2-3 weeks  
**Status**: 📅 **PLANNED**

#### 📋 Sprint 2 Development Cards:

---

### 💾 **EPIC 6: Multimodal Memory System**

#### 🎫 **S2-001: Text Memory Enhancement**
**Story**: As a user, I want to create rich text memories with formatting so I can organize my thoughts better.

**Tasks**:
- [ ] Implement rich text editor with Compose
- [ ] Add text formatting options (bold, italic, lists)
- [ ] Create text memory templates
- [ ] Add markdown support for power users
- [ ] Implement text search functionality

**Acceptance Criteria**:
- Rich text editing with preview
- Common formatting options available
- Search works across all text content
- Templates speed up memory creation

**Effort**: 5 points | **Priority**: High

---

#### 🎫 **S2-002: Audio Memory System**
**Story**: As a user, I want to save and replay audio memories so I can capture moments that text can't describe.

**Tasks**:
- [ ] Implement audio recording with quality controls
- [ ] Add audio playback with controls (play, pause, seek)
- [ ] Create audio waveform visualization
- [ ] Add audio compression and storage optimization
- [ ] Implement audio transcription for searchability

**Acceptance Criteria**:
- High-quality audio recording and playback
- Waveform visualization during playback
- Automatic transcription for search
- Efficient storage usage

**Effort**: 8 points | **Priority**: High

---

#### 🎫 **S2-003: Photo & Visual Memory Integration**
**Story**: As a user, I want to attach photos to my memories so I can capture visual moments.

**Tasks**:
- [ ] Implement camera integration for photo capture
- [ ] Add gallery selection for existing photos
- [ ] Create screenshot capture functionality
- [ ] Add image compression and optimization
- [ ] Implement image preview and editing tools

**Acceptance Criteria**:
- Camera and gallery integration working
- Screenshot capture from other apps
- Proper image compression and quality
- Basic editing tools (crop, rotate)

**Effort**: 8 points | **Priority**: Medium

---

#### 🎫 **S2-004: Link Preview & Web Integration**
**Story**: As a user, I want to save web links with previews so I can remember interesting content.

**Tasks**:
- [ ] Implement URL detection in text input
- [ ] Create web link preview generation
- [ ] Add web content extraction and summarization
- [ ] Implement bookmark-style link storage
- [ ] Add link validation and error handling

**Acceptance Criteria**:
- Automatic URL detection and preview
- Rich preview with title, description, image
- Content summarization for long articles
- Offline access to saved link data

**Effort**: 5 points | **Priority**: Low

---

### 🔐 **EPIC 7: Privacy & Security System**

#### 🎫 **S2-005: End-to-End Encryption (Premium)**
**Story**: As a privacy-conscious user, I want my sensitive memories encrypted so only I can access them.

**Tasks**:
- [ ] Implement client-side encryption for memory content
- [ ] Add encryption key management and derivation
- [ ] Create secure key storage using Android Keystore
- [ ] Add encryption status indicators in UI
- [ ] Implement encrypted sync with backend

**Acceptance Criteria**:
- Strong encryption for sensitive memories
- Secure key management
- Encrypted data sync with Supabase
- Clear encryption status for users

**Effort**: 13 points | **Priority**: Medium

---

#### 🎫 **S2-006: Biometric Security & Privacy Modes**
**Story**: As a user, I want biometric protection for sensitive memories so they're secure even if my phone is accessed.

**Tasks**:
- [ ] Integrate Android BiometricPrompt API
- [ ] Implement biometric lock for specific memories
- [ ] Create privacy mode for sensitive content
- [ ] Add memory visibility controls
- [ ] Implement secure memory deletion

**Acceptance Criteria**:
- Biometric authentication for protected memories
- Privacy mode hides sensitive content
- Secure deletion removes all traces
- User controls over memory visibility

**Effort**: 8 points | **Priority**: Medium

---

### 📅 **EPIC 8: Advanced Scheduling System**

#### 🎫 **S2-007: RRULE Implementation**
**Story**: As a user, I want to create recurring memories so I can be reminded of important things regularly.

**Tasks**:
- [ ] Implement RRULE parser for complex recurrence patterns
- [ ] Create UI for recurrence rule creation
- [ ] Add timezone handling and DST support
- [ ] Implement schedule modification and cancellation
- [ ] Add recurrence preview and validation

**Acceptance Criteria**:
- Support for complex recurrence patterns (daily, weekly, monthly, yearly)
- Proper timezone handling
- Easy schedule modification
- Clear recurrence preview for users

**Effort**: 13 points | **Priority**: High

---

#### 🎫 **S2-008: Smart Scheduling Features**
**Story**: As a user, I want intelligent scheduling suggestions so I can optimize when I receive my memories.

**Tasks**:
- [ ] Implement AI-powered schedule suggestions based on content
- [ ] Add conflict detection for overlapping schedules
- [ ] Create batch scheduling operations
- [ ] Design schedule templates for common patterns
- [ ] Add smart reminder timing optimization

**Acceptance Criteria**:
- AI suggests optimal delivery times
- Conflict detection prevents overlaps
- Batch operations for multiple memories
- Template system for common schedules

**Effort**: 8 points | **Priority**: Medium

---

### 📋 **EPIC 9: Memory Organization & Management**

#### 🎫 **S2-009: Tagging & Categorization System**
**Story**: As a user, I want to organize my memories with tags so I can find them easily later.

**Tasks**:
- [ ] Implement tag creation and management
- [ ] Add auto-tagging based on content analysis
- [ ] Create tag-based filtering and search
- [ ] Design tag visualization and statistics
- [ ] Add tag hierarchy and relationships

**Acceptance Criteria**:
- Easy tag creation and management
- Smart auto-tagging suggestions
- Fast tag-based filtering
- Visual tag organization

**Effort**: 5 points | **Priority**: Medium

---

#### 🎫 **S2-010: Memory Search & Discovery**
**Story**: As a user, I want powerful search capabilities so I can quickly find specific memories.

**Tasks**:
- [ ] Implement full-text search across all memory types
- [ ] Add date range and time-based filtering
- [ ] Create semantic search using AI embeddings
- [ ] Design search history and saved searches
- [ ] Add search suggestions and autocomplete

**Acceptance Criteria**:
- Fast full-text search across all content types
- Advanced filtering options
- Semantic search for concept-based queries
- Search suggestions improve user experience

**Effort**: 8 points | **Priority**: High

**Sprint 2 Total**: 68 points

#### 📊 Sprint 2 Success Metrics:
- Memory creation success rate: 99%
- Multimodal support: Text, audio, photo, links working
- Schedule accuracy: 95% on-time delivery with RRULE
- Storage efficiency: < 50MB for 100 memories
- Search performance: < 500ms for full-text queries
- User engagement: Daily active usage with 3+ memory types
- Privacy: E2E encryption working for premium users

---

### 🚀 Sprint 3: Delivery System & UX Polish (PLANNED)
**Duration**: 2-3 weeks  
**Status**: 📅 **PLANNED**

#### 📋 Sprint 3 Backlog:

##### 📬 Multi-Channel Delivery
- [ ] **Enhanced Push Notifications**
  - Rich media notifications
  - Interactive notification actions
  - Custom notification sounds
  - Delivery confirmation tracking

- [ ] **Email Integration**
  - Beautiful email templates
  - Calendar (.ics) attachments
  - Email delivery preferences
  - Delivery status tracking

##### 🎨 User Experience Enhancement
- [ ] **Intuitive UI/UX**
  - Material Design 3 implementation
  - Dark mode support
  - Accessibility improvements
  - Animation and micro-interactions

- [ ] **Memory Management Interface**
  - Search and filter capabilities
  - Tag-based organization
  - Bulk operations
  - Export functionality

#### 📊 Sprint 3 Success Metrics:
- Delivery success rate: 99.5%
- User satisfaction: 4.5+ stars
- App performance: 60fps smooth scrolling
- Crash rate: < 0.1%

---

## 🔧 Configuration & Prerequisites

### ⚙️ Environment Setup Required

#### Backend Configuration (.env.local):
```bash
# Supabase (✅ CONFIGURED)
NEXT_PUBLIC_SUPABASE_URL=https://cnegjzoiryjoyvzffvyh.supabase.co
NEXT_PUBLIC_SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
SUPABASE_SERVICE_ROLE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# AI Integration (✅ CONFIGURED AND OPERATIONAL)
GEMINI_API_KEY=configured_and_working

# Delivery APIs (🔄 PENDING FOR SPRINT 1)
FCM_SERVER_KEY=your_firebase_server_key  
RESEND_API_KEY=your_resend_api_key
TEST_FALLBACK_EMAIL=your_test_email
```

#### Database Setup:
- ✅ **Connection**: PostgreSQL on Supabase
- ✅ **Migrations**: All schema migrations applied
- ✅ **RLS Policies**: Row Level Security configured
- ✅ **Functions**: Helper functions for delivery management
- 🔄 **pg_cron**: Automated delivery scheduling (pending setup)

#### Android Prerequisites:
- [ ] **Development Environment**
  - Android Studio with latest SDK
  - Kotlin 1.9+ with Compose support
  - Firebase project configuration
  - Google Cloud API credentials

---

## 📈 Success Metrics & KPIs

### 🎯 MVP Success Criteria

#### Technical Metrics:
- **Voice Recognition Accuracy**: > 95%
- **Intent Processing Success**: > 90%
- **Delivery Success Rate**: > 99%
- **App Response Time**: < 2 seconds
- **Crash Rate**: < 0.1%

#### User Experience Metrics:
- **Onboarding Completion**: > 80%
- **Daily Active Users**: Target growth
- **Memory Creation Rate**: > 1 per day per active user
- **User Retention**: 30% after 30 days

#### Business Metrics:
- **Time to MVP**: 10-12 weeks
- **Feature Completion**: 100% core features
- **Code Quality**: Zero critical bugs
- **Performance**: 60fps smooth operation

---

## 🚨 Risks & Mitigations

### 🔴 High Risk Items:
1. **Voice Recognition in Portuguese**
   - Risk: Accuracy issues with Portuguese accents/dialects
   - Mitigation: Extensive testing with diverse speakers

2. **Battery Optimization**
   - Risk: Background voice processing draining battery
   - Mitigation: Efficient wake word detection, power optimization

3. **Privacy Compliance**
   - Risk: LGPD compliance for voice data processing
   - Mitigation: On-device processing, explicit consent flows

### 🟡 Medium Risk Items:
1. **Gemini AI Rate Limits**
   - Mitigation: Caching, fallback processing, rate limiting

2. **Android Fragmentation**
   - Mitigation: Target Android 8+ (API 26+), extensive device testing

---

## 📅 Timeline & Milestones

### 🗓️ Overall MVP Timeline: 10-12 Weeks

```
Week 1-2:  ✅ Sprint 0 - Backend Infrastructure (COMPLETED)
Week 3-6:  🔄 Sprint 1 - Android App Foundation
Week 7-9:  📅 Sprint 2 - Memory Management & Scheduling  
Week 10-12: 🚀 Sprint 3 - Delivery System & UX Polish
```

### 🏆 Key Milestones:
- **Week 2**: ✅ Backend fully operational
- **Week 6**: 🎯 Working Android app with voice capture
- **Week 9**: 🎯 Complete memory management system
- **Week 12**: 🎯 MVP ready for beta testing

---

## 🔄 Next Actions

### 🚀 Immediate Next Steps (Sprint 1 Start):

1. **Configure External APIs** (Priority: HIGH)
   - Obtain Google AI Studio API key for Gemini
   - Set up Firebase project for FCM
   - Configure Resend for email delivery

2. **Set up pg_cron in Supabase** (Priority: MEDIUM)
   - Enable automated delivery processing
   - Schedule regular /api/deliver/run execution

3. **Begin Android Development** (Priority: HIGH)
   - Create new Android project with Kotlin + Compose
   - Set up project architecture and dependencies
   - Implement basic authentication flow

### 📋 Sprint 1 Kickoff Checklist:
- [ ] External API keys configured and tested
- [ ] Android Studio project created
- [ ] Team/developer ready for mobile development
- [ ] Backend endpoints tested and validated
- [ ] Sprint 1 tasks prioritized and estimated

---

*Last Updated: 2025-08-27*  
*Sprint 0 Status: COMPLETED*  
*Next Sprint: Android App Foundation*  

---

## 📋 **Development Cards Summary**

### **Sprint 1: Android App Foundation** (69 points)
- 🎤 **EPIC 1**: Voice & Wake Word System (13 points)
- 🤖 **EPIC 2**: AI Integration & Intent Processing (13 points)
- 📱 **EPIC 3**: Core Android Architecture (16 points)
- 🔔 **EPIC 4**: Notification System (10 points)
- 🎨 **EPIC 5**: Core UI & User Experience (13 points)

### **Sprint 2: Memory Management & Scheduling** (68 points)
- 💾 **EPIC 6**: Multimodal Memory System (26 points)
- 🔐 **EPIC 7**: Privacy & Security System (21 points)
- 📅 **EPIC 8**: Advanced Scheduling System (21 points)
- 📋 **EPIC 9**: Memory Organization & Management (13 points)

**Total Sprint Points**: S1 = 69 points | S2 = 68 points  
**Estimated Velocity**: 25-30 points per week  
**Sprint 1 Duration**: 3-4 weeks | **Sprint 2 Duration**: 2-3 weeks  

### 🏆 **Epic Priority Breakdown**:

#### **High Priority** (Must-have for MVP):
- Voice & Wake Word System
- AI Integration & Intent Processing  
- Core Android Architecture
- Core UI & User Experience
- Text Memory Enhancement
- Audio Memory System
- RRULE Implementation
- Memory Search & Discovery

#### **Medium Priority** (Important for user experience):
- FCM Push Notifications
- Local Notification Scheduling
- Photo & Visual Memory Integration
- End-to-End Encryption
- Biometric Security & Privacy Modes
- Smart Scheduling Features
- Tagging & Categorization System

#### **Low Priority** (Nice-to-have features):
- Link Preview & Web Integration