import BasicLogin from './BasicLogin';
import AuthContextProvider, { useAuth } from '../api/jwtContext';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import HomeComponent from './HomeComponent';
import NeedAuth from './NeedAuth';


function AuthRoute({children}){
    const {isAuthenticated} = useAuth();
    
    if(isAuthenticated()){
        return children;
    }
    
    return <Navigate to='/' />
}

export default function RouterApp() {
    return (
        <AuthContextProvider>
            <BrowserRouter>
                <Routes>
                    <Route   
                        path='/'
                        element={<HomeComponent />}
                    />
                    <Route 
                        path="/login" 
                        element={ <BasicLogin></BasicLogin>}
                    />
                    <Route 
                        path='/need-auth'
                        element = {
                            <AuthRoute>
                                <NeedAuth></NeedAuth>
                            </AuthRoute>
                        }
                    />
                </Routes>
            </BrowserRouter>
        </AuthContextProvider>
    );
}
