import pandas as pd
import re
from pathlib import Path

base = Path(__file__).parent

flights = pd.read_csv(base / "wrk_flights.csv", low_memory=False)
sirena = pd.read_csv(base / "airlines_sirena_export.csv", low_memory=False)
sirena_users = pd.read_csv(base / "airlines_sirena_export_users.csv", low_memory=False)
users = pd.read_csv(base / "wrk_users.csv", low_memory=False)


def norm_doc(x):
    if not isinstance(x, str):
        return ""
    return re.sub(r"\D", "", x)


def norm_name(x):
    if not isinstance(x, str):
        return ""
    return re.sub(r"[^A-Za-zА-Яа-яЁё ]", "", x).lower().strip()


for df in [sirena, sirena_users, users]:
    if "document" in df.columns:
        df["document_norm"] = df["document"].apply(norm_doc)
    if "travel_doc" in df.columns:
        df["document_norm"] = df["travel_doc"].apply(norm_doc)

merged = flights.merge(sirena, left_on="sirena_id", right_on="id", how="left")

if "pax_name" in merged.columns:
    merged["pax_last"] = merged["pax_name"].apply(
        lambda x: x.split()[0] if isinstance(x, str) and len(x.split()) > 0 else ""
    )
    merged["pax_first"] = merged["pax_name"].apply(
        lambda x: x.split()[1] if isinstance(x, str) and len(x.split()) > 1 else ""
    )
else:
    merged["pax_last"] = ""
    merged["pax_first"] = ""

merged["pax_last_norm"] = merged["pax_last"].apply(norm_name)
merged["pax_first_norm"] = merged["pax_first"].apply(norm_name)

sirena_users["last_name_norm"] = sirena_users["last_name"].apply(norm_name)
sirena_users["first_name_norm"] = sirena_users["first_name"].apply(norm_name)

merged = merged.merge(
    sirena_users[
        [
            "first_name",
            "last_name",
            "second_name",
            "last_name_norm",
            "first_name_norm",
            "pax_birth_data",
        ]
    ],
    left_on=["pax_last_norm", "pax_first_norm", "pax_birth_data"],
    right_on=["last_name_norm", "first_name_norm", "pax_birth_data"],
    how="left",
    suffixes=("", "_su"),
)

merged = merged.merge(
    users[["first_name_v2", "last_name_v2", "sex", "document_norm", "pax_birth_data"]],
    on=["document_norm", "pax_birth_data"],
    how="left",
)


def pick(a, b, c, d):
    for x in [a, b, c, d]:
        if isinstance(x, str) and x.strip():
            return x.strip()
    return ""


merged["first_name"] = merged.apply(
    lambda r: pick(
        r.get("first_name_v2"),
        r.get("first_name_su"),
        r.get("first_name"),
        r.get("pax_first"),
    ),
    axis=1,
)

merged["last_name"] = merged.apply(
    lambda r: pick(
        r.get("last_name_v2"),
        r.get("last_name_su"),
        r.get("last_name"),
        r.get("pax_last"),
    ),
    axis=1,
)

cols = [
    "flight_code",
    "flight_date",
    "departure",
    "arrival",
    "fare",
    "baggage",
    "agent_info",
    "first_name",
    "last_name",
    "second_name",
    "sex",
    "pax_birth_data",
    "document_norm",
]

cols = [c for c in cols if c in merged.columns]

merged[cols].fillna("").to_csv(base / "merged_all_detailed.csv", index=False)
