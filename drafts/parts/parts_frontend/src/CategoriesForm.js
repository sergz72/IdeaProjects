import React, { useState, useEffect } from 'react';
import './forms.css';

function CategoriesForm() {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingId, setEditingId] = useState(null);
    const [editingName, setEditingName] = useState('');
    const [newCategoryName, setNewCategoryName] = useState('');
    const [validationError, setValidationError] = useState('');

    useEffect(() => {
        fetchCategories();
    }, [newCategoryName]);

    const fetchCategories = () => {
        const url = new URL(`${process.env.REACT_APP_API_BASE_URL}/categories`);
        if (newCategoryName.trim()) {
            url.searchParams.append('name', newCategoryName.trim());
        }

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setCategories(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    };

    const validateName = (name) => {
        if (!name || name.trim().length === 0) {
            return 'Name is required';
        }
        if (name.trim().length > 50) {
            return 'Name must not exceed 50 characters';
        }
        return null;
    };

    const handleAddCategory = (e) => {
        e.preventDefault();

        const error = validateName(newCategoryName);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/categories`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: newCategoryName.trim() }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add category');
                }
                return response.json();
            })
            .then(() => {
                setNewCategoryName('');
                fetchCategories();
            })
            .catch(error => {
                alert('Error adding category: ' + error.message);
            });
    };

    const handleUpdateCategory = (id) => {
        const error = validateName(editingName);
        if (error) {
            setValidationError(error);
            return;
        }

        setValidationError('');

        fetch(`${process.env.REACT_APP_API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: editingName.trim() }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update category');
                }
                return response.json();
            })
            .then(() => {
                setEditingId(null);
                setEditingName('');
                fetchCategories();
            })
            .catch(error => {
                alert('Error updating category: ' + error.message);
            });
    };

    const handleDeleteCategory = (id) => {
        if (!window.confirm('Are you sure you want to delete this category?')) {
            return;
        }

        fetch(`${process.env.REACT_APP_API_BASE_URL}/categories/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete category');
                }
                fetchCategories();
            })
            .catch(error => {
                alert('Error deleting category: ' + error.message);
            });
    };

    const startEditing = (category) => {
        setEditingId(category.id);
        setEditingName(category.name);
    };

    const cancelEditing = () => {
        setEditingId(null);
        setEditingName('');
        setValidationError('');
    };

    if (loading) {
        return (
            <div>
                <h2>Categories</h2>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div>
                <h2>Categories</h2>
                <p style={{ color: 'red' }}>Error: {error}</p>
            </div>
        );
    }

    return (
        <div>
            <h2>Categories</h2>

            <form onSubmit={handleAddCategory} className="add-form">
                <input
                    type="text"
                    placeholder="New category name"
                    value={newCategoryName}
                    onChange={(e) => setNewCategoryName(e.target.value)}
                    className="input-field"
                    maxLength={50}
                />
                <button type="submit" className="btn btn-add">Add Category</button>
            </form>
            {validationError && <p style={{ color: 'red', marginTop: '5px' }}>{validationError}</p>}

            <table className="form-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {categories.map(category => (
                    <tr key={category.id}>
                        <td>{category.id}</td>
                        <td>
                            {editingId === category.id ? (
                                <input
                                    type="text"
                                    value={editingName}
                                    onChange={(e) => setEditingName(e.target.value)}
                                    className="input-field"
                                    maxLength={50}
                                />
                            ) : (
                                category.name
                            )}
                        </td>
                        <td>
                            {editingId === category.id ? (
                                <>
                                    <button
                                        onClick={() => handleUpdateCategory(category.id)}
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
                                        onClick={() => startEditing(category)}
                                        className="btn btn-edit"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDeleteCategory(category.id)}
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
            {categories.length === 0 && <p>No categories found.</p>}
        </div>
    );
}

export default CategoriesForm;
