import client from "@/helpers/client.js";

export const login = async (credentials) => {
    try {
        const response = await client.post('auth/authenticate', credentials);
        localStorage.setItem('token', response.data.token);
        return response.data;
    } catch (error) {
        throw error;
    }
};


export const register = async (userData) => {
    try {
        const response = await client.post('auth/register', userData);
        localStorage.setItem('token', response.data.token);
        return response.data;
    } catch (error) {
        if (error.response && error.response.status === 409) {
            throw new Error('Email already exists');
        }
        throw error;
    }
};
