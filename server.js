/**
 * Blood Arena - Push Notification Server
 * Deploy on Render.com (Node.js)
 */

const express = require('express');
const webpush = require('web-push');

const app = express();
app.use(express.json());

// ── VAPID Keys (same as your vapid.php) ──────────────────────
const VAPID_PUBLIC  = 'BP_teCkbEQBIZ4uFwI0lO3CfzA5Nhco6qB52uwjRLBOCQd1KPbSc-zRaGoWKvR8FGH6GNFclJDPiR2rqptkLK9U';
const VAPID_PRIVATE = 'feIGaw3LoPdNfRD-32E9tf9W5mmDX9aUPrJ2JDLUAeE';
const VAPID_SUBJECT = 'mailto:admin@bloodarena.local';

// ── Secret token — InfinityFree theke call korte lagbe ───────
// Render Dashboard e Environment Variable hisebe SET KORUN:
// Key: API_SECRET   Value: (jekono strong password)
const API_SECRET = process.env.API_SECRET || 'bloodarena2024';

webpush.setVapidDetails(VAPID_SUBJECT, VAPID_PUBLIC, VAPID_PRIVATE);

// ── Health check ─────────────────────────────────────────────
app.get('/', function(req, res) {
    res.json({ status: 'ok', service: 'Blood Arena Push Server' });
});

// ── Send push to one subscription ────────────────────────────
// POST /send-push
// Body: { secret, endpoint, p256dh, auth, title, body, url }
app.post('/send-push', async function(req, res) {
    // Auth check
    if (req.body.secret !== API_SECRET) {
        return res.status(403).json({ ok: false, msg: 'Unauthorized' });
    }

    const { endpoint, p256dh, auth, title, body, url, type } = req.body;

    if (!endpoint || !p256dh || !auth) {
        return res.status(400).json({ ok: false, msg: 'Missing subscription data' });
    }

    const subscription = { endpoint, keys: { p256dh, auth } };
    const payload = JSON.stringify({
        title: title || 'Blood Arena',
        body:  body  || 'New notification',
        url:   url   || '/',
        type:  (type === 'emergency') ? 'emergency' : 'info'
    });

    try {
        await webpush.sendNotification(subscription, payload);
        res.json({ ok: true });
    } catch (err) {
        // 404/410 = expired subscription
        res.json({ ok: false, expired: (err.statusCode === 404 || err.statusCode === 410), msg: err.message });
    }
});

// ── Send push to multiple subscriptions ──────────────────────
// POST /send-push-bulk
// Body: { secret, subscriptions: [{endpoint, p256dh, auth}], title, body, url }
app.post('/send-push-bulk', async function(req, res) {
    if (req.body.secret !== API_SECRET) {
        return res.status(403).json({ ok: false, msg: 'Unauthorized' });
    }

    const { subscriptions, title, body, url, type } = req.body;

    if (!subscriptions || !Array.isArray(subscriptions) || subscriptions.length === 0) {
        return res.status(400).json({ ok: false, msg: 'No subscriptions provided' });
    }

    const payload = JSON.stringify({
        title: title || 'Blood Arena',
        body:  body  || 'New notification',
        url:   url   || '/',
        type:  (type === 'emergency') ? 'emergency' : 'info'
    });

    let sent = 0, failed = 0, expired = [];

    const results = await Promise.allSettled(
        subscriptions.map(async function(sub) {
            const subscription = {
                endpoint: sub.endpoint,
                keys: { p256dh: sub.p256dh, auth: sub.auth }
            };
            try {
                await webpush.sendNotification(subscription, payload);
                sent++;
            } catch (err) {
                failed++;
                if (err.statusCode === 404 || err.statusCode === 410) {
                    expired.push(sub.endpoint);
                }
            }
        })
    );

    res.json({ ok: true, sent, failed, expired_endpoints: expired });
});

// ── Start ─────────────────────────────────────────────────────
const PORT = process.env.PORT || 3000;
app.listen(PORT, function() {
    console.log('Blood Arena Push Server running on port ' + PORT);
});

