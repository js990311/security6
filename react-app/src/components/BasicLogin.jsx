import React, { useState } from 'react';

const BasicLogin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
  
    const submitHdr = async (e) => {
        e.preventDefault();
        const credentials = btoa(`${username}:${password}`)
        try{
            const resp = await fetch('http://localhost:8080/jwt/token', {
                method: 'GET',
                headers : {
                    'Authorization': `Basic ${credentials}`,
                    'Content-Type': 'application/json'          
                }
            });
            if(resp.ok){
                const data = await resp.json();
                console.log(data.accessToken);
                localStorage.setItem("accessToken", data.accessToken);
                console.log(
                    localStorage.getItem("accessToken")
                );
            }
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