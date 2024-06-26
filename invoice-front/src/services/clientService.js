import client from "@/helpers/client.js";

const clientService = {
    async createClient(clientData) {
        try {
            const response = await client.post('/clients', clientData);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async updateClient(id, clientData) {
        try {
            const response = await client.put(`/clients/${id}`, clientData);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async deleteClient(id) {
        try {
            await client.delete(`/clients/${id}`);
        } catch (error) {
            throw error;
        }
    },
    async getClientById(id) {
        try {
            const response = await client.get(`/clients/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getAllClients(page = 0, sizePerPage = 5, sortBy = 'updatedAt', sortDirection = 'desc') {
        try {
            const response = await client.get('/clients', {
                params: {page, sizePerPage, sortBy, sortDirection},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getAllListClients() {
        try {
            const response = await client.get('/clients/list');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async searchClientsByName(name, page = 0, sizePerPage = 5) {
        try {
            const response = await client.get(`/clients/search`, {
                params: {name, page, sizePerPage},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getClientStatistics(clientId) {
        try {
            const response = await client.get('/dashboard/client-statistics', {
                params: {clientId},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};


export default clientService;