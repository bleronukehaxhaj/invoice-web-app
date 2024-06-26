import client from "@/helpers/client.js";

const invoiceService = {
    async createInvoice(invoiceData) {
        try {
            const response = await client.post('/invoices', invoiceData);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async updateInvoice(id, invoiceData) {
        try {
            const response = await client.put(`/invoices/${id}`, invoiceData);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async deleteInvoice(id) {
        try {
            await client.delete(`/invoices/${id}`);
        } catch (error) {
            throw error;
        }
    },
    async getInvoiceById(id) {
        try {
            const response = await client.get(`/invoices/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getAllInvoices(page = 0,
                         sizePerPage = 5,
                         sortBy = 'updatedAt',
                         sortDirection = 'desc') {
        try {
            const response = await client.get('/invoices', {
                params: {page, sizePerPage, sortBy, sortDirection},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    async getAllListInvoices() {
        try {
            const response = await client.get('/invoices/list');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    async getAllClientInvoices(clientId,
                               page = 0,
                               sizePerPage = 5,
                               sortBy = 'updatedAt',
                               sortDirection = 'desc') {
        try {
            const response = await client.get(`/invoices/client/${clientId}`, {
                params: {page, sizePerPage, sortBy, sortDirection},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    async searchInvoicesByClientName(clientName,
                                     page = 0,
                                     sizePerPage = 5) {
        try {
            const response = await client.get(`/invoices/search`, {
                params: {clientName, page, sizePerPage},
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    async updateInvoiceStatus(invoiceId, status) {
        try {
            const response = await client.patch(`/invoices/${invoiceId}/status`, {status});
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};


export default invoiceService;