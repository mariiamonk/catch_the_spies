import re
import pandas as pd
from pathlib import Path

base = Path(__file__).resolve().parents[1]

input_path = base / "data.txt"
output_dir = base / "data_staging"
output_dir.mkdir(parents=True, exist_ok=True)

with open(input_path, "r", errors="ignore") as f:
    text = f.read()

pattern = re.compile(
    r"COPY\s+([\w\.]+)\s*\(([^)]+)\)\s+FROM\s+stdin;\s*(.*?)\n\\\.",
    re.DOTALL | re.IGNORECASE,
)

blocks = pattern.findall(text)

for table_name, columns, rows in blocks:
    cols = [c.strip() for c in columns.split(",")]

    lines = rows.strip().split("\n")
    data = []

    for line in lines:
        if line.strip() == "":
            continue
        parts = line.split("\t")
        row = []
        for p in parts:
            if p == r"\N":
                row.append(None)
            else:
                row.append(p)
        data.append(row)

    if not data:
        continue

    max_len = 0
    for r in data:
        if len(r) > max_len:
            max_len = len(r)

    if len(cols) < max_len:
        for i in range(len(cols) + 1, max_len + 1):
            cols.append("extra_" + str(i))
    if len(cols) > max_len:
        cols = cols[:max_len]

    df = pd.DataFrame(data, columns=cols)
    name = table_name.replace(".", "_") + ".csv"
    df.to_csv(output_dir / name, index=False)
