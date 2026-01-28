import React from 'react';

function DatePicker({ value, onChange }) {
  const formatDate = (date) => date.toISOString().split('T')[0];
  const today = formatDate(new Date());

  const handleChange = (e) => {
    const selectedDate = new Date(e.target.value);
    if (!isNaN(selectedDate)) {
      onChange(selectedDate);
    }
  };

  return (
    <input
      type="date"
      value={formatDate(value)}
      max={today}
      onChange={handleChange}
      className="date-picker-input"
    />
  );
}

export default DatePicker;
