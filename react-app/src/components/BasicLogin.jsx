import React, { useState } from 'react';
import { loginApi } from '../api/apiService';
import { useAuth } from '../api/jwtContext';
import { useNavigate } from 'react-router-dom';

const BasicLogin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useAuth(); // useAuth 호출
    const navigate = useNavigate(); 

    const submitHdr = async (e) => {
        e.preventDefault();
        try{
            const resp = await loginApi(username, password);
            login(resp.data.accessToken);
            
            navigate("/");
        }catch(err){
            console.log(err);
        }
    };

    return (
            <form onSubmit={submitHdr}>
                <label htmlFor="username">
                    <input type="text" name="username" onChange={(e)=>{setUsername(e.target.value)}}/>
                </label>
                <label htmlFor="password" name="password">
                    <input type="text" name="username" onChange={(e)=>{setPassword(e.target.value)}}/>
                </label>
                <button type="submit">로그인</button>
            </form>
    );
};

export default BasicLogin;