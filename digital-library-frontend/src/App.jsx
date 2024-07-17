import React, { useContext, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Dashboard from './pages/Dashboard';
import SignUp from './pages/SignUp';
import Login from './pages/Login';
import History from './pages/History';
import TransactionHistory from './pages/TransactionHistory';
import Prime from './pages/Prime';
import { Context } from './index';
import axios from 'axios';

function App() {

  const { user, setUser, isAuthenticated, setIsAuthenticated,refresh, setRefresh, loading, setLoading } = useContext(Context);

  useEffect(() => {
    const token = sessionStorage.getItem('token');
    if (token) {
      decodeToken(token);
    }
  }, [isAuthenticated,refresh]);
  
  const decodeToken = (token) => {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  
    axios.get('http://localhost:8080/student/get', {
      withCredentials: true,
    }).then((res) => {
      setUser(res.data);
      setIsAuthenticated(true);
    }).catch((error) => {
      console.error('Error fetching user details:', error);
      setUser({});
      setIsAuthenticated(false);
    });
  };
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<Home/>} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/login" element={<Login />} />
        <Route path="/history" element={<History />} />
        <Route path="/txn-history" element={<TransactionHistory />} />
        <Route path="/prime" element={<Prime/>} />
        <Route />
      </Routes>
    </Router>
  );
}

export default App;
