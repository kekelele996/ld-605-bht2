import { useState } from "react";
import { createRoot } from "react-dom/client";
import { routes } from "./router/routes";
import { PipelinesPage } from "./pages/PipelinesPage";
import "./styles.css";

/**
 * 管网资产页已接入风险调整联动；其余页面保持评审占位，
 * 选择左侧导航切换，默认进入 /pipelines 便于评审本次改动。
 */
function PlaceholderPage({ name }: { name: string }) {
  return (
    <main className="page">
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>{name}</h1>
        </div>
      </section>
      <section className="panel">
        <p>该页面为评审占位；风险调整与巡检周期联动请前往「管网资产」。</p>
      </section>
    </main>
  );
}

function App() {
  const [active, setActive] = useState<string>("/pipelines");
  const current = routes.find((route) => route.route === active) ?? routes[1];

  let content;
  if (active === "/pipelines") {
    content = <PipelinesPage />;
  } else {
    content = <PlaceholderPage name={current?.name ?? "工作台"} />;
  }

  return (
    <div className="shell">
      <aside>
        <div className="brand">城市水务漏损巡检平台</div>
        <nav>
          {routes.map((route) => (
            <button
              key={route.route}
              className={active === route.route ? "active" : ""}
              onClick={() => setActive(route.route)}
            >
              {route.name}
            </button>
          ))}
        </nav>
      </aside>
      {content}
    </div>
  );
}

createRoot(document.getElementById("root")!).render(<App />);
