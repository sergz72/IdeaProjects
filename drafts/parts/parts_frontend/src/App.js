import './App.css';
import { useState } from 'react';
import CategoriesForm from './CategoriesForm';
import SizesForm from './SizesForm';
import UnitsForm from './UnitsForm';
import PrecisionsForm from './PrecisionsForm';
import PartsForm from './PartsForm';

function App() {
    const [activeForm, setActiveForm] = useState(null);

    const handleMenuClick = (menuName) => {
        setActiveForm(menuName);
    };

    return (
        <div className="App">
            <header className="header">
                <nav className="nav">
                    <ul className="nav-list">
                        <li className="nav-item" onClick={() => handleMenuClick('Categories')}>Categories</li>
                        <li className="nav-item" onClick={() => handleMenuClick('Sizes')}>Sizes</li>
                        <li className="nav-item" onClick={() => handleMenuClick('Units')}>Units</li>
                        <li className="nav-item" onClick={() => handleMenuClick('Precisions')}>Precisions</li>
                        <li className="nav-item" onClick={() => handleMenuClick('Parts')}>Parts</li>
                    </ul>
                </nav>
            </header>

            {activeForm === 'Categories' && <CategoriesForm />}
            {activeForm === 'Sizes' && <SizesForm />}
            {activeForm === 'Units' && <UnitsForm />}
            {activeForm === 'Precisions' && <PrecisionsForm />}
            {activeForm === 'Parts' && <PartsForm />}
        </div>
    );
}

export default App;
