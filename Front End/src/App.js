import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home/Home';

function App() {

    return (
        <Router>
            <Routes>
                <Route path="/folder/:folderId" element={<Home />} />
            </Routes>
        </Router>
    );
}

export default App;