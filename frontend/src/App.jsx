import React, { useState } from 'react';
import CurrencyForm from './components/CurrencyForm';
import './App.css';

function App() {
  const [conversionResult, setConversionResult] = useState(null);

  return (
    <div className="container">
      <h1 className="app-title">Valūtas kursu kalkulators</h1>
      <CurrencyForm setConversionResult={setConversionResult} />
      {conversionResult && (
        <div className="conversion-result"> </div>
      )}
    </div>
  );
}

export default App;
