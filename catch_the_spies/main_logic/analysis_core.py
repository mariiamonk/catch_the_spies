import pandas as pd
import numpy as np

def _make_pid(r):
    return r['first_name'] + '|' + r['last_name'] + '|' + r['pax_birth_data']

def _cluster_stats(part):
    part = part.sort_values('flight_date')
    dates = part['flight_date']
    dif = dates.diff().dt.days.fillna(0)
    grp = []
    cur = []
    for i, v in enumerate(dif):
        if v <= 2:
            cur.append(i)
        else:
            if len(cur) > 1:
                grp.append(cur)
            cur = [i]
    if len(cur) > 1:
        grp.append(cur)
    if len(part) == 0:
        cluster = 0
    else:
        s = 0.0
        for g in grp:
            s += len(g) ** 1.3
        cluster = s / len(part)
    sudden = 0.0
    p = part.copy()
    p['week'] = p['flight_date'].dt.isocalendar().week
    p['year'] = p['flight_date'].dt.year
    w = p.groupby(['year', 'week']).size().reset_index(name='cnt')
    w = w.sort_values(['year', 'week'])['cnt'].tolist()
    if len(w) >= 4:
        for i in range(2, len(w)):
            prev = np.median(w[i-2:i])
            if prev > 0:
                r = w[i] / prev
                if r > sudden:
                    sudden = r
    logi = 0
    if 'departure' in part.columns and 'arrival' in part.columns:
        day = part.groupby(part['flight_date'].dt.date).agg({'departure': list, 'arrival': list})
        for _, rr in day.iterrows():
            ds = set(rr['departure'])
            as_ = set(rr['arrival'])
            miss = ds - as_
            if len(miss) > 0:
                logi += len(miss)
    if len(part) > 0:
        logi = logi / len(part)
    else:
        logi = 0
    if len(dates) > 0:
        total = (dates.max() - dates.min()).days + 1
        peak = len(part) / total if total > 0 else 0
    else:
        peak = 0
    if len(part) > 30:
        avg = len(part) / 30
    else:
        rng = ((dates.max() - dates.min()).days + 1) if len(dates) > 0 else 1
        avg = len(part) / rng
    return cluster, sudden, logi, peak, avg

def analyze_patterns(df):
    if 'flight_date' in df.columns:
        df['flight_date'] = pd.to_datetime(df['flight_date'], errors='coerce')
    else:
        df['flight_date'] = pd.NaT
    df = df.dropna(subset=['flight_date'])
    if 'passenger_id' not in df.columns:
        df['passenger_id'] = df.apply(_make_pid, axis=1)
    out = []
    for pid, sub in df.groupby('passenger_id'):
        cl, su, lo, pk, av = _cluster_stats(sub)
        out.append({
            'passenger_id': pid,
            'activity_cluster_score': cl,
            'sudden_activity_increase': su,
            'logistic_inconsistency': lo,
            'peak_activity_period': pk,
            'avg_flights_per_period': av
        })
    if len(out) == 0:
        return pd.DataFrame(columns=['passenger_id','activity_cluster_score','sudden_activity_increase','logistic_inconsistency','peak_activity_period','avg_flights_per_period'])
    return pd.DataFrame(out)

def main_analysis(path):
    df = pd.read_csv(path, low_memory=False)
    for c in ['document_norm','first_name','last_name','pax_birth_data','departure','arrival','agent_info']:
        if c in df.columns:
            df[c] = df[c].astype(str).replace('nan','').replace('None','')
    mask = (df.get('first_name','') != '') & (df.get('last_name','') != '') & (df.get('pax_birth_data','') != '') & (df.get('document_norm','') != '')
    base = df[mask].copy()
    if base.shape[0] == 0:
        return base, pd.DataFrame()
    dstat = base.groupby('document_norm').agg(u=('first_name','nunique'),cnt=('flight_code','count')).reset_index()
    sdocs = set(dstat[dstat['u'] > 1]['document_norm'].tolist())
    base['flight_date'] = pd.to_datetime(base['flight_date'], errors='coerce')
    base = base.dropna(subset=['flight_date'])
    base['passenger_id'] = base.apply(_make_pid, axis=1)
    patt = analyze_patterns(base)
    agg = base.groupby('passenger_id').agg({
        'flight_code': 'count',
        'document_norm': 'nunique',
        'agent_info': 'nunique',
        'flight_date': ['min','max'],
        'departure': lambda x: list(x.unique()) if 'departure' in base.columns else [],
        'arrival': lambda x: list(x.unique()) if 'arrival' in base.columns else []
    }).reset_index()
    cols = ['passenger_id','n_flights_total','n_unique_documents','n_unique_agents','first_flight','last_flight','departure_airports','arrival_airports']
    agg.columns = cols
    merged = agg.merge(patt, on='passenger_id', how='left')
    fn = merged['passenger_id'].str.split('|', expand=True)
    merged['first_name'] = fn[0]
    merged['last_name'] = fn[1]
    merged['pax_birth_data'] = fn[2]
    merged['days_active'] = (pd.to_datetime(merged['last_flight']) - pd.to_datetime(merged['first_flight'])).dt.days.clip(lower=1)
    merged['avg_activity_frequency'] = merged['avg_flights_per_period']
    merged['n_unique_departures'] = merged['departure_airports'].apply(lambda x: len(x) if isinstance(x, list) else 0)
    merged['n_unique_arrivals'] = merged['arrival_airports'].apply(lambda x: len(x) if isinstance(x, list) else 0)
    merged['total_unique_airports'] = merged['n_unique_departures'] + merged['n_unique_arrivals']
    docs = base.groupby(['first_name','last_name','pax_birth_data'])['document_norm'].apply(list).reset_index()
    docs['suspicious_documents'] = docs['document_norm'].apply(lambda arr: [i for i in arr if i in sdocs])
    docs['suspicious_docs_count'] = docs['suspicious_documents'].apply(len)
    merged = merged.merge(docs[['first_name','last_name','pax_birth_data','suspicious_documents','suspicious_docs_count']], on=['first_name','last_name','pax_birth_data'], how='left')
    merged['has_suspicious_doc'] = merged['suspicious_docs_count'].fillna(0).astype(int) > 0
    def _score(r):
        score = 0
        if r['has_suspicious_doc']:
            score += 140 + int(r.get('suspicious_docs_count',0)) * 18
        ac = r.get('activity_cluster_score', 0) or 0
        if ac > 2.5:
            score += 70
        elif ac > 1.2:
            score += 35
        si = r.get('sudden_activity_increase', 0) or 0
        if si > 12:
            score += 90
        elif si > 6:
            score += 60
        elif si > 3:
            score += 35
        li = r.get('logistic_inconsistency', 0) or 0
        if li > 0.25:
            score += 70
        elif li > 0.08:
            score += 30
        pk = r.get('peak_activity_period', 0) or 0
        if pk > 2:
            score += 40
        elif pk > 1:
            score += 20
        na = int(r.get('n_unique_agents',0) or 0)
        if na >= 9:
            score += 90
        elif na >= 6:
            score += 60
        elif na >= 4:
            score += 30
        nd = int(r.get('n_unique_documents',0) or 0)
        if nd > 3:
            score += 45
        elif nd > 1:
            score += 20
        ta = int(r.get('total_unique_airports',0) or 0)
        if ta > 9:
            score += 35
        elif ta > 4:
            score += 15
        return int(score)
    merged['risk_score'] = merged.apply(_score, axis=1)
    def _cat(s):
        if s >= 200: return 'КРИТИЧЕСКИЙ'
        if s >= 110: return 'ВЫСОКИЙ'
        if s >= 55: return 'СРЕДНИЙ'
        if s >= 25: return 'НИЗКИЙ'
        return 'НОРМА'
    merged['risk_category'] = merged['risk_score'].apply(_cat)
    merged['is_suspicious'] = merged['risk_score'] >= 50
    return base, merged
