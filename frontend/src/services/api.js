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
