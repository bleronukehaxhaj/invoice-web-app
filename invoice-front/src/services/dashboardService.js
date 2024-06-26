import client from "@/helpers/client.js";

const dashboardService = {

    async getDashboardStatistics() {
        try {
            const response = await client.get('/dashboard/statistics');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};


export default dashboardService;