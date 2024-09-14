import BasicLogin from './BasicLogin';
import AuthContextProvider from '../api/jwtContext';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { useContext } from 'react';

function AuthRoute({children}){
    const {isAuthenticated} = useContext();
    
    if(isAuthenticated()){
        return ({children});
    }
    
    return <Navigate to='/' />
}

export default function RouterApp() {
    return (
        <AuthContextProvider>
            <BrowserRouter>
                <Routes>
                    <Route 
                        path="/login" 
                        element={ <BasicLogin></BasicLogin>}
                    />
                </Routes>
            </BrowserRouter>
        </AuthContextProvider>
    );
}
