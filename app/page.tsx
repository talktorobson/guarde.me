export default function Home() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-8">
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            🎙️ Guarde.me
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            Voice-first Memory Assistant - MVP Backend
          </p>
          <p className="text-lg text-gray-500">
            Diga &quot;Guarde me&quot; e capture memórias para entregar no momento certo
          </p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3">🧠 Intent Parsing</h3>
            <p className="text-gray-600 mb-4">
              Natural language understanding with Gemini AI
            </p>
            <code className="text-sm bg-gray-100 p-2 rounded block">
              POST /api/intent/decode
            </code>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3">📅 Schedule Creation</h3>
            <p className="text-gray-600 mb-4">
              Create memories with RRULE recurrence support
            </p>
            <code className="text-sm bg-gray-100 p-2 rounded block">
              POST /api/schedule/create
            </code>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-3">🚀 Delivery System</h3>
            <p className="text-gray-600 mb-4">
              FCM push notifications and email with ICS
            </p>
            <code className="text-sm bg-gray-100 p-2 rounded block">
              POST /api/deliver/run
            </code>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-8 mb-8">
          <h2 className="text-2xl font-semibold text-gray-900 mb-6">📊 System Status</h2>
          
          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">✅ Implemented</h3>
              <ul className="space-y-2 text-gray-600">
                <li>• Supabase database schema with RLS</li>
                <li>• Next.js 14 App Router backend</li>
                <li>• Gemini AI intent parsing</li>
                <li>• RRULE scheduling support</li>
                <li>• TypeScript types generation</li>
                <li>• Database migrations & functions</li>
                <li>• API endpoints structure</li>
              </ul>
            </div>
            
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">🔄 Next Steps</h3>
              <ul className="space-y-2 text-gray-600">
                <li>• Configure external API keys</li>
                <li>• Set up pg_cron in Supabase</li>
                <li>• Android app development</li>
                <li>• Wake word service</li>
                <li>• Voice recording integration</li>
                <li>• Production testing</li>
                <li>• Deploy to Vercel</li>
              </ul>
            </div>
          </div>
        </div>

        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-yellow-800 mb-3">⚙️ Configuration Required</h3>
          <p className="text-yellow-700 mb-4">
            To complete the setup, add these environment variables to your .env.local:
          </p>
          <ul className="space-y-1 text-yellow-700 font-mono text-sm">
            <li>• GEMINI_API_KEY (Google AI Studio)</li>
            <li>• FCM_SERVER_KEY (Firebase)</li>
            <li>• RESEND_API_KEY (Email service)</li>
            <li>• TEST_FALLBACK_EMAIL (Testing)</li>
          </ul>
        </div>
      </div>
    </main>
  )
}