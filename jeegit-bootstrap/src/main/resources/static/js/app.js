// jeegit console — Material 3 styled SPA.
// No bundler: ES module loaded directly by the browser.

const SUPPORTED_LOCALES = ['en', 'zh-CN', 'zh-TW', 'ja', 'ko', 'es', 'fr', 'de', 'pt-BR', 'ru', 'it', 'ar'];
const RTL_LOCALES = new Set(['ar']);
const BASIC_AUTH = 'Basic ' + btoa('admin:admin');

const state = {
  locale: 'en',
  messages: {},
  platformLocales: [],
};

// ---------- i18n ----------

function detectInitialLocale() {
  const url = new URL(window.location.href);
  const queryLang = url.searchParams.get('lang');
  if (queryLang && SUPPORTED_LOCALES.includes(queryLang)) return queryLang;
  const stored = window.localStorage.getItem('jeegit_locale');
  if (stored && SUPPORTED_LOCALES.includes(stored)) return stored;
  for (const pref of navigator.languages || [navigator.language || 'en']) {
    if (!pref) continue;
    if (SUPPORTED_LOCALES.includes(pref)) return pref;
    const lang = pref.split('-')[0];
    const fallback = SUPPORTED_LOCALES.find(l => l.split('-')[0] === lang);
    if (fallback) return fallback;
  }
  return 'en';
}

async function loadMessages(locale) {
  try {
    const response = await fetch(`/i18n/${locale}.json`, { cache: 'no-cache' });
    if (!response.ok) throw new Error('i18n HTTP ' + response.status);
    state.messages = await response.json();
  } catch (err) {
    console.warn('i18n load failed for', locale, err);
    state.messages = {};
  }
}

function t(key, fallback) {
  return state.messages[key] || fallback || key;
}

function applyTranslations(root = document) {
  root.querySelectorAll('[data-i18n]').forEach(el => {
    const key = el.getAttribute('data-i18n');
    el.textContent = t(key, el.textContent);
  });
  document.documentElement.lang = state.locale;
  document.documentElement.dir = RTL_LOCALES.has(state.locale) ? 'rtl' : 'ltr';
}

function setLocale(locale) {
  state.locale = locale;
  window.localStorage.setItem('jeegit_locale', locale);
  document.cookie = `jeegit_locale=${locale};path=/;max-age=31536000`;
  loadMessages(locale).then(() => {
    applyTranslations();
    render(currentRoute());
  });
}

function populateLocalePicker() {
  const picker = document.getElementById('locale-picker');
  picker.innerHTML = '';
  for (const locale of SUPPORTED_LOCALES) {
    const displayName = new Intl.DisplayNames([locale], { type: 'language' }).of(locale);
    const option = document.createElement('option');
    option.value = locale;
    option.textContent = `${locale} · ${displayName}`;
    if (locale === state.locale) option.selected = true;
    picker.appendChild(option);
  }
  picker.addEventListener('change', e => setLocale(e.target.value));
}

// ---------- API client ----------

async function apiGet(path) {
  const res = await fetch(path, {
    headers: { 'Accept-Language': state.locale },
  });
  if (!res.ok) throw new Error(`${path}: HTTP ${res.status}`);
  return res.json();
}

async function apiPost(path, body) {
  const res = await fetch(path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept-Language': state.locale,
      'Authorization': BASIC_AUTH,
    },
    body: body == null ? null : JSON.stringify(body),
  });
  const payload = await res.json().catch(() => ({}));
  if (!res.ok) {
    throw new Error(payload.message || `${path}: HTTP ${res.status}`);
  }
  return payload;
}

// ---------- Toast ----------

function toast(message, type = 'info') {
  const host = document.getElementById('toast-host');
  const node = document.createElement('div');
  node.className = `toast toast--${type}`;
  node.textContent = message;
  host.appendChild(node);
  setTimeout(() => {
    node.style.transition = 'opacity 200ms';
    node.style.opacity = '0';
    setTimeout(() => node.remove(), 220);
  }, 2800);
}

// ---------- Routing ----------

function currentRoute() {
  const hash = window.location.hash.replace('#/', '').trim();
  if (!hash) return 'dashboard';
  return hash;
}

function updateActiveNav(route) {
  document.querySelectorAll('.nav-link').forEach(a => {
    a.classList.toggle('is-active', a.dataset.route === route);
  });
}

window.addEventListener('hashchange', () => render(currentRoute()));

// ---------- Views ----------

const views = {
  dashboard: renderDashboard,
  matters: renderMatters,
  audit: renderAudit,
  orgs: renderOrgs,
};

function render(route) {
  updateActiveNav(route);
  const root = document.getElementById('app-root');
  const view = views[route] || renderNotFound;
  view(root);
}

async function renderDashboard(root) {
  root.innerHTML = `<h2 class="sr-only">${t('dashboard.heading')}</h2><div class="loading">${t('app.loading')}</div>`;
  try {
    const [info, locales] = await Promise.all([
      apiGet('/api/v1/platform/info'),
      apiGet('/api/v1/platform/locales'),
    ]);
    state.platformLocales = locales.data || [];
    document.getElementById('footer-version').textContent = info.data.version;

    const modules = (info.data.modules || []).map(m => `<span class="chip">${m}</span>`).join(' ');
    const charters = (info.data.charters || [])
      .map(path => `<li><a href="https://github.com/smallyaohailu/jeegit/blob/master/${path}" target="_blank" rel="noopener">${path}</a></li>`)
      .join('');

    const localeCards = state.platformLocales.map(l => `
      <div class="chip ${l.rtl ? 'chip--primary' : 'chip--outline'}" title="${l.displayNameEnglish}">
        ${l.tag} · ${l.displayNameNative}${l.rtl ? ' · ' + t('dashboard.locales.rtl') : ''}
      </div>
    `).join('');

    root.innerHTML = `
      <section class="grid grid--2">
        <article class="card">
          <h2 class="card__title">
            <span class="material-symbols-outlined" aria-hidden="true">monitoring</span>
            ${t('dashboard.metrics')}
          </h2>
          <dl class="kv-list">
            <dt>${t('dashboard.version')}</dt><dd><code>${info.data.version}</code></dd>
            <dt>${t('dashboard.license')}</dt><dd><span class="chip chip--primary">${info.data.license}</span></dd>
            <dt>${t('dashboard.tagline')}</dt><dd>${info.data.tagline}</dd>
            <dt>${t('dashboard.architecture')}</dt><dd>${info.data.architecture}</dd>
          </dl>
        </article>
        <article class="card">
          <h2 class="card__title">
            <span class="material-symbols-outlined" aria-hidden="true">translate</span>
            ${t('dashboard.locales')}
          </h2>
          <div class="chip-row" style="display:flex;flex-wrap:wrap;gap:6px;">${localeCards}</div>
        </article>
      </section>
      <section class="card">
        <h2 class="card__title">
          <span class="material-symbols-outlined" aria-hidden="true">widgets</span>
          ${t('dashboard.modules')}
        </h2>
        <div class="chip-row" style="display:flex;flex-wrap:wrap;gap:6px;">${modules}</div>
      </section>
      <section class="card">
        <h2 class="card__title">
          <span class="material-symbols-outlined" aria-hidden="true">description</span>
          ${t('dashboard.charters')}
        </h2>
        <ul>${charters}</ul>
      </section>
    `;
  } catch (err) {
    root.innerHTML = `<div class="card"><p>${err.message}</p></div>`;
  }
}

async function renderMatters(root) {
  root.innerHTML = `
    <section class="card">
      <h2 class="card__title">
        <span class="material-symbols-outlined" aria-hidden="true">edit_note</span>
        ${t('matters.create')}
      </h2>
      <p style="font-size:12px;color:var(--md-sys-color-on-surface-variant)">${t('matters.auth')}</p>
      <form id="create-matter">
        <div class="form-row">
          <label for="m-title">${t('matters.title')}</label>
          <input id="m-title" name="title" required>
        </div>
        <div class="form-row">
          <label for="m-category">${t('matters.category')}</label>
          <input id="m-category" name="category" placeholder="tax · social · complaint · business · civil" required>
        </div>
        <div class="form-row">
          <label for="m-desc">${t('matters.description')}</label>
          <textarea id="m-desc" name="description"></textarea>
        </div>
        <div class="form-row">
          <label for="m-applicant">${t('matters.applicant')}</label>
          <input id="m-applicant" name="applicantId" value="console-user">
        </div>
        <div class="btn-row">
          <button class="btn btn--filled" type="submit">
            <span class="material-symbols-outlined" aria-hidden="true">send</span>
            ${t('matters.submit')}
          </button>
        </div>
      </form>
    </section>
    <section class="card" id="matters-list">
      <h2 class="card__title">
        <span class="material-symbols-outlined" aria-hidden="true">inbox</span>
        ${t('matters.heading')}
      </h2>
      <div class="loading">${t('app.loading')}</div>
    </section>
  `;

  document.getElementById('create-matter').addEventListener('submit', async e => {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(e.target).entries());
    try {
      const response = await apiPost('/api/v1/matters', data);
      toast(t('matters.submitted') + ' ' + response.data.id);
      e.target.reset();
      refreshMattersList();
    } catch (err) {
      toast(err.message, 'error');
    }
  });

  refreshMattersList();
}

async function refreshMattersList() {
  const list = document.getElementById('matters-list');
  try {
    const response = await apiGet('/api/v1/matters');
    const matters = response.data || [];
    if (matters.length === 0) {
      list.innerHTML = `<h2 class="card__title">${t('matters.heading')}</h2><p>${t('matters.empty')}</p>`;
      return;
    }
    const rows = matters.map(m => `
      <tr>
        <td><code>${m.id.slice(0, 8)}…</code></td>
        <td>${escapeHtml(m.title)}</td>
        <td><span class="chip chip--outline">${m.category || ''}</span></td>
        <td><span class="chip ${m.matterStatus === 'DISPATCHED' ? 'chip--primary' : ''}">${m.matterStatus}</span></td>
        <td>${escapeHtml(m.assignedDepartment || '—')}</td>
        <td>
          <button class="btn btn--tonal" data-dispatch="${m.id}">
            <span class="material-symbols-outlined" aria-hidden="true">smart_toy</span>
            ${t('matters.dispatch')}
          </button>
        </td>
      </tr>
    `).join('');
    list.innerHTML = `
      <h2 class="card__title">
        <span class="material-symbols-outlined" aria-hidden="true">inbox</span>
        ${t('matters.heading')}
      </h2>
      <table class="table">
        <thead>
          <tr>
            <th>${t('matters.id')}</th>
            <th>${t('matters.title')}</th>
            <th>${t('matters.category')}</th>
            <th>${t('matters.status')}</th>
            <th>${t('matters.assigned')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody>${rows}</tbody>
      </table>
    `;
    list.querySelectorAll('button[data-dispatch]').forEach(btn => {
      btn.addEventListener('click', async () => {
        const id = btn.dataset.dispatch;
        btn.disabled = true;
        try {
          const response = await apiPost(`/api/v1/matters/${id}/dispatch`, null);
          toast(t('matters.dispatched') + ' → ' + response.data.decision.department);
          refreshMattersList();
        } catch (err) {
          toast(err.message, 'error');
        } finally {
          btn.disabled = false;
        }
      });
    });
  } catch (err) {
    list.innerHTML = `<p>${err.message}</p>`;
  }
}

async function renderAudit(root) {
  root.innerHTML = `<div class="card"><h2 class="card__title">
    <span class="material-symbols-outlined" aria-hidden="true">fact_check</span>
    ${t('audit.heading')}
  </h2><div class="loading">${t('app.loading')}</div></div>`;
  try {
    const response = await apiGet('/api/v1/audit/tenants/default');
    const entries = response.data || [];
    if (entries.length === 0) {
      root.innerHTML = `<div class="card"><p>${t('audit.empty')}</p></div>`;
      return;
    }
    const rows = entries.map(e => `
      <tr>
        <td>${new Date(e.occurredAt).toLocaleString(state.locale)}</td>
        <td><span class="chip chip--outline">${e.action}</span></td>
        <td>${escapeHtml(e.actorId || '—')}</td>
        <td><span class="chip ${riskChipClass(e.riskLevel)}">${e.riskLevel || '—'}</span></td>
        <td>${escapeHtml(e.decision || '')}</td>
        <td>${escapeHtml((e.reasoningSummary || '').slice(0, 180))}${(e.reasoningSummary || '').length > 180 ? '…' : ''}</td>
      </tr>
    `).join('');
    root.innerHTML = `
      <div class="card">
        <h2 class="card__title">
          <span class="material-symbols-outlined" aria-hidden="true">fact_check</span>
          ${t('audit.heading')}
        </h2>
        <table class="table">
          <thead>
            <tr>
              <th>${t('audit.occurredAt')}</th>
              <th>${t('audit.action')}</th>
              <th>${t('audit.actor')}</th>
              <th>${t('audit.risk')}</th>
              <th>${t('audit.decision')}</th>
              <th>${t('audit.reasoning')}</th>
            </tr>
          </thead>
          <tbody>${rows}</tbody>
        </table>
      </div>
    `;
  } catch (err) {
    root.innerHTML = `<div class="card"><p>${err.message}</p></div>`;
  }
}

function riskChipClass(risk) {
  if (risk === 'HIGH') return 'chip--error';
  if (risk === 'MEDIUM') return 'chip--primary';
  return 'chip--outline';
}

async function renderOrgs(root) {
  root.innerHTML = `<div class="card"><h2 class="card__title">
    <span class="material-symbols-outlined" aria-hidden="true">account_tree</span>
    ${t('orgs.heading')}
  </h2><div class="loading">${t('app.loading')}</div></div>`;
  try {
    const response = await apiGet('/api/v1/orgs');
    const orgs = response.data || [];
    if (orgs.length === 0) {
      root.innerHTML = `<div class="card"><p>${t('orgs.empty')}</p></div>`;
      return;
    }
    const rows = orgs.map(o => `
      <tr>
        <td style="padding-left:${o.treeLevel * 16}px"><code>${o.code}</code></td>
        <td>${escapeHtml(o.name)}</td>
        <td><span class="chip chip--outline">${o.type}</span></td>
        <td>${o.treeLevel}</td>
      </tr>
    `).join('');
    root.innerHTML = `
      <div class="card">
        <h2 class="card__title">
          <span class="material-symbols-outlined" aria-hidden="true">account_tree</span>
          ${t('orgs.heading')}
        </h2>
        <table class="table">
          <thead>
            <tr>
              <th>${t('orgs.code')}</th>
              <th>${t('orgs.name')}</th>
              <th>${t('orgs.type')}</th>
              <th>${t('orgs.level')}</th>
            </tr>
          </thead>
          <tbody>${rows}</tbody>
        </table>
      </div>
    `;
  } catch (err) {
    root.innerHTML = `<div class="card"><p>${err.message}</p></div>`;
  }
}

function renderNotFound(root) {
  root.innerHTML = `<div class="card"><p>Page not found</p></div>`;
}

function escapeHtml(s) {
  if (s == null) return '';
  return String(s).replace(/[&<>"']/g, ch => ({'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'}[ch]));
}

// ---------- bootstrap ----------

(async function main() {
  state.locale = detectInitialLocale();
  await loadMessages(state.locale);
  applyTranslations();
  populateLocalePicker();
  render(currentRoute());
})();
