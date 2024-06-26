import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://127.0.0.1:8080/api/v1/',

});


axiosInstance.interceptors.request.use(
    (config) => {
        // Do something before request is sent

        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        // Do something with request error
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
        // Do something with response data
        return response;
    },
    (error) => {
        // Do something with response error
        return Promise.reject(error);
    }
);

export default axiosInstance;
