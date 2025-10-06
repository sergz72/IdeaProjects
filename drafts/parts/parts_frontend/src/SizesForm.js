import React, { useState, useEffect } from 'react';
import './forms.css';

function SizesForm() {
    const [sizes, setSizes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingId, setEditingId] = useState(null);
    const [editingNewId, setEditingNewId] = useState('');
    const [newSizeId, setNewSizeId] = useState('');
    const [validationError, setValidationError] = useState('');

    useEffect(() => {
        fetchSizes();
    }, [newSizeId]);

    const fetchSizes = () => {
        const url = new URL(`${process.env.REACT_APP_API_BASE_URL}/sizes`);
        if (newSizeId.trim()) {
            url.searchParams.append('id', newSizeId.trim());
        }

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setSizes(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    };

    const validateId = (id) => {
        if (!id || id.trim().length === 0) {
            return 'ID is required';
        }
        if (id.trim().length < 1 || id.trim().length > 4) {
            return 'ID must be 1-4 characters';
        }
        return null;
    };

    const handleAddSize = (e) => {
        e.preventDefault();

        const error = validateId(newSizeId);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/sizes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ id: newSizeId.trim() }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add size');
                }
                return response.json();
            })
            .then(() => {
                setNewSizeId('');
                fetchSizes();
            })
            .catch(error => {
                alert('Error adding size: ' + error.message);
            });
    };

    const handleUpdateSize = (oldId) => {
        const error = validateId(editingNewId);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/sizes/${oldId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ id: editingNewId.trim() }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update size');
                }
                return response.json();
            })
            .then(() => {
                setEditingId(null);
                setEditingNewId('');
                fetchSizes();
            })
            .catch(error => {
                alert('Error updating size: ' + error.message);
            });
    };

    const handleDeleteSize = (id) => {
        if (!window.confirm('Are you sure you want to delete this size?')) {
            return;
        }

        fetch(`${process.env.REACT_APP_API_BASE_URL}/sizes/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete size');
                }
                fetchSizes();
            })
            .catch(error => {
                alert('Error deleting size: ' + error.message);
            });
    };

    const startEditing = (size) => {
        setEditingId(size.id);
        setEditingNewId(size.id);
    };

    const cancelEditing = () => {
        setEditingId(null);
        setEditingNewId('');
        setValidationError('');
    };

    if (loading) {
        return (
            <div>
                <h2>Sizes</h2>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div>
                <h2>Sizes</h2>
                <p style={{ color: 'red' }}>Error: {error}</p>
            </div>
        );
    }

    return (
        <div>
            <h2>Sizes</h2>

            <form onSubmit={handleAddSize} className="add-form">
                <input
                    type="text"
                    placeholder="New size ID (1-4 characters)"
                    value={newSizeId}
                    onChange={(e) => setNewSizeId(e.target.value)}
                    className="input-field"
                    maxLength={4}
                />
                <button type="submit" className="btn btn-add">Add Size</button>
            </form>
            {validationError && <p style={{ color: 'red', marginTop: '5px' }}>{validationError}</p>}

            <table className="form-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {sizes.map(size => (
                    <tr key={size.id}>
                        <td>
                            {editingId === size.id ? (
                                <input
                                    type="text"
                                    value={editingNewId}
                                    onChange={(e) => setEditingNewId(e.target.value)}
                                    className="input-field"
                                    maxLength={4}
                                />
                            ) : (
                                size.id
                            )}
                        </td>
                        <td>
                            {editingId === size.id ? (
                                <>
                                    <button
                                        onClick={() => handleUpdateSize(size.id)}
                                        className="btn btn-save"
                                    >
                                        Save
                                    </button>
                                    <button
                                        onClick={cancelEditing}
                                        className="btn btn-cancel"
                                    >
                                        Cancel
                                    </button>
                                </>
                            ) : (
                                <>
                                    <button
                                        onClick={() => startEditing(size)}
                                        className="btn btn-edit"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDeleteSize(size.id)}
                                        className="btn btn-delete"
                                    >
                                        Delete
                                    </button>
                                </>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            {sizes.length === 0 && <p>No sizes found.</p>}
        </div>
    );
}

export default SizesForm;