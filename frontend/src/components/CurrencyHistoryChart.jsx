import React, { useEffect, useRef, useState } from "react"; 
import { Sparklines, SparklinesLine } from "react-sparklines";
import { getCurrencyHistory } from "../services/api.js";

const cache = {};

function CurrencyHistoryChart({ from, to }) {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const chartRef = useRef(null);
  const [width, setWidth] = useState(0);

  useEffect(() => {
    if (!from || !to) return;
    setLoading(true);
    setData([]);

    const key = `${from}_${to}`;

    if (cache[key]) {
      setData(cache[key]);
      setLoading(false);
    } else {
      async function fetchHistory() {
        try {
          const history = await getCurrencyHistory(from, to);
          const rates = history.map(point => point.rate).filter(r => !isNaN(r));
          cache[key] = rates;
          setData(rates);
        } catch (err) {
          console.error("[CurrencyHistoryChart] Error fetching history:", err);
          setData([]);
        } finally {
          setLoading(false);
        }
      }

      fetchHistory();
    }
  }, [from, to]);

  useEffect(() => {
    if (chartRef.current) setWidth(chartRef.current.offsetWidth);
    const handleResize = () => {
      if (chartRef.current) setWidth(chartRef.current.offsetWidth);
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const startValue = data.length ? data[0] : null;
  const endValue = data.length ? data[data.length - 1] : null;

  const percentChange =
    startValue && endValue ? ((endValue - startValue) / startValue) * 100 : null;
  const percentColor = percentChange >= 0 ? "#16a34a" : "#dc2626"; // green if >=0 else red

  const minValue = data.length ? Math.min(...data) : 0;
  const maxValue = data.length ? Math.max(...data) : 1;
  const buffer = (maxValue - minValue) * 0.05 || 0.01;
  const minWithBuffer = minValue - buffer;
  const maxWithBuffer = maxValue + buffer;

  return (
    <div className="currency-history-chart-container">
      <h4>
        {from?.toUpperCase()} uz {to?.toUpperCase()} (30 dienās):
      </h4>
      <div className="currency-history-chart" ref={chartRef} style={{ position: "relative" }}>
        {loading ? (
          <p style={{ textAlign: "center" }}>Ielādē datus...</p>
        ) : data.length ? (
          <>
            <Sparklines
              data={data}
              width={width}
              height={70}
              margin={0}
              min={minWithBuffer}
              max={maxWithBuffer}
            >
              <SparklinesLine color="#4f46e5" style={{ strokeWidth: 2, fill: "none" }} />
            </Sparklines>
            <div style={{ position: "absolute", left: 0, bottom: 0, fontSize: 12 }}>
              {startValue.toFixed(4)}
            </div>
            {percentChange !== null && (
              <div
                style={{
                  position: "absolute",
                  bottom: 0,
                  left: "50%",
                  transform: "translateX(-50%)",
                  fontSize: 12,
                  color: percentColor,
                }}
              >
                {percentChange >= 0 ? "+" : ""}
                {percentChange.toFixed(2)}%
              </div>
            )}
            <div style={{ position: "absolute", right: 0, bottom: 0, fontSize: 12 }}>
              {endValue.toFixed(4)}
            </div>
          </>
        ) : (
          <p style={{ textAlign: "center" }}>No data available</p>
        )}
      </div>
    </div>
  );
}

export default CurrencyHistoryChart;
