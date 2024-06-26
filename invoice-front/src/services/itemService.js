import client from "@/helpers/client.js";

const itemService = {
    async createItem(item) {
        try {
            const response = await client.post('/items', item);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async updateItem(id, item) {
        try {
            const response = await client.put(`/items/${id}`, item);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async deleteItem(id) {
        try {
            await client.delete(`/items/${id}`);
        } catch (error) {
            throw error;
        }
    },
    async getItemById(id) {
        try {
            const response = await client.get(`/items/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getAllItems(page = 0,
                      sizePerPage = 5,
                      sortBy = 'updatedAt',
                      sortDirection = 'desc') {
        try {
            const response = await client.get('/items', {
                params: {page, sizePerPage, sortBy, sortDirection},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getAllListItems() {
        try {
            const response = await client.get('/items/list');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    async searchItemsByDescription(description,
                                   page = 0,
                                   sizePerPage = 5) {
        try {
            const response = await client.get(`/items/search`, {
                params: {description, page, sizePerPage},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    }
};


export default itemService;