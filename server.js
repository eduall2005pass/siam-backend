const express = require('express');
const cors = require('cors');
const axios = require('axios');

const app = express();
app.use(cors());
app.use(express.json({ limit: '10mb' }));

const GEMINI_KEY = process.env.GEMINI_API_KEY;
const GROQ_KEY = process.env.GROQ_API_KEY;
const DEEPSEEK_KEY = process.env.DEEPSEEK_API_KEY;
const TAVILY_KEY = process.env.TAVILY_API_KEY;
const WEATHER_KEY = process.env.OPENWEATHER_API_KEY;

// ─── Gemini 2.5 Flash ───────────────────────────────────────────
async function callGemini(messages, imageBase64 = null) {
  const lastMsg = messages[messages.length - 1].content;
  const history = messages.slice(0, -1).map(m => ({
    role: m.role === 'assistant' ? 'model' : 'user',
    parts: [{ text: m.content }]
  }));

  const parts = [];
  if (imageBase64) {
    parts.push({ inlineData: { mimeType: 'image/jpeg', data: imageBase64 } });
  }
  parts.push({ text: lastMsg });

  const res = await axios.post(
    `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-04-17:generateContent?key=${GEMINI_KEY}`,
    {
      systemInstruction: {
        parts: [{ text: 'তুমি Siam AI। তুমি বাংলায় কথা বলো। User যে ভাষায় কথা বলবে সেই ভাষায় উত্তর দাও।' }]
      },
      contents: [...history, { role: 'user', parts }]
    }
  );
  return res.data.candidates[0].content.parts[0].text;
}

// ─── Groq Llama 3.3 (Fallback 1) ────────────────────────────────
async function callGroq(messages) {
  const res = await axios.post(
    'https://api.groq.com/openai/v1/chat/completions',
    {
      model: 'llama-3.3-70b-versatile',
      messages: [
        { role: 'system', content: 'তুমি Siam AI। User যে ভাষায় লিখবে সেই ভাষায় উত্তর দাও।' },
        ...messages
      ],
      max_tokens: 2048
    },
    { headers: { Authorization: `Bearer ${GROQ_KEY}` } }
  );
  return res.data.choices[0].message.content;
}

// ─── DeepSeek (Fallback 2) ───────────────────────────────────────
async function callDeepSeek(messages) {
  const res = await axios.post(
    'https://api.deepseek.com/v1/chat/completions',
    {
      model: 'deepseek-chat',
      messages: [
        { role: 'system', content: 'তুমি Siam AI। User যে ভাষায় লিখবে সেই ভাষায় উত্তর দাও।' },
        ...messages
      ]
    },
    { headers: { Authorization: `Bearer ${DEEPSEEK_KEY}` } }
  );
  return res.data.choices[0].message.content;
}

// ─── /chat ───────────────────────────────────────────────────────
app.post('/chat', async (req, res) => {
  const { messages, imageBase64 } = req.body;
  if (!messages || !Array.isArray(messages)) {
    return res.status(400).json({ error: 'messages array required' });
  }

  try {
    const reply = await callGemini(messages, imageBase64);
    return res.json({ reply, model: 'Gemini 2.5 Flash' });
  } catch (e1) {
    console.error('Gemini error:', e1.message);
    try {
      const reply = await callGroq(messages);
      return res.json({ reply, model: 'Groq Llama 3.3' });
    } catch (e2) {
      console.error('Groq error:', e2.message);
      try {
        const reply = await callDeepSeek(messages);
        return res.json({ reply, model: 'DeepSeek' });
      } catch (e3) {
        return res.status(500).json({ error: 'সব AI fail করেছে', detail: e3.message });
      }
    }
  }
});

// ─── /search ─────────────────────────────────────────────────────
app.post('/search', async (req, res) => {
  const { query } = req.body;
  try {
    const r = await axios.post('https://api.tavily.com/search', {
      api_key: TAVILY_KEY,
      query,
      max_results: 5,
      include_answer: true
    });
    res.json({
      answer: r.data.answer,
      results: r.data.results.map(x => ({
        title: x.title,
        url: x.url,
        content: x.content.slice(0, 300)
      }))
    });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ─── /weather ────────────────────────────────────────────────────
app.get('/weather', async (req, res) => {
  const city = req.query.city || 'Dhaka';
  try {
    const r = await axios.get(
      `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${WEATHER_KEY}&units=metric&lang=bn`
    );
    const d = r.data;
    res.json({
      city: d.name,
      country: d.sys.country,
      temp: d.main.temp,
      feels_like: d.main.feels_like,
      humidity: d.main.humidity,
      description: d.weather[0].description,
      icon: d.weather[0].icon,
      wind: d.wind.speed
    });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ─── /translate ──────────────────────────────────────────────────
app.post('/translate', async (req, res) => {
  const { text, to } = req.body;
  try {
    const messages = [{ role: 'user', content: `Translate this to ${to || 'Bengali'}: "${text}". Reply ONLY with the translation.` }];
    const reply = await callGemini(messages);
    res.json({ translation: reply });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ─── /health ─────────────────────────────────────────────────────
app.get('/', (req, res) => {
  res.json({
    status: 'running',
    app: 'Siam 2.5.8',
    message: 'Backend চলছে! ✅'
  });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Siam 2.5.8 Backend running on port ${PORT} ✅`);
});
