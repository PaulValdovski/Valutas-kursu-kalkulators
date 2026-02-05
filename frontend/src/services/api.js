export async function convertCurrency(from, to, amount, date) {
  if (!from || !to || amount <= 0 || !(date instanceof Date)) {
    console.error('Invalid parameters:', { from, to, amount, date });
    return null;
  }

  try {
    const jsDate = date instanceof Date ? date : new Date(date);
    const formattedDate = jsDate.toISOString().split('T')[0];
    const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
    const url = `${apiUrl}/convert?from=${from}&to=${to}&amount=${amount}&date=${formattedDate}`;

    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`Backend returned ${response.status}`);
    }

    const data = await response.json();
    return data;

  } catch (err) {
    console.error('Error fetching conversion:', err);
    return null;
  }
}

export async function getCurrencyHistory(from, to, days = 30) {
  if (!from || !to || days <= 0) {
    console.error('Invalid parameters for history:', { from, to, days });
    return null;
  }

  try {
    const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
    const url = `${apiUrl}/history?from=${from}&to=${to}&days=${days}`;

    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`Backend returned ${response.status}`);
    }

    const data = await response.json();

    console.log('[getCurrencyHistory] Fetched data:', data);
    return data;

  } catch (err) {
    console.error('Error fetching historical rates:', err);
    return null;
  }
}