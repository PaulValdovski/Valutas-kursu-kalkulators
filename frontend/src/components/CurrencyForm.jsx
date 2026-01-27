import React, { useState, useEffect } from 'react';
import DatePicker from './DatePicker.jsx';
import { convertCurrency } from '../services/api.js';

const CURRENCIES = [
  "EUR", "AUD", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD",
  "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK",
  "NZD", "PHP", "PLN", "RON", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
];

function CurrencyForm({ setConversionResult }) {
  const [from, setFrom] = useState('EUR');
  const [to, setTo] = useState('USD');
  const [amount, setAmount] = useState(1);
  const [date, setDate] = useState(new Date());
  const [result, setResult] = useState('');
  const [rate, setRate] = useState(null);

  const handleSwap = () => {
    setFrom(to);
    setTo(from);
    setResult('');
    setRate(null);
  };

  useEffect(() => {
    if (!amount || amount <= 0 || from === to) {
      setResult('');
      setRate(null);
      return;
    }

    const timeout = setTimeout(async () => {
      try {
        const res = await convertCurrency(from, to, amount, date);
        if (res && typeof res.convertedAmount === 'number') {
          setResult(res.convertedAmount.toFixed(2));
          setRate(res.convertedAmount / amount);
          setConversionResult(res);
        } else {
          setResult('Kļūda');
          setRate(null);
        }
      } catch (err) {
        console.error(err);
        setResult('Kļūda');
        setRate(null);
      }
    }, 300);

    return () => clearTimeout(timeout);
  }, [from, to, amount, date]);

  const inverseRate = rate ? (1 / rate).toFixed(4) : null;

  return (
    <div className="currency-converter-container">
      <form className="currency-form">

        <div className="currency-row">
          <div className="currency-field">
            <label>Pārdot:</label>
            <select value={from} onChange={e => setFrom(e.target.value)}>
              {CURRENCIES.map(c => <option key={c} value={c}>{c}</option>)}
            </select>
            <input
              type="text"
              value={amount}
              onChange={e => {
                let sanitizedValue = e.target.value.replace(/^0+(?=\d)/, '');
                if (sanitizedValue === '') sanitizedValue = '0';
                setAmount(sanitizedValue);
              }}
            />
            <div className="current-rate">
              {rate ? `Pērk: 1 ${from} = ${rate.toFixed(4)} ${to}` : '\u00A0'}
            </div>
          </div>

          <div className="action">
            <button type="button" onClick={handleSwap}>⇄</button>
          </div>

          <div className="currency-field">
            <label>Pirkt:</label>
            <select value={to} onChange={e => setTo(e.target.value)}>
              {CURRENCIES.map(c => <option key={c} value={c}>{c}</option>)}
            </select>
            <input
              type="text"
              value={result}
              readOnly
              placeholder="Ievadiet daudzumu, ko pārdot"
            />
            <div className="current-rate">
              {inverseRate ? `Pārdod: 1 ${to} = ${inverseRate} ${from}` : '\u00A0'}
            </div>
          </div>
        </div>

        <div className="date-picker">
          <label>Datums:</label>
          <DatePicker value={date} onChange={setDate} />
        </div>

      </form>
    </div>
  );
}

export default CurrencyForm;
