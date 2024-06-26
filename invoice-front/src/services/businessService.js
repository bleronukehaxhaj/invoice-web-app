import client from "@/helpers/client.js";

const businessService = {
    async createBusiness(businessData) {
        try {
            const response = await client.post('/businesses', businessData);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async updateBusiness(id, businessData) {
        try {
            const response = await client.put(`/businesses/${id}`, businessData);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async deleteBusiness(id) {
        try {
            await client.delete(`/businesses/${id}`);
        } catch (error) {
            throw error;
        }
    },
    async getBusinessById(id) {
        try {
            const response = await client.get(`/businesses/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getMyBusiness() {
        try {
            const response = await client.get('/businesses/my-business');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

};


export default businessService;