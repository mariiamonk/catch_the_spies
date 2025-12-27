from analysis_core import main_analysis
from map_visuals import passenger_map
import os


def main():
    dpath = "../data/merged_all_detailed.csv"

    base, stats = main_analysis(dpath)
    print("Всего:", len(stats))
    print("Подозрительных:", stats["is_suspicious"].sum())

    while True:
        q = input("Поиск (имя/фам/дата) или exit: ").strip()
        if q == "exit":
            break

        r = stats[
            stats["first_name"].str.contains(q, case=False, na=False) |
            stats["last_name"].str.contains(q, case=False, na=False) |
            stats["pax_birth_data"].str.contains(q, case=False, na=False)
        ]

        if len(r) == 0:
            print("Не найдено")
            continue

        for i, (_, row) in enumerate(r.head(10).iterrows(), 1):
            print(f"{i}. {row['first_name']} {row['last_name']} {row['pax_birth_data']}  {row['risk_score']}")

        pick = input("Номер: ").strip()
        if pick.isdigit():
            pick = int(pick) - 1
            if 0 <= pick < len(r.head(10)):
                row = r.iloc[pick]

                base_piece = base[
                    (base["first_name"] == row["first_name"]) &
                    (base["last_name"] == row["last_name"]) &
                    (base["pax_birth_data"] == row["pax_birth_data"])
                ].copy()

                print("Рейсов:", row["n_flights_total"])
                print("Категория:", row["risk_category"])

                if input("Карта? y/n: ").strip().lower() == "y":
                    fn = f"map_{row['first_name']}_{row['last_name']}.html"
                    passenger_map(row, base_piece, fn)
                    print("Сохранено:", fn)


if __name__ == "__main__":
    main()
