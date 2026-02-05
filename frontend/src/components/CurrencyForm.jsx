import React, { useState, useEffect, useRef } from "react"; 
import DatePicker from "./DatePicker.jsx";
import { convertCurrency } from "../services/api.js";
import CurrencyHistoryChart from "./CurrencyHistoryChart.jsx";

const CURRENCIES = [
  { code: "EUR", name: "Eiro" },
  { code: "USD", name: "ASV dolārs" },
  { code: "AUD", name: "Austrālijas dolārs" },
  { code: "BRL", name: "Brazīlijas reāls" },
  { code: "CAD", name: "Kanādas dolārs" },
  { code: "CHF", name: "Šveices franks" },
  { code: "CNY", name: "Ķīnas juaņs renminbi" },
  { code: "CZK", name: "Čehijas krona" },
  { code: "DKK", name: "Dānijas krona" },
  { code: "GBP", name: "Lielbritānijas sterliņu mārciņa" },
  { code: "HKD", name: "Honkongas dolārs" },
  { code: "HUF", name: "Ungārijas forints" },
  { code: "IDR", name: "Indonēzijas rūpija" },
  { code: "ILS", name: "Izraēlas šekelis" },
  { code: "INR", name: "Indijas rūpija" },
  { code: "ISK", name: "Islande krona" },
  { code: "JPY", name: "Japānas jēna" },
  { code: "KRW", name: "Dienvidkorejas vona" },
  { code: "MXN", name: "Meksikas peso" },
  { code: "MYR", name: "Malaizijas ringits" },
  { code: "NOK", name: "Norvēģijas krona" },
  { code: "NZD", name: "Jaunzēlandes dolārs" },
  { code: "PHP", name: "Filipīnu peso" },
  { code: "PLN", name: "Polijas zlots" },
  { code: "RON", name: "Rumānijas leja" },
  { code: "SEK", name: "Zviedrijas krona" },
  { code: "SGD", name: "Singapūras dolārs" },
  { code: "THB", name: "Taizemes bats" },
  { code: "TRY", name: "Turcijas lira" },
  { code: "ZAR", name: "Dienvidāfrikas rands" }
];

function Dropdown({ options, value, onChange }) {
  const [open, setOpen] = useState(false);
  const ref = useRef();

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (ref.current && !ref.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const selected = options.find((o) => o.code === value);

  return (
    <div className="custom-dropdown" ref={ref}>
      <div
        className="currency-field select-like"
        onClick={() => setOpen(!open)}
      >
        {selected ? selected.code : "Select..."}
      </div>

      {open && (
        <ul className="dropdown-list">
          {options.map((opt) => (
            <li
              key={opt.code}
              onClick={() => {
                onChange(opt.code);
                setOpen(false);
              }}
            >
              {opt.code} ({opt.name})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

const cache = {};

function CurrencyForm({ setConversionResult }) {
  const [from, setFrom] = useState("EUR");
  const [to, setTo] = useState("USD");
  const [amount, setAmount] = useState(1);
  const [date, setDate] = useState(new Date());
  const [result, setResult] = useState("");
  const [rate, setRate] = useState(null);

  const handleSwap = () => {
    setFrom(to);
    setTo(from);
    setResult("");
    setRate(null);
  };

  useEffect(() => {
    if (!amount || amount <= 0) {
      setResult("");
      setRate(null);
      return;
    }

    if (from === to) {
      setResult(amount);
      setRate(1);
      setConversionResult({ from, to, amount, convertedAmount: amount, rate: 1 });
      return;
    }

    const cacheKey = `${from}_${to}_${amount}_${date.toISOString().split("T")[0]}`;

    if (cache[cacheKey]) {
      const cached = cache[cacheKey];
      setResult(cached.convertedAmount.toFixed(2));
      setRate(cached.convertedAmount / amount);
      setConversionResult(cached);
      return;
    }

    const timeout = setTimeout(async () => {
      try {
        const res = await convertCurrency(from, to, amount, date);
        if (res && typeof res.convertedAmount === "number") {
          setResult(res.convertedAmount.toFixed(2));
          setRate(res.convertedAmount / amount);
          setConversionResult(res);
          cache[cacheKey] = res;
        } else {
          setResult("Kļūda");
          setRate(null);
        }
      } catch (err) {
        console.error(err);
        setResult("Kļūda");
        setRate(null);
      }
    }, 300);

    return () => clearTimeout(timeout);
  }, [from, to, amount, date]);

  const inverseRate = rate ? (1 / rate).toFixed(4) : null;

  return (
    <div className="currency-converter-container" style={{ position: "relative" }}>
      <form className="currency-form">
        <div className="currency-row">
          <div className="currency-field">
            <label>Pārdot:</label>
            <Dropdown options={CURRENCIES} value={from} onChange={setFrom} />
            <input
              type="text"
              value={amount}
              onChange={(e) => {
                let sanitizedValue = e.target.value.replace(/^0+(?=\d)/, "");
                if (sanitizedValue === "") sanitizedValue = "0";
                setAmount(sanitizedValue);
              }}
            />
            <div className="current-rate">
              {rate ? `Pērk: 1 ${from} = ${rate.toFixed(4)} ${to}` : "\u00A0"}
            </div>
          </div>

          <div className="action">
            <button type="button" onClick={handleSwap}>⇄</button>
          </div>

          <div className="currency-field">
            <label>Pirkt:</label>
            <Dropdown options={CURRENCIES} value={to} onChange={setTo} />
            <input
              type="text"
              value={result}
              readOnly
              placeholder="Ievadiet daudzumu, ko pārdot"
            />
            <div className="current-rate">
              {inverseRate ? `Pārdod: 1 ${to} = ${inverseRate} ${from}` : "\u00A0"}
            </div>
          </div>
        </div>

        <div className="date-chart-wrapper">
          <div className="date-picker">
            <label>Datums:</label>
            <DatePicker value={date} onChange={setDate} />
          </div>
          <CurrencyHistoryChart from={from} to={to} />
        </div>

      </form>
    </div>
  );
}

export default CurrencyForm;
