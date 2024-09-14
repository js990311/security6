import axios from "axios";

const apiClient = axios.create(
    {
        baseURL : 'http://localhost:8080',
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

export const needAuth = async (token) => {
    const credentials = token; 
    console.log(credentials);
    const resp = await apiClient.get('/jwt/need-auth', {});

    if(resp.status === 200){
        return resp.data.name;
    }

    return null;

}

export const addHeader = (token) => {
    apiClient.interceptors.request.use(
        (config) => {
            config.headers.Authorization = `Bearer ${token}`
        }
    );
}