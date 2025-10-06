import React, {useEffect, useState} from 'react';
import './forms.css';

function PrecisionsForm() {
    const [precisions, setPrecisions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingId, setEditingId] = useState(null);
    const [editingValue, setEditingValue] = useState(null);
    const [newPrecisionValue, setNewPrecisionValue] = useState(null);
    const [validationError, setValidationError] = useState('');

    useEffect(() => {
        fetchPrecisions();
    }, [newPrecisionValue]);

    const fetchPrecisions = () => {
        const url = new URL(`${process.env.REACT_APP_API_BASE_URL}/precisions`);
        if (newPrecisionValue) {
            url.searchParams.append('value', newPrecisionValue.toString());
        }

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setPrecisions(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    };

    const validateValue = (value) => {
        if (!value || value.trim() === '') {
            return 'Value is required';
        }
        if (isNaN(value) || isNaN(parseFloat(value))) {
            return 'Value must be a valid number';
        }
        return null;
    };

    const handleNumericInput = (value) => {
        // Allow empty string, numbers, decimal point, and minus sign
        // This regex allows: optional minus, digits, optional decimal point with digits
        if (value === '' || value === '-' || /^-?\d*\.?\d*$/.test(value)) {
            return value;
        }
        return null;
    };

    const handleAddPrecision = (e) => {
        e.preventDefault();

        const error = validateValue(newPrecisionValue);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/precisions`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ value: newPrecisionValue }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add precision');
                }
                return response.json();
            })
            .then(() => {
                setNewPrecisionValue(null);
                fetchPrecisions();
            })
            .catch(error => {
                alert('Error adding precision: ' + error.message);
            });
    };

    const handleUpdatePrecision = (id) => {
        const error = validateValue(editingValue);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/precisions/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ value: editingValue }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update precision');
                }
                return response.json();
            })
            .then(() => {
                setEditingId(null);
                setEditingValue(null);
                fetchPrecisions();
            })
            .catch(error => {
                alert('Error updating Precision: ' + error.message);
            });
    };

    const handleDeletePrecision = (id) => {
        if (!window.confirm('Are you sure you want to delete this precision?')) {
            return;
        }

        fetch(`${process.env.REACT_APP_API_BASE_URL}/precisions/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete precision');
                }
                fetchPrecisions();
            })
            .catch(error => {
                alert('Error deleting precision: ' + error.message);
            });
    };

    const startEditing = (precision) => {
        setEditingId(precision.id);
        setEditingValue(precision.value);
    };

    const cancelEditing = () => {
        setEditingId(null);
        setEditingValue(null);
        setValidationError('');
    };

    if (loading) {
        return (
            <div>
                <h2>Precisions</h2>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div>
                <h2>Precisions</h2>
                <p style={{ color: 'red' }}>Error: {error}</p>
            </div>
        );
    }

    return (
        <div>
            <h2>Precisions</h2>

            <form onSubmit={handleAddPrecision} className="add-form">
                <input
                    type="text"
                    placeholder="New Precision name"
                    value={newPrecisionValue}
                    onChange={(e) => {
                        const filtered = handleNumericInput(e.target.value);
                        if (filtered !== null) {
                            setNewPrecisionValue(filtered);
                        }
                    }}
                    className="input-field"
                    maxLength={50}
                />
                <button type="submit" className="btn btn-add">Add Precision</button>
            </form>
            {validationError && <p style={{ color: 'red', marginTop: '5px' }}>{validationError}</p>}

            <table className="form-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Value</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {precisions.map(precision => (
                    <tr key={precision.id}>
                        <td>{precision.id}</td>
                        <td>
                            {editingId === precision.id ? (
                                <input
                                    type="text"
                                    value={editingValue}
                                    onChange={(e) => {
                                        const filtered = handleNumericInput(e.target.value);
                                        if (filtered !== null) {
                                            setEditingValue(filtered);
                                        }
                                    }}
                                    className="input-field"
                                    maxLength={50}
                                />
                            ) : (
                                precision.value
                            )}
                        </td>
                        <td>
                            {editingId === precision.id ? (
                                <>
                                    <button
                                        onClick={() => handleUpdatePrecision(precision.id)}
                                        className="btn btn-save">
                                        Save
                                    </button>
                                    <button
                                        onClick={cancelEditing}
                                        className="btn btn-cancel">
                                        Cancel
                                    </button>
                                </>
                            ) : (
                                <>
                                    <button
                                        onClick={() => startEditing(precision)}
                                        className="btn btn-edit">
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDeletePrecision(precision.id)}
                                        className="btn btn-delete">
                                        Delete
                                    </button>
                                </>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            {precisions.length === 0 && <p>No precisions found.</p>}
        </div>
    );
}

export default PrecisionsForm;
