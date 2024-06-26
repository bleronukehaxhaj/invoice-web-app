import client from "@/helpers/client.js";

const userService = {

    async getUserDetails() {
        try {
            const response = await client.get('/users/me');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};


export default userService;