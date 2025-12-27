from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

RAW = ROOT / "data_raw"
STAGING = ROOT / "data_staging"
FEATURES = ROOT / "data_features"
MODELS = ROOT / "models"

for p in [RAW, STAGING, FEATURES, MODELS]:
    p.mkdir(exist_ok=True)

RAW_SQL = RAW / "data.txt"