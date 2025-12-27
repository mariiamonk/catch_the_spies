import pandas as pd
import numpy as np
from sklearn.ensemble import IsolationForest
from sklearn.preprocessing import StandardScaler

path = r"D:\projects\python\AI2\data_staging\merged_all_detailed.csv"
df = pd.read_csv(path, low_memory=False)

df["flight_date"] = pd.to_datetime(df["flight_date"], errors="coerce")

keys = ["first_name", "last_name", "pax_birth_data"]
g = df.groupby(keys)


def routes_count(idx):
    d = df.loc[idx, "departure"]
    a = df.loc[idx, "arrival"]
    return len(set(zip(d, a)))


agg = g.agg(
    n_flights_total=("flight_code", "count"),
    n_unique_documents=("document_norm", "nunique"),
    n_unique_agents=("agent_info", "nunique"),
    baggage_ratio=("baggage", lambda x: (x != "").mean()),
).reset_index()

agg["n_unique_routes"] = g["flight_code"].apply(lambda x: routes_count(x.index)).values

df2 = df.sort_values(keys + ["flight_date"])
df2["gap_days"] = df2.groupby(keys)["flight_date"].diff().dt.days

gap = (
    df2.groupby(keys)["gap_days"]
    .agg(mean_time_between_flights="mean", min_time_between_flights="min")
    .reset_index()
)

agg = agg.merge(gap, on=keys, how="left")


def days_active(x):
    x = x.dropna()
    if len(x) < 2:
        return 0
    return (x.max() - x.min()).days


agg["days_active"] = g["flight_date"].apply(days_active).values
m = (agg["days_active"] / 30).replace(0, 1)
agg["flights_per_month"] = agg["n_flights_total"] / m


def miss_ratio(sub):
    cols = ["fare", "baggage", "agent_info"]
    cols = [c for c in cols if c in sub.columns]
    if not cols:
        return np.nan
    return (sub[cols] == "").mean().mean()


agg["missing_ratio"] = g.apply(miss_ratio).values

route_freq = df.groupby(["departure", "arrival"]).size().rename("route_freq")
df = df.merge(route_freq, on=["departure", "arrival"], how="left")

agent_freq = df["agent_info"].value_counts().to_dict()

agg["avg_route_popularity"] = (
    g["flight_code"].apply(lambda x: df.loc[x.index, "route_freq"].mean()).values
)

agg["avg_agent_popularity"] = (
    g["agent_info"].apply(lambda x: np.mean([agent_freq.get(v, 0) for v in x])).values
)

X = agg.drop(columns=keys).fillna(0)
scaler = StandardScaler()
Xn = scaler.fit_transform(X)

model = IsolationForest(n_estimators=300, contamination=0.02, random_state=42)
model.fit(Xn)

agg["anomaly_score"] = model.decision_function(Xn)
agg["is_suspicious"] = model.predict(Xn)


def explain_row(r):
    out = []
    if r["n_unique_documents"] > 1:
        out.append("несколько документов")
    if r["flights_per_month"] > 10:
        out.append("частые перелёты")
    if r["baggage_ratio"] < 0.2 and r["n_flights_total"] > 5:
        out.append("мало багажа")
    if r["missing_ratio"] > 0.3:
        out.append("много пропусков")
    if r["n_unique_agents"] > 5:
        out.append("много агентств")
    if not out:
        out.append("совокупность признаков")
    return "; ".join(out)


agg["reason"] = agg.apply(explain_row, axis=1)

suspects = agg[agg["is_suspicious"] == -1].sort_values("anomaly_score")

out_path = r"D:\projects\python\AI2\data_staging\anomaly_people_report.csv"
agg.to_csv(out_path, index=False)
