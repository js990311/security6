import axios from "axios";

const apiClient = axios.create(
    {
        baseURL : 'http://localhost:8080',
        // headers: {
        //     'Content-Type': 'application/json',
        //   }
    }
);

export const loginApi = async (username, password) => {
    const credentials = btoa(`${username}:${password}`)
    console.log(credentials);
    const resp = await apiClient.get('/jwt/token', {
        headers: {
            'Authorization': `Basic ${credentials}`
        }
    });

    if(resp.status === 200){
        return resp;
    }

    return null;
}