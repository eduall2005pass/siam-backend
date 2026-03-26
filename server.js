const express = require('express');
const cors = require('cors');
const axios = require('axios');

const app = express();
app.use(cors());
app.use(express.json({ limit: '20mb' }));

// ── API Keys (Render Environment Variables থেকে আসবে) ──
const GROQ_KEY = process.env.GROQ_API_KEY;
const DEEPSEEK_KEY = process.env.DEEPSEEK_API_KEY;
const CEREBRAS_KEY = process.env.CEREBRAS_API_KEY;
const TAVILY_KEY = process.env.TAVILY_API_KEY;
const WEATHER_KEY = process.env.OPENWEATHER_API_KEY;
const HF_KEY = process.env.HUGGINGFACE_API_KEY;

const SYSTEM_PROMPT = `তুমি Siam AI — একটি শক্তিশালী AI assistant। 
User যে ভাষায় কথা বলবে সেই ভাষায় উত্তর দাও।
বাংলায় কথা বললে বাংলায় উত্তর দাও।
সবসময় helpful, friendly এবং accurate থাকো।`;

// ═══════════════════════════════════════════
// GROQ — Quick Chat (Llama 3.3 70B)
// ═══════════════════════════════════════════
async function callGroq(messages) {
  const res = await axios.post(
    'https://api.groq.com/openai/v1/chat/completions',
    {
      model: 'llama-3.3-70b-versatile',
      messages: [
        { role: 'system', content: SYSTEM_PROMPT },
        ...messages
      ],
      max_tokens: 8192,
      temperature: 0.7
    },
    { headers: { Authorization: `Bearer ${GROQ_KEY}` } }
  );
  return { reply: res.data.choices[0].message.content, model: 'Groq Llama 3.3' };
}

// ═══════════════════════════════════════════
// GROQ VISION — Image Analysis
// ═══════════════════════════════════════════
async function callGroqVision(messages, imageBase64, mimeType = 'image/jpeg') {
  const lastMsg = messages[messages.length - 1].content;
  const res = await axios.post(
    'https://api.groq.com/openai/v1/chat/completions',
    {
      model: 'llama-3.2-11b-vision-preview',
      messages: [
        { role: 'system', content: SYSTEM_PROMPT },
        {
          role: 'user',
          content: [
            { type: 'image_url', image_url: { url: `data:${mimeType};base64,${imageBase64}` } },
            { type: 'text', text: lastMsg }
          ]
        }
      ],
      max_tokens: 4096
    },
    { headers: { Authorization: `Bearer ${GROQ_KEY}` } }
  );
  return { reply: res.data.choices[0].message.content, model: 'Groq Vision' };
}

// ═══════════════════════════════════════════
// DEEPSEEK — Complex Reasoning + Backup
// ═══════════════════════════════════════════
async function callDeepSeek(messages, reasoning = false) {
  const model = reasoning ? 'deepseek-reasoner' : 'deepseek-chat';
  const res = await axios.post(
    'https://api.deepseek.com/v1/chat/completions',
    {
      model,
      messages: [
        { role: 'system', content: SYSTEM_PROMPT },
        ...messages
      ],
      max_tokens: 8192
    },
    { headers: { Authorization: `Bearer ${DEEPSEEK_KEY}` } }
  );
  return { reply: res.data.choices[0].message.content, model: reasoning ? 'DeepSeek Reasoner' : 'DeepSeek Chat' };
}

// ═══════════════════════════════════════════
// CEREBRAS — Long Code Generation
// ═══════════════════════════════════════════
async function callCerebras(messages) {
  const res = await axios.post(
    'https://api.cerebras.ai/v1/chat/completions',
    {
      model: 'llama-3.3-70b',
      messages: [
        { role: 'system', content: SYSTEM_PROMPT },
        ...messages
      ],
      max_tokens: 8192,
      temperature: 0.2
    },
    { headers: { Authorization: `Bearer ${CEREBRAS_KEY}` } }
  );
  return { reply: res.data.choices[0].message.content, model: 'Cerebras Llama 3.3' };
}

// ═══════════════════════════════════════════
// AUTO ROUTE — কোন AI কে কখন ডাকবে
// ═══════════════════════════════════════════
function detectMode(message) {
  const msg = message.toLowerCase();
  const codeKeywords = ['code', 'কোড', 'program', 'function', 'class', 'script', 'লেখো', 'বানাও', 'fix', 'debug', 'error'];
  const reasonKeywords = ['কেন', 'why', 'analyze', 'explain', 'বিশ্লেষণ', 'reason', 'logic', 'think'];
  const longCodeKeywords = ['1000 line', '2000 line', '5000 line', '10000 line', '20000 line', 'full project', 'complete app', 'পুরো app'];

  if (longCodeKeywords.some(k => msg.includes(k))) return 'cerebras';
  if (codeKeywords.some(k => msg.includes(k))) return 'deepseek';
  if (reasonKeywords.some(k => msg.includes(k))) return 'deepseek-reason';
  return 'groq';
}

// ═══════════════════════════════════════════
// /chat — Main Chat Endpoint
// ═══════════════════════════════════════════
app.post('/chat', async (req, res) => {
  const { messages, imageBase64, mimeType, mode } = req.body;
  if (!messages || !Array.isArray(messages)) {
    return res.status(400).json({ error: 'messages array required' });
  }

  const lastMsg = messages[messages.length - 1].content;
  const autoMode = mode || detectMode(lastMsg);

  try {
    // Image আসলে Vision use করো
    if (imageBase64) {
      const result = await callGroqVision(messages, imageBase64, mimeType);
      return res.json(result);
    }

    // Auto routing
    if (autoMode === 'cerebras') {
      try {
        const result = await callCerebras(messages);
        return res.json(result);
      } catch (e) {
        console.error('Cerebras failed:', e.message);
      }
    }

    if (autoMode === 'deepseek' || autoMode === 'deepseek-reason') {
      try {
        const result = await callDeepSeek(messages, autoMode === 'deepseek-reason');
        return res.json(result);
      } catch (e) {
        console.error('DeepSeek failed:', e.message);
      }
    }

    // Default: Groq
    try {
      const result = await callGroq(messages);
      return res.json(result);
    } catch (e) {
      console.error('Groq failed:', e.message);
      // Final fallback: DeepSeek
      const result = await callDeepSeek(messages);
      return res.json(result);
    }

  } catch (e) {
    return res.status(500).json({ error: 'সব AI fail করেছে', detail: e.message });
  }
});

// ═══════════════════════════════════════════
// /search — Web Search (Tavily)
// ═══════════════════════════════════════════
app.post('/search', async (req, res) => {
  const { query } = req.body;
  try {
    const r = await axios.post('https://api.tavily.com/search', {
      api_key: TAVILY_KEY,
      query,
      max_results: 5,
      include_answer: true,
      include_images: false
    });
    res.json({
      answer: r.data.answer,
      results: r.data.results.map(x => ({
        title: x.title,
        url: x.url,
        content: x.content.slice(0, 400)
      }))
    });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ═══════════════════════════════════════════
// /weather — Weather Info
// ═══════════════════════════════════════════
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
      temp: Math.round(d.main.temp),
      feels_like: Math.round(d.main.feels_like),
      humidity: d.main.humidity,
      description: d.weather[0].description,
      icon: `https://openweathermap.org/img/wn/${d.weather[0].icon}@2x.png`,
      wind: d.wind.speed,
      min: Math.round(d.main.temp_min),
      max: Math.round(d.main.temp_max)
    });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ═══════════════════════════════════════════
// /image — Image Generation (Pollinations - Unlimited Free)
// ═══════════════════════════════════════════
app.post('/image', async (req, res) => {
  const { prompt, width = 1024, height = 1024, style = 'realistic' } = req.body;
  try {
    const encodedPrompt = encodeURIComponent(prompt);
    const seed = Math.floor(Math.random() * 999999);
    const imageUrl = `https://image.pollinations.ai/prompt/${encodedPrompt}?width=${width}&height=${height}&seed=${seed}&nologo=true`;
    res.json({ imageUrl, prompt, model: 'Pollinations FLUX' });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ═══════════════════════════════════════════
// /image-hf — Hugging Face Image (Logo/Art)
// ═══════════════════════════════════════════
app.post('/image-hf', async (req, res) => {
  const { prompt, model = 'black-forest-labs/FLUX.1-schnell' } = req.body;
  try {
    const r = await axios.post(
      `https://api-inference.huggingface.co/models/${model}`,
      { inputs: prompt },
      {
        headers: { Authorization: `Bearer ${HF_KEY}` },
        responseType: 'arraybuffer'
      }
    );
    const base64 = Buffer.from(r.data).toString('base64');
    res.json({ imageBase64: base64, model: 'HuggingFace FLUX' });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ═══════════════════════════════════════════
// /translate — Translation
// ═══════════════════════════════════════════
app.post('/translate', async (req, res) => {
  const { text, to = 'Bengali' } = req.body;
  try {
    const messages = [{ role: 'user', content: `Translate to ${to}: "${text}". Reply ONLY with translation.` }];
    const result = await callGroq(messages);
    res.json({ translation: result.reply });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

// ═══════════════════════════════════════════
// / — Health Check
// ═══════════════════════════════════════════
app.get('/', (req, res) => {
  res.json({
    status: '✅ Running',
    app: 'Siam 2.5.8',
    features: ['chat', 'vision', 'search', 'weather', 'image', 'translate'],
    models: ['Groq Llama 3.3', 'Groq Vision', 'DeepSeek', 'Cerebras', 'Pollinations']
  });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Siam 2.5.8 Backend running on port ${PORT}`);
});
