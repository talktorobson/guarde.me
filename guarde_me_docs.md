# 📄 01_Solution_Blueprint.md (Updated for Vercel + Supabase)

## Problem Statement
- Memórias e insights se perdem no dia a dia.
- Apps atuais não entregam contexto + emoção + timing personalizado.
- Necessidade de solução simples, móvel, hands-free e confiável.

## Competitive Landscape
- **Notion/Keep**: foco em notas, sem emoção.
- **Timehop/FB Memories**: não controlável.
- **Journaling apps**: individuais, não sociais.
- **Slack/Teams retros**: só corporativos.
- **Guarde.me**: omnichannel, replay emocional, entrega adaptada + ativação por voz.

## Objectives & KPIs
- Adoção inicial: 1k usuários ativos/3 meses.
- Engajamento: ≥3 memórias/mês/usuário.
- Retenção: ≥50% após 3 meses.
- SLA entregas: 95% ±1 min.
- Latência wake word→UI: <500ms.

## User Archetypes
- Família/Pessoal.
- Profissional.
- Espiritual.
- Estudantes.

## Capabilities Scope
- **Voice-first**: hotword “Guarde me” hands-free.
- Multimodal capture: texto, áudio, foto, screenshot, link.
- NLU (on-device + Gemini fallback) → slots: {what, when, how}.
- Scheduling (ISO-8601 + RRULE recurrences).
- Delivery channels: in-app push (FCM), e-mail (ICS/attachments), calendar, later WhatsApp/social.
- Timeline in-app.

## System Context
- **Frontend:** Android app (Kotlin + Compose) + wake word.
- **Backend:** Vercel serverless API + Supabase DB/storage.
- **Storage:** Supabase Postgres (data) + Supabase Storage (media).
- **Scheduling:** pg_cron + function to trigger due deliveries.
- **Delivery:** FCM (push), Resend (email).
- **NLU:** on-device parser; Gemini API (AI Studio) fallback.

## High-Level Architecture
- **Mobile:** Android app with wake word + UI timeline.
- **API:** Vercel functions (`/intent/decode`, `/schedule/create`, `/deliver/send`).
- **Data:** Supabase Postgres.
- **Storage:** Supabase Storage buckets.
- **Scheduler:** pg_cron SELECT → call delivery API.
- **NLU:** Gemini API (few-shot JSON schema output).
- **Delivery:** FCM, Resend (email w/ .ics).

## Data Model Outline
- **users**(id, email, plan, created_at)
- **memories**(id, user_id, type, content_url/text, tags, created_at)
- **schedules**(id, memory_id, when_type, when_value, recurrence_rrule, channel, status)
- **deliveries**(id, schedule_id, status, attempts, last_error, sent_at)

## Non-functional Requirements
- Latency p95 < 1.5s para schedule.
- Hotword latency <500ms.
- FAR ≤0.5/h; FRR ≤5%.
- Battery impact ≤3%/dia.
- GDPR compliance; EU data residency (Supabase eu-west-1).

## Risks & Mitigations
- Custo WhatsApp → não core no MVP.
- Falsos positivos hotword → toggle + fallback botão.
- Privacidade microfone → on-device, indicador, kill-switch.
- Dependência externa (Gemini, FCM) → fallback UX.

---

# 📄 02_PRD.md (Updated for Vercel + Supabase)

## Summary
Guarde.me é um app Android voice-first para guardar memórias multimodais com agendamento inteligente e entregas personalizadas.

## Goals
- MVP em 12 semanas.
- Beta com 100 usuários.
- Engajamento emocional e utilitário.

## Non-Goals
- Dashboard web completo no MVP.
- iOS no MVP.
- Vídeo longo.

## MoSCoW
- **Must:** Wake word, captura multimodal, intent parse, agendamento (ISO/RRULE), timeline local, replay push/email.
- **Should:** TTS replay, templates visuais, offline sync.
- **Could:** Tags automáticas, resumos mensais.
- **Won’t:** Web dashboard, integrações sociais completas.

## Detailed Requirements
- **REQ-001:** Hotword wake word “Guarde me” (opt-in, indicator).
- **REQ-002:** Capture multimodal (text/audio/photo/screenshot).
- **REQ-003:** Intent parse (on-device, Gemini fallback JSON schema).
- **REQ-004:** Scheduling (ISO-8601, RRULE recurrence).
- **REQ-005:** Delivery channels MVP: FCM push, e-mail (Resend with .ics/attachments).
- **REQ-006:** Timeline local in-app.
- **REQ-007:** Criptografia em trânsito/repouso.
- **REQ-008:** Consentimento microfone; toggle/kill switch.
- **REQ-009:** Data residency EU.

## UX Notes
- Conversational, minimal interruptions.
- Compact confirmation toast ("Guardar texto, sexta 18h, WhatsApp?").

## Analytics & Metrics
- Memórias salvas/dia.
- Latência hotword→UI.
- Intent parse success %.
- Entregas on-time SLA.

## Release Plan
- Sprint 0: Supabase/Vercel infra, wake word prototype.
- Sprint 1: Captura texto+voz, schedule básico.
- Sprint 2: Push delivery, timeline.
- Sprint 3: Email/ICS, offline sync.

## Dependencies
- Supabase, Vercel, FCM, Resend, Gemini API.

---

# 📄 03_User_Stories.md (Updated)

## Epic 1 – Capture
- **US-001:** Como usuário, quero dizer "Guarde me" + conteúdo (texto/foto/áudio), para salvar rapidamente.
- **US-002:** Como usuário, quero confirmar antes de salvar (toast ou voz).
- **US-003:** Como usuário, quero capturar offline e sincronizar depois.

## Epic 2 – Scheduling
- **US-004:** Como usuário, quero falar quando quero receber (sexta 18h, aniversário, todo mês).
- **US-005:** Como usuário, quero recorrências (RRULE) para memórias.

## Epic 3 – Delivery
- **US-006:** Como usuário, quero replay por push notification e timeline.
- **US-007:** Como usuário, quero receber por e-mail com .ics e anexos.
- **US-008:** Como usuário, quero fallback de canal se falhar (ex: push→email).

## Epic 4 – Privacy
- **US-009:** Como usuário, quero toggle de hotword + kill-switch.
- **US-010:** Como usuário, quero e2e premium para memórias sensíveis.

## Epic 5 – NLU
- **US-011:** Como usuário, quero que o app entenda frases complexas ("me lembra toda segunda às 9h por email").

---

# 📄 04_HLTC.md (Updated)

## HLTC-001 – Wake Word Activation
- **Steps:** Usuário diz “Guarde me” em vários ambientes.
- **Expected:** UI em <500ms; FAR/FRR metas.

## HLTC-002 – Intent Parse (Gemini Fallback)
- **Steps:** Frase complexa com conteúdo, quando e como.
- **Expected:** JSON válido {intent, slots}, confirmação.

## HLTC-003 – Scheduling RRULE
- **Steps:** “Todo mês dia 1 às 9h”.
- **Expected:** RRULE gerado válido; armazenado em schedules.

## HLTC-004 – Delivery Push
- **Steps:** Agendar memória, esperar trigger.
- **Expected:** Push FCM ±1 min.

## HLTC-005 – Delivery Email
- **Steps:** Agendar por email.
- **Expected:** Email Resend com .ics + attachments.

## HLTC-006 – Offline Capture & Sync
- **Steps:** Salvar offline, reconectar.
- **Expected:** Memória sincronizada; sem duplicatas.

## HLTC-007 – Privacy & Consent
- **Steps:** Revogar permissão microfone.
- **Expected:** Serviço parado; indicador some.

---

# 🗄️ Database Schema (SQL for Supabase)

```sql
create table users (
  id uuid primary key default gen_random_uuid(),
  email text unique not null,
  plan text default 'free',
  created_at timestamptz default now()
);

create table memories (
  id uuid primary key default gen_random_uuid(),
  user_id uuid references users(id) on delete cascade,
  type text check (type in ('text','audio','photo','screenshot','link')),
  content_text text,
  content_url text,
  tags text[],
  created_at timestamptz default now()
);

create table schedules (
  id uuid primary key default gen_random_uuid(),
  memory_id uuid references memories(id) on delete cascade,
  when_type text check (when_type in ('date','datetime','recurrence')),
  when_value timestamptz,
  recurrence_rrule text,
  channel text check (channel in ('push','email','calendar','whatsapp','instagram','facebook')),
  status text default 'pending',
  created_at timestamptz default now()
);

create table deliveries (
  id uuid primary key default gen_random_uuid(),
  schedule_id uuid references schedules(id) on delete cascade,
  status text default 'scheduled',
  attempts int default 0,
  last_error text,
  sent_at timestamptz
);
```

---

# 🔌 API Handlers (Vercel, Node/TS)

```ts
// /api/intent/decode.ts
import { NextRequest, NextResponse } from 'next/server';
import { GoogleGenerativeAI } from "@google/generative-ai";

export async function POST(req: NextRequest) {
  const body = await req.json(); // { transcript }
  // TODO: redact PII client-side
  const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY!);
  const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });
  const prompt = `Extract intent and slots as JSON {intent, slots{content_type, when_type, when_value, channel}} from: ${body.transcript}`;
  const result = await model.generateContent(prompt);
  const text = result.response.text();
  return NextResponse.json(JSON.parse(text));
}
```

```ts
// /api/schedule/create.ts
import { createClient } from '@supabase/supabase-js';

const supabase = createClient(process.env.SUPABASE_URL!, process.env.SUPABASE_KEY!);

export async function POST(req) {
  const { memory_id, when_type, when_value, recurrence_rrule, channel } = await req.json();
  const { data, error } = await supabase.from('schedules').insert({
    memory_id, when_type, when_value, recurrence_rrule, channel
  }).select();
  if (error) return new Response(error.message, { status: 400 });
  return new Response(JSON.stringify(data[0]));
}
```

```ts
// /api/deliver/send.ts
import { createClient } from '@supabase/supabase-js';
import { sendEmail } from './lib/email';
import { sendPush } from './lib/push';

const supabase = createClient(process.env.SUPABASE_URL!, process.env.SUPABASE_KEY!);

export async function POST(req) {
  const { schedule_id } = await req.json();
  const { data: schedule } = await supabase.from('schedules').select('*').eq('id', schedule_id).single();
  if (!schedule) return new Response('Not found', { status: 404 });

  try {
    if (schedule.channel === 'push') {
      await sendPush(schedule);
    } else if (schedule.channel === 'email') {
      await sendEmail(schedule);
    }
    await supabase.from('deliveries').insert({ schedule_id, status: 'sent', sent_at: new Date() });
    return new Response('OK');
  } catch (err:any) {
    await supabase.from('deliveries').insert({ schedule_id, status: 'error', last_error: err.message });
    return new Response('Error', { status: 500 });
  }
}
```

---

# ⏰ pg_cron Job (Supabase)

```sql
-- Function to trigger due deliveries
create or replace function trigger_due_deliveries()
returns void as $$
begin
  for rec in
    select s.id from schedules s
    where s.status = 'pending'
      and (
        (s.when_type = 'datetime' and s.when_value <= now())
        or (s.when_type = 'recurrence' and now() >= next_occurrence(s.recurrence_rrule))
      )
  loop
    perform
      net.http_post(
        url := 'https://your-vercel-app.vercel.app/api/deliver/send',
        headers := json_build_object('Content-Type','application/json'),
        body := json_build_object('schedule_id', rec.id)::text
      );
    update schedules set status='triggered' where id = rec.id;
  end loop;
end;
$$ language plpgsql;

-- Run every minute
select cron.schedule('deliveries-job', '* * * * *', $$call trigger_due_deliveries();$$);
```

---


---

# ✅ V2 — Android Voice‑First + Vercel/Supabase Update (Replaces Prior Drafts)

Below are the **four updated Markdown docs** aligned to an **Android voice‑first MVP** running on **Vercel + Supabase + FCM + Gemini (AI Studio)**.

---

## 📄 01_Solution_Blueprint_v2.md

```markdown
# Guarde.me — Solution Blueprint (v2: Android + Vercel/Supabase)

## Problem Statement
- Momentos e insights se perdem porque capturar é friccionado e revisar não é automático.
- Apps genéricos (notas/mensageiros) não entendem **o que**, **quando** e **como** entregar depois.
- Precisamos de uma captura **mão‑livre** e de uma entrega **no momento certo**.

## Differentiation
- **Wake word** ("Guarde me") + **intenção por voz** (NLU) → decide **o que** salvar, **quando** (data/recorrência RRULE) e **como** (push, e‑mail/ICS, etc.).
- **Replay emocional** (timeline/scrapbook) + anexos (áudio/imagem/pdf) prontos para compartilhar.
- **Privacidade por design**: on‑device primeiro; sem upload de áudio bruto; redacted transcripts only quando usar Gemini.

## Objectives & KPIs
- 1k MAU em 90 dias; ≥ 3 memórias/usuário/mês.
- On‑time deliveries p95: ±1 min. NLU success ≥ 95%. Hotword→UI p95 < 500 ms. FAR ≤ 0,5/h; FRR ≤ 5%.
- Conversão free→premium ≥ 10%.

## User Archetypes
- **Pais/Família**: momentos afetivos (voz/fotos) com replays em datas marcantes.
- **Líderes/Times**: capsulas para retros, reconhecimento, 1:1s.
- **Estudos/Pitches**: lembretes com conteúdo agregado.

## Capability Scope (MVP)
- **Voice‑first**: wake word + comando natural (o que/quando/como).
- **Multimodal**: texto, voz, foto (screenshot/galeria no pós‑MVP), tags.
- **Agendamento**: data/horário e **recorrências** via **RRULE (RFC 5545)**.
- **Entrega**: push in‑app; **e‑mail com .ics e anexos**.
- **Timeline** local com filtros.

## System Context
- **Input**: Android app (Kotlin/Compose) com wake word on‑device e ASR on‑device.
- **Output**: FCM push + in‑app; e‑mail (Resend) com **.ics**; (WhatsApp/IG/FB via share intents no futuro).

## High‑Level Architecture (MVP)
- **Mobile**: Android (Compose). Foreground service (hotword). Offline‑first cache e sync.
- **Backend**: Vercel Serverless Functions (Node/TS). Edge Functions p/ normalização (ISO/RRULE) quando útil.
- **DB/Storage/Auth**: Supabase (Postgres + Storage + Auth). RLS ativo desde o dia 1.
- **Scheduling**: `pg_cron` + funções SQL para enfileirar; execução via Vercel function ou Supabase Edge Fn.
- **Push**: FCM.
- **E‑mail**: Resend (+ anexo **.ics** e mídia via URLs assinadas do Supabase Storage).
- **NLU Fallback**: **Gemini (AI Studio)** com redaction e schema JSON.
- **Observability**: Vercel Logs + Sentry; métricas no Postgres; auditoria básica.

## Data Model Outline
- **profiles**(user_id, locale, timezone, plan)
- **memories**(id, user_id, content_type, content_text, media_path, source, tags[], privacy_mode, created_at)
- **schedules**(id, user_id, memory_id, when_type, dtstart, rrule, timezone, status, next_run_at)
- **deliveries**(id, user_id, schedule_id, channel, status, attempt, last_error, run_at)
- **push_tokens**(id, user_id, token, platform, enabled, last_seen_at)

## Non‑Functional
- Hotword→UI p95 < 500 ms; FAR ≤ 0,5/h; FRR ≤ 5%.
- App battery budget ≤ 3%/dia (serviço ativo).
- p95 backend ≤ 300 ms para operações simples; ≤ 1.5 s para criar agendamento.
- Dados na UE (Supabase EU). RLS + criptografia em trânsito; mídia via URLs assinadas.

## Risks & Mitigations
- **Ambiguidade NLU**: confirmação curta + exemplos guiados; fallback editor RRULE.
- **Custos e‑mail**: Resend free tier; agregar digests.
- **Bateria**: sensibilidade configurável; pausa em DND/chamadas.
- **Privacidade**: sem áudio bruto; redaction antes de Gemini; opt‑in explícito.

## Assumptions
- Android first; iOS depois.
- WhatsApp/IG/FB integrações oficiais pós‑MVP; usar intents de compartilhamento no início.
```

---

## 📄 02_PRD_v2.md

```markdown
# PRD — Guarde.me (v2: Android + Vercel/Supabase)

## Summary
App Android voice‑first para capturar memórias com wake word (“Guarde me”), entender intenção (o que/quando/como), agendar (incl. RRULE) e entregar via push e e‑mail/ICS.

## Goals
- MVP em 10–12 semanas; beta com 100+ usuários.
- NLU success ≥ 95%; on‑time ≥ 99% (p95 ±1 min).

## Non‑Goals (MVP)
- iOS; web dashboard completo; vídeo longo; integrações oficiais WhatsApp/IG/FB.

## MoSCoW
- **Must**: wake word + ASR on‑device; intent decode (on‑device → Gemini fallback); RRULE; push (FCM); e‑mail/ICS; timeline; RLS; EU region.
- **Should**: TTS nos replays; offline‑first e sync; templates visuais; biometria para memórias protegidas.
- **Could**: tags automáticas; resumos mensais; share intents; Edge normalizers.
- **Won’t**: iOS, web full, APIs sociais oficiais no MVP.

## Detailed Requirements (IDs)
- **REQ-APP-001 (Must)**: Wake word em PT‑BR on‑device; indicador permanente; kill‑switch.
- **REQ-ASR-001 (Must)**: ASR on‑device; fallback cloud desativado por padrão.
- **REQ-NLU-001 (Must)**: Parser local para `content_type/channel/when` básicos.
- **REQ-NLU-002 (Must)**: Fallback **Gemini (AI Studio)** com **JSON schema** e redaction.
- **REQ-SCH-001 (Must)**: RRULE (RFC 5545) + timezone seguro (DST).
- **REQ-DEL-001 (Must)**: FCM push + tela de replay in‑app.
- **REQ-DEL-002 (Must)**: E‑mail via Resend com **.ics** e anexos.
- **REQ-DB-001 (Must)**: Supabase Postgres + Storage + Auth; RLS por `user_id`.
- **REQ-OPS-001 (Must)**: `pg_cron` + função de enfileirar; job a cada 1 min; logs e métricas.
- **REQ-SEC-001 (Must)**: Sem áudio bruto em logs; PII redaction antes de NLU cloud; política de retenção.

## UX Notes
- Confirmação de 1 linha: “Guardar **foto**, **toda sexta 18h**, **por e‑mail** — confirmar?”.
- Editor visual de recorrência como fallback.
- Timeline com filtros (tag/data/tipo).

## Analytics & KPIs
- Hotword→UI latência; FAR/FRR; NLU success; criação de agenda p95; on‑time delivery; open rate; resend/failures.

## Release Plan
- Sprint 0: base Android, wake word service, Supabase, Vercel project.
- Sprint 1: captura voz→texto; intent decode (local); schedule create; FCM push.
- Sprint 2: e‑mail/ICS; timeline; Gemini fallback; pg_cron; TTS opcional.

## Dependencies
- FCM, Resend, Supabase, Google AI Studio (Gemini), Sentry.

## Legal/Compliance
- EU data region; RLS; ToS/Privacy; consentimentos; export/delete data.
```

---

## 📄 03_User_Stories_v2.md

```markdown
# User Stories — v2

## Epic: Voice Capture & Intent
- **US-001:** Dizer “Guarde me” abre captura imediatamente. **AC:** UI < 500 ms; FAR ≤ 0,5/h; FRR ≤ 5%.
- **US-002:** Ditar o que salvar (texto/voz/foto) e o app entender. **AC:** `content_type` correto; preview rápido.
- **US-003:** Falar quando e como numa frase (“toda sexta 18h por e‑mail”). **AC:** RRULE válido; canal definido; confirmação curta.
- **US-004 (disambiguation):** Se faltar slot, 1 pergunta objetiva.

## Epic: Scheduling & Delivery
- **US-005:** Receber push no horário e abrir replay in‑app. **AC:** SLA 95% ±1 min.
- **US-006:** Receber e‑mail com **.ics** e anexos. **AC:** ICS importável; attachments corretos.
- **US-007:** Fallback de canal em falha. **AC:** Reenvio segundo regra; rastro em histórico.

## Epic: Privacy & Control
- **US-008:** Desligar hotword temporariamente. **AC:** serviço para; indicador some; sem escuta.
- **US-009:** Apagar memória para sempre. **AC:** removida + mídia; sem restos.
- **US-010:** Dados na UE. **AC:** recursos em região EU; auditoria básica.

## Epic: Timeline
- **US-011:** Ver memórias com filtros (tag/data/tipo). **AC:** resposta local < 300 ms.
```

---

## 📄 04_HLTC_v2.md

```markdown
# High‑Level Test Cases — v2

## HLTC-001 — Wake Word Latency & Accuracy
- **Maps:** US-001; **Expected:** UI < 500 ms; FAR/FRR nas metas.

## HLTC-002 — One‑shot Intent (what+when+how)
- **Maps:** US-002/003; **Expected:** slots corretos; confirmação 1 linha; persistência ≤ 1.5 s.

## HLTC-003 — RRULE & Timezones
- **Maps:** US-003; **Expected:** RRULE válido; execução pontual em DST.

## HLTC-004 — Push Delivery
- **Maps:** US-005; **Expected:** entrega p95 ±1 min; open rate registrado.

## HLTC-005 — Email + ICS + Attachments
- **Maps:** US-006; **Expected:** ICS abre em Gmail/Outlook/Calendar; anexos íntegros.

## HLTC-006 — Channel Fallback
- **Maps:** US-007; **Expected:** falha simulada → fallback; usuário notificado; logs.

## HLTC-007 — Privacy & Consent
- **Maps:** US-008/009; **Expected:** hotword kill‑switch; delete hard; sem PII em logs.

## HLTC-008 — EU Residency
- **Maps:** US-010; **Expected:** tabelas/buckets em EU; auditoria.

## HLTC-009 — Timeline Performance
- **Maps:** US-011; **Expected:** filtros locais < 300 ms.
```

---

# 🗄️ Supabase DB Schema (SQL)

```sql
-- Types
do $$
begin
  create type content_type as enum ('text','audio','photo','screenshot','image_link','selection');
exception when duplicate_object then null; end $$;

do $$ begin create type content_source as enum ('selected_text','camera','gallery','clipboard','url','screen_share'); exception when duplicate_object then null; end $$;

do $$ begin create type privacy_mode as enum ('standard','biometric_lock','e2e_premium'); exception when duplicate_object then null; end $$;

do $$ begin create type when_type as enum ('date','datetime','recurrence'); exception when duplicate_object then null; end $$;

do $$ begin create type schedule_status as enum ('scheduled','paused','canceled','completed'); exception when duplicate_object then null; end $$;

do $$ begin create type delivery_channel as enum ('in_app','push','email','calendar'); exception when duplicate_object then null; end $$;

do $$ begin create type delivery_status as enum ('pending','in_flight','succeeded','failed'); exception when duplicate_object then null; end $$;

-- Profiles (link to auth.users)
create table if not exists profiles (
  user_id uuid primary key references auth.users(id) on delete cascade,
  display_name text,
  locale text default 'pt-BR',
  timezone text default 'Europe/Paris',
  plan text default 'free',
  created_at timestamptz default now()
);

-- Memories
create table if not exists memories (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  content_type content_type not null,
  content_text text,
  media_path text, -- path in Supabase Storage
  source content_source,
  tags text[] default '{}',
  privacy_mode privacy_mode default 'standard',
  created_at timestamptz default now()
);
create index if not exists idx_memories_user_created on memories(user_id, created_at desc);

-- Schedules (one memory -> many schedules)
create table if not exists schedules (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  memory_id uuid not null references memories(id) on delete cascade,
  when_type when_type not null,
  dtstart timestamptz, -- for date/datetime
  rrule text,          -- for recurrence
  timezone text not null default 'Europe/Paris',
  status schedule_status not null default 'scheduled',
  next_run_at timestamptz, -- precomputed next occurrence
  created_at timestamptz default now()
);
create index if not exists idx_schedules_due on schedules(status, next_run_at);

-- Deliveries (instantiated executions)
create table if not exists deliveries (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  schedule_id uuid not null references schedules(id) on delete cascade,
  channel delivery_channel not null,
  status delivery_status not null default 'pending',
  attempt int not null default 0,
  last_error text,
  run_at timestamptz default now(),
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);
create index if not exists idx_deliveries_pending on deliveries(status, run_at);

-- Push tokens
create table if not exists push_tokens (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  platform text not null default 'android',
  token text not null,
  enabled boolean default true,
  last_seen_at timestamptz
);
create unique index if not exists uq_push_token on push_tokens(token);

-- Intent logs (redacted)
create table if not exists intent_logs (
  id uuid primary key default gen_random_uuid(),
  user_id uuid references auth.users(id) on delete cascade,
  transcript_redacted text not null,
  intent jsonb not null,
  created_at timestamptz default now()
);

-- RLS
alter table profiles enable row level security;
alter table memories enable row level security;
alter table schedules enable row level security;
alter table deliveries enable row level security;
alter table push_tokens enable row level security;
alter table intent_logs enable row level security;

create policy p_profiles_owner on profiles using (user_id = auth.uid());
create policy p_memories_owner on memories using (user_id = auth.uid());
create policy p_schedules_owner on schedules using (user_id = auth.uid());
create policy p_deliveries_owner on deliveries using (user_id = auth.uid());
create policy p_push_tokens_owner on push_tokens using (user_id = auth.uid());
create policy p_intent_logs_owner on intent_logs using (user_id = auth.uid());

-- Helper: claim pending deliveries (SKIP LOCKED semantics via FOR UPDATE)
create or replace function app_claim_pending_deliveries(max_rows int default 100)
returns setof deliveries
language plpgsql security definer as $$
begin
  return query
  with cte as (
    select id
    from deliveries
    where status = 'pending' and run_at <= now()
    order by run_at asc
    limit max_rows
  )
  update deliveries d
    set status = 'in_flight', attempt = attempt + 1, updated_at = now()
    from cte
    where d.id = cte.id
  returning d.*;
end; $$;

-- Enqueue due deliveries from schedules
create or replace function app_enqueue_due_deliveries()
returns void language plpgsql security definer as $$
declare r record;
begin
  for r in (
    select s.id as schedule_id, s.user_id, s.next_run_at
    from schedules s
    where s.status = 'scheduled' and s.next_run_at is not null and s.next_run_at <= now()
    for update skip locked
  ) loop
    insert into deliveries(user_id, schedule_id, channel, status, run_at)
    values (r.user_id, r.schedule_id, 'push', 'pending', coalesce(r.next_run_at, now()))
    on conflict do nothing;

    -- Advance next_run_at or complete schedule
    update schedules s
      set next_run_at = case
        when s.rrule is not null and s.rrule <> '' then s.next_run_at + interval '1 week' -- placeholder; recompute in app
        else null
      end,
      status = case when s.rrule is null or s.rrule = '' then 'completed' else s.status end
    where s.id = r.schedule_id;
  end loop;
end; $$;
```

> Note: recompute `next_run_at` precisely (RRULE) in the API after each delivery; the SQL above uses a placeholder increment for simplicity.

---

# 🛠️ Vercel API Handlers (Node/TypeScript)

## `/api/intent/decode.ts`
```ts
import { NextRequest, NextResponse } from 'next/server';
import { z } from 'zod';

const schema = z.object({
  intent: z.enum(['SAVE_MEMORY','SCHEDULE_REPLAY','DELIVERY_CHANNEL']),
  slots: z.object({
    content_type: z.enum(['text','voice','photo','screenshot','image_link','selection']).optional(),
    content_source: z.enum(['selected_text','camera','gallery','clipboard','url','screen_share']).optional(),
    topic_tags: z.array(z.string()).optional(),
    when_type: z.enum(['date','datetime','recurrence']).optional(),
    when_value: z.string().optional(), // ISO-8601 or RRULE
    channel: z.enum(['in_app','push','email','calendar']).optional()
  })
});

export const runtime = 'edge';

export default async function handler(req: NextRequest) {
  const { transcript_redacted, partial } = await req.json();
  // Few-shot prompt
  const prompt = {
    contents: [{
      role: 'user',
      parts: [{ text: `Extract intent and slots from: "${transcript_redacted}". Return JSON only matching this schema: ${schema.toString()}.` }]
    }]
  };

  const resp = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${process.env.GEMINI_API_KEY}`,
    { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(prompt) });
  const data = await resp.json();
  const text = data.candidates?.[0]?.content?.parts?.[0]?.text ?? '{}';
  const parsed = schema.safeParse(JSON.parse(text));
  if (!parsed.success) return NextResponse.json({ error: 'Invalid NLU' }, { status: 422 });
  return NextResponse.json(parsed.data);
}
```

## `/api/schedule/create.ts`
```ts
import { NextRequest, NextResponse } from 'next/server';
import { createClient } from '@supabase/supabase-js';

const supabase = createClient(process.env.SUPABASE_URL!, process.env.SUPABASE_SERVICE_ROLE_KEY!);

export async function POST(req: NextRequest) {
  const { user_id, memory, schedule } = await req.json();
  // Insert memory
  const { data: mem, error: mErr } = await supabase.from('memories').insert({
    user_id,
    content_type: memory.content_type,
    content_text: memory.content_text,
    media_path: memory.media_path,
    source: memory.source,
    tags: memory.tags,
    privacy_mode: memory.privacy_mode
  }).select().single();
  if (mErr) return NextResponse.json({ error: mErr.message }, { status: 400 });

  // Compute next_run_at from dtstart/rrule (server-side util)
  const next_run_at = schedule.dtstart;

  const { data: sch, error: sErr } = await supabase.from('schedules').insert({
    user_id,
    memory_id: mem.id,
    when_type: schedule.when_type,
    dtstart: schedule.dtstart,
    rrule: schedule.rrule,
    timezone: schedule.timezone || 'Europe/Paris',
    status: 'scheduled',
    next_run_at
  }).select().single();
  if (sErr) return NextResponse.json({ error: sErr.message }, { status: 400 });

  return NextResponse.json({ memory: mem, schedule: sch });
}
```

## `/api/deliver/run.ts` (Vercel Cron or manual trigger)
```ts
import { NextRequest, NextResponse } from 'next/server';
import { createClient } from '@supabase/supabase-js';

const supabase = createClient(process.env.SUPABASE_URL!, process.env.SUPABASE_SERVICE_ROLE_KEY!);

async function sendPush(user_id: string, title: string, body: string) {
  const { data: toks } = await supabase.from('push_tokens').select('*').eq('user_id', user_id).eq('enabled', true);
  if (!toks || toks.length === 0) return;
  await Promise.all(toks.map(t => fetch('https://fcm.googleapis.com/fcm/send', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Authorization': `key=${process.env.FCM_SERVER_KEY}` },
    body: JSON.stringify({ to: t.token, notification: { title, body } })
  })));
}

async function sendEmail(to: string, subject: string, html: string, ics?: string) {
  await fetch('https://api.resend.com/emails', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${process.env.RESEND_API_KEY}` },
    body: JSON.stringify({ from: 'Guarde.me <noreply@guarde.me>', to, subject, html, attachments: ics ? [{ filename: 'reminder.ics', content: Buffer.from(ics).toString('base64') }] : [] })
  });
}

function buildICS({ uid, dtstart, durationMinutes = 10, summary, description }: { uid: string, dtstart: string, durationMinutes?: number, summary: string, description?: string }) {
  const dt = dtstart.replace(/[-:]/g, '').replace('.000Z','Z');
  const dtend = new Date(new Date(dtstart).getTime() + durationMinutes*60000).toISOString().replace(/[-:]/g,'').replace('.000Z','Z');
  return [
    'BEGIN:VCALENDAR','VERSION:2.0','PRODID:-//Guarde.me//EN','BEGIN:VEVENT',
    `UID:${uid}`,
    `DTSTAMP:${dt}`,
    `DTSTART:${dt}`,
    `DTEND:${dtend}`,
    `SUMMARY:${summary}`,
    description ? `DESCRIPTION:${description}` : undefined,
    'END:VEVENT','END:VCALENDAR'
  ].filter(Boolean).join('
');
}

export async function POST(_req: NextRequest) {
  // Claim up to N pending deliveries
  const { data: claimed, error } = await supabase.rpc('app_claim_pending_deliveries', { max_rows: 50 });
  if (error) return NextResponse.json({ error: error.message }, { status: 500 });

  for (const d of claimed as any[]) {
    try {
      // Load memory
      const { data: sch } = await supabase.from('schedules').select('*, memories(*)').eq('id', d.schedule_id).single();
      const mem = sch?.memories;
      const title = 'Guarde.me — lembrete';
      const body = mem?.content_text?.slice(0,120) || 'Você tem uma memória para revisitar.';

      if (d.channel === 'push' || d.channel === 'in_app') {
        await sendPush(d.user_id, title, body);
      }
      if (d.channel === 'email') {
        // resolve email from profile or channel table
        const { data: prof } = await supabase.from('profiles').select('display_name').eq('user_id', d.user_id).single();
        const email = process.env.TEST_FALLBACK_EMAIL!; // replace with user email
        const ics = buildICS({ uid: d.id, dtstart: d.run_at, summary: title, description: body });
        await sendEmail(email, title, `<p>${body}</p>`, ics);
      }

      await supabase.from('deliveries').update({ status: 'succeeded', updated_at: new Date().toISOString() }).eq('id', d.id);
    } catch (e:any) {
      await supabase.from('deliveries').update({ status: 'failed', last_error: e?.message || 'unknown', updated_at: new Date().toISOString() }).eq('id', d.id);
    }
  }

  return NextResponse.json({ processed: claimed?.length || 0 });
}
```

> Set env vars: `SUPABASE_URL`, `SUPABASE_SERVICE_ROLE_KEY`, `GEMINI_API_KEY`, `FCM_SERVER_KEY`, `RESEND_API_KEY`, `TEST_FALLBACK_EMAIL`.

---

# ⏱️ `pg_cron` Job (enqueue every minute)

```sql
-- Enable pg_cron extension (Supabase: request via dashboard if not enabled)
create extension if not exists pg_cron;

-- Schedule enqueue to run every minute
select cron.schedule('guarde_me_enqueue', '* * * * *', $$select app_enqueue_due_deliveries();$$);
```

> Alternative: use **Vercel Cron** to call `POST /api/deliver/run` every minute and skip `pg_cron`.

---

