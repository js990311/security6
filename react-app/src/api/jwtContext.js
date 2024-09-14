import { createContext, useContext, useState } from "react"

// context
export const AuthContext = createContext()
export const useAuth = () => useContext(AuthContext)

export default function AuthContextProvider({ children }) {
    const [token, setToken] = useState(localStorage.getItem('accessToken') || '');
    
    const login = (token) => {
        console.log(token);
        localStorage.setItem('accessToken', token);
        setToken(token);
        console.log(
            localStorage.getItem("accessToken")
        );
    }

    const logout = () => { 
        setToken('');
    }

    const isAuthenticated = () => !token;

    return (
        <AuthContext.Provider value={{token, login, logout}}>
            { children }
        </AuthContext.Provider>
    )
}
