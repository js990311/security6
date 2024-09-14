import { useEffect, useState } from 'react';
import { useAuth } from '../api/jwtContext';
import { needAuth } from '../api/apiService';

export default function NeedAuth(){
    const { token } = useAuth(); // useAuth 호출
    const [ name, setName ] = useState('');
    useEffect(()=>{
        const getNeedAuth = async () => {
            const responseName = await needAuth(token);
            console.log(responseName);
            setName(responseName);    
        }
        getNeedAuth();
    })
    // const name = 

    return (
        <div>
            <h1>인증되었습니다.</h1>
            <h3> {name} </h3>
        </div>
    )
}