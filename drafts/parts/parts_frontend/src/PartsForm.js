
import React, { useState, useEffect } from 'react';
import './forms.css';

function PartsForm() {
    const [parts, setParts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [sizes, setSizes] = useState([]);
    const [units, setUnits] = useState([]);
    const [precisions, setPrecisions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingId, setEditingId] = useState(null);
    const [editingData, setEditingData] = useState({});
    const [newPart, setNewPart] = useState({
        name: '',
        categoryId: '',
        sizeId: '',
        unitId: '',
        precisionId: ''
    });
    const [validationError, setValidationError] = useState('');

    useEffect(() => {
        fetchAllData();
    }, []);

    const fetchAllData = async () => {
        try {
            const [partsRes, categoriesRes, sizesRes, unitsRes, precisionsRes] = await Promise.all([
                fetch(`${process.env.REACT_APP_API_BASE_URL}/parts`),
                fetch(`${process.env.REACT_APP_API_BASE_URL}/categories`),
                fetch(`${process.env.REACT_APP_API_BASE_URL}/sizes`),
                fetch(`${process.env.REACT_APP_API_BASE_URL}/units`),
                fetch(`${process.env.REACT_APP_API_BASE_URL}/precisions`)
            ]);

            if (!partsRes.ok || !categoriesRes.ok || !sizesRes.ok || !unitsRes.ok || !precisionsRes.ok) {
                throw new Error('Failed to fetch data');
            }

            const [partsData, categoriesData, sizesData, unitsData, precisionsData] = await Promise.all([
                partsRes.json(),
                categoriesRes.json(),
                sizesRes.json(),
                unitsRes.json(),
                precisionsRes.json()
            ]);

            setParts(partsData);
            setCategories(categoriesData);
            setSizes(sizesData);
            setUnits(unitsData);
            setPrecisions(precisionsData);
            setLoading(false);
        } catch (error) {
            setError(error.message);
            setLoading(false);
        }
    };

    const fetchParts = () => {
        fetch(`${process.env.REACT_APP_API_BASE_URL}/parts`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setParts(data);
            })
            .catch(error => {
                alert('Error fetching parts: ' + error.message);
            });
    };

    const validatePart = (part) => {
        if (!part.name || part.name.trim().length === 0) {
            return 'Name is required';
        }
        if (part.name.trim().length > 100) {
            return 'Name must not exceed 100 characters';
        }
        if (!part.categoryId) {
            return 'Category is required';
        }
        if (!part.sizeId) {
            return 'Size is required';
        }
        if (!part.unitId) {
            return 'Unit is required';
        }
        if (!part.precisionId) {
            return 'Precision is required';
        }
        return null;
    };

    const handleAddPart = (e) => {
        e.preventDefault();

        const error = validatePart(newPart);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/parts`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: newPart.name.trim(),
                categoryId: parseInt(newPart.categoryId),
                sizeId: newPart.sizeId,
                unitId: newPart.unitId,
                precisionId: newPart.precisionId
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add part');
                }
                return response.json();
            })
            .then(() => {
                setNewPart({
                    name: '',
                    categoryId: '',
                    sizeId: '',
                    unitId: '',
                    precisionId: ''
                });
                fetchParts();
            })
            .catch(error => {
                alert('Error adding part: ' + error.message);
            });
    };

    const handleUpdatePart = (id) => {
        const error = validatePart(editingData);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/parts/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: editingData.name.trim(),
                categoryId: parseInt(editingData.categoryId),
                sizeId: editingData.sizeId,
                unitId: editingData.unitId,
                precisionId: editingData.precisionId
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update part');
                }
                return response.json();
            })
            .then(() => {
                setEditingId(null);
                setEditingData({});
                fetchParts();
            })
            .catch(error => {
                alert('Error updating part: ' + error.message);
            });
    };

    const handleDeletePart = (id) => {
        if (!window.confirm('Are you sure you want to delete this part?')) {
            return;
        }

        fetch(`${process.env.REACT_APP_API_BASE_URL}/parts/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete part');
                }
                fetchParts();
            })
            .catch(error => {
                alert('Error deleting part: ' + error.message);
            });
    };

    const startEditing = (part) => {
        setEditingId(part.id);
        setEditingData({
            name: part.name,
            categoryId: part.categoryId,
            sizeId: part.sizeId,
            unitId: part.unitId,
            precisionId: part.precisionId
        });
    };

    const cancelEditing = () => {
        setEditingId(null);
        setEditingData({});
        setValidationError('');
    };

    const getCategoryName = (categoryId) => {
        const category = categories.find(c => c.id === categoryId);
        return category ? category.name : categoryId;
    };

    if (loading) {
        return (
            <div>
                <h2>Parts</h2>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div>
                <h2>Parts</h2>
                <p style={{ color: 'red' }}>Error: {error}</p>
            </div>
        );
    }

    return (
        <div>
            <h2>Parts</h2>

            <form onSubmit={handleAddPart} className="add-form">
                <input
                    type="text"
                    placeholder="Part name"
                    value={newPart.name}
                    onChange={(e) => setNewPart({ ...newPart, name: e.target.value })}
                    className="input-field"
                    maxLength={100}
                />
                <select
                    value={newPart.categoryId}
                    onChange={(e) => setNewPart({ ...newPart, categoryId: e.target.value })}
                    className="input-field"
                >
                    <option value="">Select Category</option>
                    {categories.map(category => (
                        <option key={category.id} value={category.id}>
                            {category.name}
                        </option>
                    ))}
                </select>
                <select
                    value={newPart.sizeId}
                    onChange={(e) => setNewPart({ ...newPart, sizeId: e.target.value })}
                    className="input-field"
                >
                    <option value="">Select Size</option>
                    {sizes.map(size => (
                        <option key={size.id} value={size.id}>
                            {size.id}
                        </option>
                    ))}
                </select>
                <select
                    value={newPart.unitId}
                    onChange={(e) => setNewPart({ ...newPart, unitId: e.target.value })}
                    className="input-field"
                >
                    <option value="">Select Unit</option>
                    {units.map(unit => (
                        <option key={unit.id} value={unit.id}>
                            {unit.id}
                        </option>
                    ))}
                </select>
                <select
                    value={newPart.precisionId}
                    onChange={(e) => setNewPart({ ...newPart, precisionId: e.target.value })}
                    className="input-field"
                >
                    <option value="">Select Precision</option>
                    {precisions.map(precision => (
                        <option key={precision.id} value={precision.id}>
                            {precision.id}
                        </option>
                    ))}
                </select>
                <button type="submit" className="btn btn-add">Add Part</button>
            </form>
            {validationError && <p style={{ color: 'red', marginTop: '5px' }}>{validationError}</p>}

            <table className="form-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Size</th>
                    <th>Unit</th>
                    <th>Precision</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {parts.map(part => (
                    <tr key={part.id}>
                        <td>{part.id}</td>
                        <td>
                            {editingId === part.id ? (
                                <input
                                    type="text"
                                    value={editingData.name}
                                    onChange={(e) => setEditingData({ ...editingData, name: e.target.value })}
                                    className="input-field"
                                    maxLength={100}
                                />
                            ) : (
                                part.name
                            )}
                        </td>
                        <td>
                            {editingId === part.id ? (
                                <select
                                    value={editingData.categoryId}
                                    onChange={(e) => setEditingData({ ...editingData, categoryId: e.target.value })}
                                    className="input-field"
                                >
                                    <option value="">Select Category</option>
                                    {categories.map(category => (
                                        <option key={category.id} value={category.id}>
                                            {category.name}
                                        </option>
                                    ))}
                                </select>
                            ) : (
                                getCategoryName(part.categoryId)
                            )}
                        </td>
                        <td>
                            {editingId === part.id ? (
                                <select
                                    value={editingData.sizeId}
                                    onChange={(e) => setEditingData({ ...editingData, sizeId: e.target.value })}
                                    className="input-field"
                                >
                                    <option value="">Select Size</option>
                                    {sizes.map(size => (
                                        <option key={size.id} value={size.id}>
                                            {size.id}
                                        </option>
                                    ))}
                                </select>
                            ) : (
                                part.sizeId
                            )}
                        </td>
                        <td>
                            {editingId === part.id ? (
                                <select
                                    value={editingData.unitId}
                                    onChange={(e) => setEditingData({ ...editingData, unitId: e.target.value })}
                                    className="input-field"
                                >
                                    <option value="">Select Unit</option>
                                    {units.map(unit => (
                                        <option key={unit.id} value={unit.id}>
                                            {unit.id}
                                        </option>
                                    ))}
                                </select>
                            ) : (
                                part.unitId
                            )}
                        </td>
                        <td>
                            {editingId === part.id ? (
                                <select
                                    value={editingData.precisionId}
                                    onChange={(e) => setEditingData({ ...editingData, precisionId: e.target.value })}
                                    className="input-field"
                                >
                                    <option value="">Select Precision</option>
                                    {precisions.map(precision => (
                                        <option key={precision.id} value={precision.id}>
                                            {precision.id}
                                        </option>
                                    ))}
                                </select>
                            ) : (
                                part.precisionId
                            )}
                        </td>
                        <td>
                            {editingId === part.id ? (
                                <>
                                    <button
                                        onClick={() => handleUpdatePart(part.id)}
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
                                        onClick={() => startEditing(part)}
                                        className="btn btn-edit"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDeletePart(part.id)}
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
            {parts.length === 0 && <p>No parts found.</p>}
        </div>
    );
}

export default PartsForm;
