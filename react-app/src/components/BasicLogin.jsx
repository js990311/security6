import React, { useState } from 'react';
import { loginApi } from '../api/apiService';

const BasicLogin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
  
    const submitHdr = async (e) => {
        e.preventDefault();
        try{
            loginApi(username, password);
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