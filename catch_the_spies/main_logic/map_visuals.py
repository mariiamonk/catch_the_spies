import plotly.graph_objects as go
from geopy.geocoders import Nominatim


class GeoLook:
    def __init__(self):
        self.g = Nominatim(user_agent="geo_map_x1")
        self.cache = {}

    def pos(self, name):
        if name in self.cache:
            return self.cache[name]
        try:
            r = self.g.geocode(name + " airport")
            if r:
                self.cache[name] = (r.latitude, r.longitude)
                return self.cache[name]
        except:
            pass
        return None


def passenger_map(row, fl, out_path):
    G = GeoLook()

    fl = fl[["departure", "arrival"]].dropna()
    aps = set(list(fl["departure"]) + list(fl["arrival"]))

    coords = {}
    for a in aps:
        r = G.pos(a)
        if r:
            coords[a] = r

    xs = []
    ys = []
    for _, rr in fl.iterrows():
        d = rr["departure"]
        a = rr["arrival"]
        if d in coords and a in coords:
            xs.extend([coords[d][1], coords[a][1], None])
            ys.extend([coords[d][0], coords[a][0], None])

    fig = go.Figure()

    fig.add_trace(go.Scattergeo(
        lon=xs, lat=ys,
        mode="lines",
        line=dict(width=2),
        opacity=0.6
    ))

    fig.add_trace(go.Scattergeo(
        lon=[coords[x][1] for x in coords],
        lat=[coords[x][0] for x in coords],
        mode="markers",
        marker=dict(size=7)
    ))

    ttl = f"{row['first_name']} {row['last_name']} — {row['risk_category']} ({row['risk_score']})"
    fig.update_layout(title=ttl, geo=dict(scope="world"))

    fig.write_html(out_path)
    return fig
